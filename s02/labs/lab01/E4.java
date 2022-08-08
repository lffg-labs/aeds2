import java.lang.Character;
import java.util.Scanner;

public class E4 {
    public static int countUpper(String string) {
        int upperCount = 0;
        for (int i = 0; i < string.length(); i++) {
            char curr = string.charAt(i);
            if (Character.isUpperCase(curr)) {
                upperCount++;
            }
        }
        return upperCount;
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String curr = scanner.nextLine();
            if ("FIM".equals(curr)) {
                break;
            }
            System.out.println(E4.countUpper(curr));
        }
        scanner.close();
    }
}
