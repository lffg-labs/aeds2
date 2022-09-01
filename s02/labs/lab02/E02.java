import java.util.Scanner;

public class E02 {
    static String formatReversedNumber(int n) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(n % 10);
            n /= 10;
        } while (n > 0);
        return sb.toString();
    }

    static void formatMirrored(int from, int to, StringBuilder sb) {
        if (from > to) {
            return;
        }

        sb.append(from);
        formatMirrored(from + 1, to, sb);
        sb.append(formatReversedNumber(from));

    }

    static String formatMirrored(int from, int to) {
        StringBuilder sb = new StringBuilder();
        formatMirrored(from, to, sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            while (s.hasNext()) {
                int from = Integer.parseInt(s.next());
                int to = Integer.parseInt(s.next());
                if (from > to) {
                    throw new RuntimeException("First number can't be greater than the second.");
                }
                System.out.println(formatMirrored(from, to));
            }
        }
    }
}
