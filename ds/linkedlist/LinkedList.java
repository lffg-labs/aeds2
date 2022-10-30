package ds.linkedlist;

import java.util.Iterator;

class LinkedList<T> implements Iterable<T> {
    private int len;
    private Node<T> first;

    public LinkedList() {
        len = 0;
        first = null;
    }

    public int length() {
        return len;
    }

    public boolean isEmpty() {
        return len == 0;
    }

    public void insertAt(T val, int pos) {
        // `insertAt` may be used to insert an element at the end, so one does
        // not use `checkBoundStrict` here.
        if (pos > len) {
            throw new RuntimeException("Out of bounds");
        }
        if (pos == 0) {
            // First node needs special handling.
            first = new Node<T>(val, first);
        } else {
            Node<T> prev = at(pos - 1);
            prev.next = new Node<T>(val, prev.next);
        }
        len++;
    }

    public void unshift(T val) {
        insertAt(val, 0);
    }

    public void push(T val) {
        insertAt(val, len);
    }

    public T removeAt(int pos) {
        checkBoundStrict(pos);
        T data;
        if (pos == 0) {
            // First node needs special handling.
            data = first.data;
            first = first.next;
        } else {
            Node<T> prev = at(pos - 1);
            data = prev.next.data;
            prev.next = prev.next.next;
        }
        len--;
        return data;
    }

    public T pop() {
        return removeAt(len - 1);
    }

    public T shift() {
        return removeAt(0);
    }

    public T get(int pos) {
        checkBoundStrict(pos);
        return at(pos).data;
    }

    public void set(T val, int pos) {
        checkBoundStrict(pos);
        at(pos).data = val;
    }

    public void swap(int a, int b) {
        if (a == b) {
            return; // noop
        }

        int max, min;
        if (a > b) {
            max = a;
            min = b;
        } else {
            max = b;
            min = a;
        }
        checkBoundStrict(max);
        int sub = max - min;

        Node<T> fst = at(min);
        Node<T> snd = fst;
        while (sub-- > 0) {
            snd = snd.next;
        }

        T temp = fst.data;
        fst.data = snd.data;
        snd.data = temp;
    }

    private Node<T> at(int pos) {
        checkBoundStrict(pos);
        Node<T> curr = first;
        while (pos-- > 0) {
            curr = curr.next;
        }
        return curr;
    }

    private void checkBoundStrict(int pos) {
        if (pos >= len) {
            throw new RuntimeException("Out of bounds");
        }
    }

    private static class Node<T> {
        public T data;
        public Node<T> next;

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator<T>(this);
    }

    private static class LinkedListIterator<T> implements Iterator<T> {
        private Node<T> curr;

        public LinkedListIterator(LinkedList<T> list) {
            curr = list.first;
        }

        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public T next() {
            T data = curr.data;
            curr = curr.next;
            return data;
        }
    }
}
