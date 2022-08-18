import java.util.Scanner;

public class E01 {
    static boolean isPalindrome(String str) {
        int middle = str.length() / 2;
        for (int i = 0; i < middle; i++) {
            int current = str.charAt(i);
            int opposite = str.charAt(str.length() - (1 + i));
            if (current != opposite) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if ("FIM".equals(line)) {
                    break;
                }
                System.out.println(isPalindrome(line) ? "SIM" : "NAO");
            }
        }
    }
}
