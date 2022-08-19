import java.util.Scanner;

public class E06 {
    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.equals("FIM")) {
                    break;
                }
                String a = yesNo(isVowel(line));
                String b = yesNo(isConsonant(line));
                String c = yesNo(isInt(line));
                String d = yesNo(isFloat(line));
                System.out.println(String.format("%s %s %s %s", a, b, c, d));
            }
        }
    }

    static String yesNo(boolean b) {
        return b ? "SIM" : "NAO";
    }

    static boolean isInt(String src) {
        for (char c : src.toCharArray()) {
            if (!isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    static boolean isFloat(String src) {
        boolean sep = false;
        for (char c : src.toCharArray()) {
            if (!sep && (c == '.' || c == ',')) {
                sep = true;
                continue;
            }
            if (!isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    static boolean isVowel(String src) {
        return src.chars().allMatch(c -> isVowel((char) c));
    }

    static boolean isVowel(char c) {
        char cl = Character.toLowerCase(c);
        return cl == 'a' || cl == 'e' || cl == 'i' || cl == 'o' || cl == 'u';
    }

    static boolean isConsonant(String src) {
        return src.chars().allMatch(c -> isConsonant((char) c));
    }

    static boolean isConsonant(char c) {
        int cl = (int) Character.toLowerCase(c);
        return (int) 'a' <= cl && cl <= (int) 'z' && !isVowel(c);
    }

    static boolean isDigit(char c) {
        return (int) '0' <= (int) c && (int) c <= (int) '9';
    }
}
