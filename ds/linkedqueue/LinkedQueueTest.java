package ds.linkedqueue;

public class LinkedQueueTest {
    public static void main(String[] args) {
        LinkedQueue<Integer> l = LinkedQueue.withCapacity(3);
        print("After creating", l, 'f');

        System.out.println("Is empty? " + l.isEmpty());

        l.pushFront(2);
        print("After pushFront(2)", l, 'f');

        System.out.println("Is empty? " + l.isEmpty());

        l.pushFront(1);
        print("After pushFront(1)", l, 'f');

        System.out.println("Is full? " + l.isFull());

        l.pushBack(3);
        print("After pushBack(3)", l, 'f');

        System.out.println("Is full? " + l.isFull());

        try {
            System.out.println("Trying to pushBack(4)...");
            l.pushBack(4);
        } catch (RuntimeException ex) {
            System.out.println("Caught `" + ex.getMessage() + "`.");
        }

        print("Showing backwards iteration", l, 'b');

        int pf1 = l.popFront();
        print("After popFront() -> (" + pf1 + "), showing backwards", l, 'b');

        System.out.println("Is full? " + l.isFull());

        int pb1 = l.popBack();
        print("After popBack() -> (" + pb1 + ")", l, 'f');

        int pf2 = l.popFront();
        print("After popFront() -> (" + pf2 + ")", l, 'f');
    }

    private static <T> void print(String label, LinkedQueue<T> q, char kind) {
        System.out.println(label + ":");
        System.out.print("[");
        boolean first = true;
        Iterable<T> iter = kind == 'f'
                ? q.frontToBackIterator()
                : q.backToFrontIterator();
        for (T el : iter) {
            if (!first) {
                System.out.print(", ");
            }
            first = false;
            System.out.print(el);
        }
        System.out.println("] (" + q.length() + ")");
    }
}
