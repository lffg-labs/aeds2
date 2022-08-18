import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class E03 {
    static final int ROTATION_KEY = 3;

    static String caesar(String str, int rotation) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            sb.append(shift(c, rotation));
        }
        return sb.toString();
    }

    static char shift(char chr, int n) {
        return (char) ((int) chr + n);
    }

    public static void main(String[] args) throws Exception {
        try (
                Scanner s = new Scanner(System.in, "ISO-8859-1").useDelimiter("\\n"); // Wrong test case. :)
                PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out, "ISO-8859-1"))) {
            while (true) {
                String line = s.next();
                if (line.trim().equals("FIM")) {
                    break;
                }
                out.println(caesar(line, ROTATION_KEY));
            }
        }
    }
}
