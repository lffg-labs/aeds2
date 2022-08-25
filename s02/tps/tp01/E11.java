import java.util.Scanner;

class E11 {
    static boolean isPalindrome(String str) {
        if (str.length() <= 1) {
            return true;
        }
        int ei = str.length() - 1;
        return str.charAt(0) == str.charAt(ei)
                && isPalindrome(str.substring(1, ei));
    }

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNextLine()) {
                String line = s.nextLine().trim();
                if (line.equals("FIM")) {
                    break;
                }
                boolean a = isPalindrome(line);
                System.out.println(a ? "SIM" : "NAO");
            }
        }
    }
}
