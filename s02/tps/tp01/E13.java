import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class E13 {
    static final int ROTATION_KEY = 3;

    static String caesar(String str, int rotation) {
        return caesar(str, rotation, new StringBuilder()).toString();
    }

    static StringBuilder caesar(String str, int rotation, StringBuilder sb) {
        if (str.length() <= 0) {
            return sb;
        }
        sb.append(shift(str.charAt(0), rotation));
        return caesar(str.substring(1), rotation, sb);
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
