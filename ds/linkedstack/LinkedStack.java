package ds.linkedstack;

import java.util.Iterator;

// In this implementation, one just inserts using `first` and pops from it. :)
class LinkedStack<T> implements Iterable<T> {
    private int len;
    private Node<T> first;

    public LinkedStack() {
        len = 0;
        first = null;
    }

    public int length() {
        return len;
    }

    public boolean isEmpty() {
        return len == 0;
    }

    public void push(T val) {
        first = new Node<T>(val, first);
        len++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Already empty");
        }
        T data = first.data;
        first = first.next;
        len--;
        return data;
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
        return new LinkedStackIterator<T>(this);
    }

    private static class LinkedStackIterator<T> implements Iterator<T> {
        private Node<T> curr;

        public LinkedStackIterator(LinkedStack<T> list) {
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
