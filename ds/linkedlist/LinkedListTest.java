package ds.linkedlist;

public class LinkedListTest {
    public static void main(String[] args) {
        LinkedList<Integer> l = new LinkedList<Integer>();
        print("After creating", l);

        l.push(1);
        print("After pushing `1`", l);

        l.push(3);
        print("After pushing `3`", l);

        l.insertAt(2, 1);
        print("After inserting `2` @1", l);

        l.insertAt(0, 0);
        print("After inserting `0` @0", l);

        l.unshift(-1);
        print("After unshifting `-1`", l);

        int r2 = l.removeAt(2);
        print("After removing @2 (" + r2 + ")", l);

        while (!l.isEmpty()) {
            int popped = l.pop();
            print("After popping last element (" + popped + ")", l);
        }

        print("Final", l);
    }

    private static <T> void print(String label, LinkedList<T> i) {
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
