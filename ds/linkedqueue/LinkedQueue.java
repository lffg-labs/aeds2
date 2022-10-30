package ds.linkedqueue;

import java.util.Iterator;
import java.util.function.Function;

class LinkedQueue<T> {
    private int cap;
    private int len;
    private Node<T> first;
    private Node<T> last;

    public static <T> LinkedQueue<T> withCapacity(int capacity) {
        return new LinkedQueue<T>(capacity);
    }

    private LinkedQueue(int capacity) {
        cap = capacity;
        len = 0;
        first = null;
        last = null;
    }

    public int capacity() {
        return cap;
    }

    public int length() {
        return len;
    }

    public boolean isFull() {
        return len == cap;
    }

    public boolean isEmpty() {
        return len == 0;
    }

    public void pushFront(T val) {
        assertHasCapacityFor(1);
        first = new Node<T>(val, null, first);
        if (isEmpty()) {
            last = first;
        }
        if (first.next != null) {
            first.next.prev = first;
        }
        len++;
    }

    public T popFront() {
        assertNonEmpty();
        T data = first.data;
        first = first.next;
        if (first != null) {
            first.prev = null;
        }
        len--;
        return data;
    }

    public void pushBack(T val) {
        assertHasCapacityFor(1);
        last = new Node<T>(val, last, null);
        if (isEmpty()) {
            first = last;
        }
        if (last.prev != null) {
            last.prev.next = last;
        }
        len++;
    }

    public T popBack() {
        assertNonEmpty();
        T data = last.data;
        last = last.prev;
        if (last != null) {
            last.next = null;
        }
        len--;
        return data;
    }

    private void assertHasCapacityFor(int needed) {
        if (len + needed > cap) {
            throw new RuntimeException("Not enough capacity");
        }
    }

    private void assertNonEmpty() {
        if (isEmpty()) {
            throw new RuntimeException("Already empty");
        }
    }

    private static class Node<T> {
        public T data;
        public Node<T> prev;
        public Node<T> next;

        public Node(T data, Node<T> prev, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    public Iterable<T> frontToBackIterator() {
        return () -> new LinkedQueueIterator<T>(first, (node) -> node.next);
    }

    public Iterable<T> backToFrontIterator() {
        return () -> new LinkedQueueIterator<T>(last, (node) -> node.prev);
    }

    private static class LinkedQueueIterator<T> implements Iterator<T> {
        private Node<T> curr;
        private Function<Node<T>, Node<T>> nextFn;

        public LinkedQueueIterator(Node<T> curr, Function<Node<T>, Node<T>> nextFn) {
            this.curr = curr;
            this.nextFn = nextFn;
        }

        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public T next() {
            T data = curr.data;
            curr = nextFn.apply(curr);
            return data;
        }
    }
}
