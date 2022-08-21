import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class E09 {
    private static final String FILE_NAME = "e09-store";

    public static String prettyFloat(float f) {
        if (f == (int) f) {
            return String.valueOf((int) f);
        } else {
            return String.valueOf(f);
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File(FILE_NAME);

        // Make sure one is writing to a fresh file.
        file.delete();

        // Open for write.
        try (
                Scanner s = new Scanner(System.in);
                RandomAccessFile f = new RandomAccessFile(file, "rw")) {
            int count = Integer.parseInt(s.nextLine().trim());

            while (count-- > 0) {
                float current = Float.parseFloat(s.nextLine().trim());
                // n.b. I am aware of `f.writeFloat`.
                f.writeInt(Float.floatToIntBits(current));
            }
        }

        // Open for read.
        try (RandomAccessFile f = new RandomAccessFile(file, "r")) {
            int byteCount = (int) f.length();
            int iterCount = byteCount / 4;

            for (int i = 1; i <= iterCount; i++) {
                f.seek(byteCount - i * 4);
                float num = Float.intBitsToFloat(f.readInt());
                System.out.println(prettyFloat(num));
            }
        }
    }
}
