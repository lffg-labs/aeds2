import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class E04 {
    public static void main(String[] args) throws IOException {
        try (
                Scanner s = new Scanner(System.in, "ISO-8859-1").useDelimiter("\\n"); // Wrong test case. :)
                PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out, "ISO-8859-1"))) {
            Random rng = new Random();
            rng.setSeed(4);

            while (true) {
                String line = s.next();
                if (line.trim().equals("FIM")) {
                    break;
                }
                out.println(randomReplace(rng, line));
            }
        }
    }

    static String randomReplace(Random rng, String str) {
        char from = next(rng);
        char to = next(rng);
        return str.replace(from, to);
    }

    static char next(Random rng) {
        int range = (int) 'z' - (int) 'a' + 1;
        return (char) ((int) 'a' + Math.abs(rng.nextInt()) % range);
    }
}
