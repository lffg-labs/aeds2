import java.lang.Character;
import java.util.Scanner;

public class E01 {
    private static int countUpper(String string) {
        return (int) string
                .codePoints()
                .filter(c -> Character.isUpperCase((char) c))
                .count();
    }

    public static void main(String args[]) {
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.equals("FIM")) {
                    break;
                }
                System.out.println(countUpper(line));
            }
        }
    }
}
