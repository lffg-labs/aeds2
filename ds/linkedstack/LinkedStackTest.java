package ds.linkedstack;

public class LinkedStackTest {
    public static void main(String[] args) {
        LinkedStack<Integer> l = new LinkedStack<Integer>();
        print("After creating", l);

        l.push(1);
        print("After pushing `1`", l);

        l.push(2);
        print("After pushing `2`", l);

        while (!l.isEmpty()) {
            int popped = l.pop();
            print("Popped (" + popped + ")", l);
        }

        print("Final", l);
    }

    private static <T> void print(String label, LinkedStack<T> i) {
        System.out.println(label + ":");
        System.out.print("[");
        boolean first = true;
        for (T el : i) {
            if (!first) {
                System.out.print(", ");
            }
            first = false;
            System.out.print(el);
        }
        System.out.println("] (" + i.length() + ")");
    }
}
