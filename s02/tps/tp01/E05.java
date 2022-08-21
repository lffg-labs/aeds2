import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

// Syntax.
//
//    <Program> ::= <Control> <Expr>
//    
//    <Control> ::= <Number> ( <Bit> )*
//    
//    <Ident> ::= ([a-z] | [A-Z])+
//    <Number> ::= [0-9]+
//    <Bit> ::= "0" | "1"
//    
//    <Expr> ::= <VarExpr> | <AppExpr>
//    
//    <VarExpr> ::= <Ident>
//    <AppExpr> ::= <Ident> "(" <AppArgs>? ")"
//    <AppArgs> ::= <Expr> ( "," <Expr> )* ","?

class Lexer {
    private List<Token> tokens;
    private final String source;
    private int cursor;

    //
    // Main implementation.
    //

    private void nextToken() throws PipelineException {
        char current = current();

        switch (current) {
            case '(':
                bump();
                add(Token.Kind.LPAREN, "(");
                return;
            case ')':
                bump();
                add(Token.Kind.RPAREN, ")");
                return;
            case ',':
                bump();
                add(Token.Kind.COMMA, ",");
                return;
        }

        if (Character.isWhitespace(current)) {
            bumpWhileNext(c -> Character.isWhitespace(c));
            // This implementation straight away ignore spaces.
            return;
        }

        if (isAsciiAlphabetic(current)) {
            String lexme = bumpWhileNext(c -> isAsciiAlphabetic(c));
            add(Token.Kind.IDENT, lexme);
            return;
        }

        if (isAsciiDigit(current)) {
            String lexme = bumpWhileNext(c -> isAsciiDigit(c));
            add(Token.Kind.NUMBER, lexme);
            return;
        }

        throw new PipelineException("woo");
    }

    //
    // Utility methods.
    //

    private char current() {
        if (cursor >= source.length()) {
            return '\0';
        }
        return source.charAt(cursor);
    }

    private String bumpWhileNext(Function<Character, Boolean> pred) {
        StringBuilder buf = new StringBuilder();
        do {
            buf.append(bump());
        } while (pred.apply(current()));
        return buf.toString();
    }

    private char bump() {
        return source.charAt(cursor++);
    }

    private void add(Token.Kind kind, String lexme) {
        tokens.add(new Token(kind, lexme));
    }

    private void add(Token.Kind kind) {
        tokens.add(new Token(kind));
    }

    private boolean isAsciiAlphabetic(char chr) {
        char c = Character.toLowerCase(chr);
        return 'a' <= c && c <= 'z';
    }

    private boolean isAsciiDigit(char chr) {
        return '0' <= chr && chr <= '9';
    }

    //
    // Public interface.
    //

    public Lexer(String source) {
        this.source = source;
        cursor = 0;
        tokens = new ArrayList<>();
    }

    public List<Token> lex() throws PipelineException {
        while (cursor < source.length()) {
            nextToken();
        }
        add(Token.Kind.EOF);
        return tokens;
    }
}

class Parser {
    private final List<Token> tokens;
    private int cursor;

    //
    // Main implementation.
    //

    private Prog parseProg() throws PipelineException {
        int argCount = parseNumber();
        if (argCount > Evaluator.MAX_PROGRAM_ARGS) {
            throw new PipelineException("Cannot declare more than 26 parameters");
        }
        List<Boolean> args = new ArrayList<>();
        for (int i = 0; i < argCount; i++) {
            args.add(parseBit());
        }
        Expr expr = parseExpr();
        return new Prog(args, expr);
    }

    private Expr parseExpr() throws PipelineException {
        String ident = parseIdent();
        if (is(Token.Kind.LPAREN)) {
            return parseApp(ident);
        }
        return new Expr.Var(ident);
    }

    private Expr.App parseApp(String ident) throws PipelineException {
        consume(Token.Kind.LPAREN);
        List<Expr> args = parseAppArgs();
        consume(Token.Kind.RPAREN);
        return new Expr.App(ident, args);
    }

    private List<Expr> parseAppArgs() throws PipelineException {
        List<Expr> args = new ArrayList<>();
        if (is(Token.Kind.RPAREN)) {
            return args;
        }
        loop: while (true) {
            args.add(parseExpr());
            switch (expect(new Token.Kind[] { Token.Kind.COMMA, Token.Kind.RPAREN })) {
                case COMMA:
                    bump();
                    continue;
                case RPAREN:
                    break loop;
                default:
                    // Unreachable per `expect` functionality.
            }
        }
        return args;
    }

    private String parseIdent() throws PipelineException {
        Token token = consume(Token.Kind.IDENT);
        return token.lexme;
    }

    private int parseNumber() throws PipelineException {
        Token token = consume(Token.Kind.NUMBER);
        try {
            return Integer.parseInt(token.lexme);
        } catch (NumberFormatException e) {
            throw new PipelineException("Unparsable number");
        }
    }

    private boolean parseBit() throws PipelineException {
        int num = parseNumber();
        if (num == 0 || num == 1) {
            return num == 1;
        }
        throw new PipelineException(String.format(
                "Expected bit (i.e. `0` or `1`), instead got `%s`", num));
    }

    //
    // Utility methods.
    //

    private Token bump() throws PipelineException {
        if (cursor >= tokens.size()) {
            throw new PipelineException("bug: Cannot advance past EOF");
        }
        return tokens.get(cursor++);
    }

    private Token peek() {
        return tokens.get(cursor);
    }

    private Token.Kind peekKind() {
        return peek().kind;
    }

    private boolean is(Token.Kind expected) {
        return peekKind() == expected;
    }

    private Token.Kind expect(Token.Kind[] expectedKinds) throws PipelineException {
        for (Token.Kind expected : expectedKinds) {
            if (is(expected)) {
                return expected;
            }
        }
        throw new PipelineException(String.format(
                "Unexpected token of kind `%s`", peekKind()));
    }

    private Token consume(Token.Kind expected) throws PipelineException {
        if (is(expected)) {
            return bump();
        }
        throw new PipelineException(String.format(
                "Expected token of kind `%s`, instead got `%s`", expected, peekKind()));
    }

    //
    // Public interface.
    //

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        cursor = 0;
    }

    public Prog parse() throws PipelineException {
        Prog prog = parseProg();
        consume(Token.Kind.EOF);
        return prog;
    }
}

class Evaluator implements Expr.Visitor<Boolean> {
    private Environment env;
    private final Prog prog;

    //
    // Main implementation.
    //

    @Override
    public Boolean visitAppExpr(Expr.App app) throws PipelineException {
        switch (app.ident) {
            case "not":
                return appNot(app.args);
            case "and":
                return appAnd(app.args);
            case "or":
                return appOr(app.args);
            default:
                throw new PipelineException(String.format(
                        "Cannot call undefined function `%s`", app.ident));
        }
    }

    private boolean appNot(List<Expr> args) throws PipelineException {
        if (args.size() != 1) {
            throw new PipelineException("Expected single argument in `not`");
        }
        return !args.get(0).accept(this);
    }

    private boolean appAnd(List<Expr> args) throws PipelineException {
        for (Expr arg : args) {
            if (!arg.accept(this)) {
                return false;
            }
        }
        return true;
    }

    private boolean appOr(List<Expr> args) throws PipelineException {
        for (Expr arg : args) {
            if (arg.accept(this)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean visitVarExpr(Expr.Var var) throws PipelineException {
        return env.get(var.ident);
    }

    //
    // Utility methods and classes.
    //

    public static final int MAX_PROGRAM_ARGS = 26;

    private class Environment {
        private HashMap<String, Boolean> env;
        private char next;

        public Environment() {
            env = new HashMap<>();
            next = 'A';
        }

        public void addNext(boolean bit) throws PipelineException {
            if (env.size() >= MAX_PROGRAM_ARGS) {
                throw new PipelineException("Cannot add more than 26 variables");
            }
            env.put(Character.toString(next++), bit);
        }

        public boolean get(String ident) throws PipelineException {
            if (!env.containsKey(ident)) {
                throw new PipelineException(String.format("Cannot read undefined variable `%s`", ident));
            }
            return env.get(ident);
        }
    }

    //
    // Public interface.
    //

    public Evaluator(Prog prog) {
        this.prog = prog;
        env = new Environment();
    }

    public boolean eval() throws PipelineException {
        for (boolean arg : prog.args) {
            env.addNext(arg);
        }
        return prog.expr.accept(this);
    }
}

class Token {
    public final String lexme;
    public final Token.Kind kind;

    public Token(Token.Kind kind, String lexme) {
        this.kind = kind;
        this.lexme = lexme;
    }

    public Token(Token.Kind kind) {
        this.kind = kind;
        lexme = "";
    }

    @Override
    public String toString() {
        if (lexme.length() == 0) {
            return kind.toString();
        } else {
            return String.format("(%s `%s`)", kind, lexme);
        }
    }

    public enum Kind {
        IDENT,
        NUMBER,
        LPAREN,
        RPAREN,
        COMMA,
        WHITESPACE,
        EOF,
    }
}

abstract class Expr {
    public abstract <T> T accept(Visitor<T> visitor) throws PipelineException;

    @Override
    public abstract String toString();

    public abstract List<Expr> children();

    public interface Visitor<T> {
        T visitAppExpr(App expr) throws PipelineException;

        T visitVarExpr(Var expr) throws PipelineException;
    }

    public static class App extends Expr {
        public final String ident;
        public final List<Expr> args;

        public App(String ident, List<Expr> args) {
            this.ident = ident;
            this.args = args;
        }

        public <T> T accept(Visitor<T> visitor) throws PipelineException {
            return visitor.visitAppExpr(this);
        }

        @Override
        public String toString() {
            return "APP";
        }

        public List<Expr> children() {
            return args;
        }
    }

    public static class Var extends Expr {
        public final String ident;

        public Var(String ident) {
            this.ident = ident;
        }

        public <T> T accept(Visitor<T> visitor) throws PipelineException {
            return visitor.visitVarExpr(this);
        }

        @Override
        public String toString() {
            return String.format("VAR (\"%s\")", ident);
        }

        public List<Expr> children() {
            return new ArrayList<>();
        }
    }
}

class Prog {
    public final List<Boolean> args;
    public final Expr expr;

    public Prog(List<Boolean> args, Expr expr) {
        this.args = args;
        this.expr = expr;
    }
}

class Pipeline {
    public static boolean run(String in) throws PipelineException {
        Lexer l = new Lexer(in);
        List<Token> tokens = l.lex();

        Parser p = new Parser(tokens);
        Prog prog = p.parse();

        Evaluator e = new Evaluator(prog);
        return e.eval();
    }
}

class PipelineException extends Exception {
    private final String message;

    public PipelineException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}

class ExprPrinter {
    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    private static void print(Expr expr, int level) {
        System.out.println(repeat(" ", level * 4) + expr);
        for (Expr child : expr.children()) {
            print(child, level + 1);
        }
    }

    public static void print(Expr expr) {
        print(expr, 0);
    }
}

public class E05 {
    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.equals("0") || line.equals("FIM")) {
                    break;
                }
                try {
                    char res = Pipeline.run(line) ? '1' : '0';
                    System.out.println(res);
                } catch (PipelineException e) {
                    String message = String.format("(error: at (%s) (%s))", line, e);
                    throw new RuntimeException(message);
                }
            }
        }
    }
}
