import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Item data;
        Node next;
        Node prev;

        private Node() {
            this.data = null;
            this.prev = null;
            this.next = null;
        }

        private Node(Item data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current;

        public DequeIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.data;
            current = current.next;
            return item;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    // construct an empty deque
    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            head = new Node(item, null, null);
            tail = head;
            size++;
        }
        else {
            Node newNode = new Node(item, null, head);
            head.prev = newNode;
            head = newNode;
            size++;
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            head = new Node(item, null, null);
            tail = head;
            size++;
        }
        else {
            Node newNode = new Node(item, tail, null);
            tail.next = newNode;
            tail = newNode;
            size++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item head_data = head.data;
        if (head == tail) {
            head = null;
            tail = null;
            size--;
        }
        else {
            head = head.next;
            head.prev = null;
            size--;
        }
        return head_data;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Item tail_data = tail.data;
        if (tail == head) {
            head = null;
            tail = null;
            size--;
        }
        else {
            tail = tail.prev;
            tail.next = null;
            size--;
        }
        return tail_data;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void testIterator() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(6);
        deque.removeFirst();
        deque.removeLast();
        for (Integer item : deque) {
            StdOut.print(item + " ");
        }
    }

    public static void main(String[] args) {
        testIterator();
    }

}
