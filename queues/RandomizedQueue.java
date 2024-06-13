import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INITIAL_CAPACITY = 16;
    private int size;
    private Item[] container;

    // construct an empty randomized queue
    public RandomizedQueue() {
        container = (Item[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Resize the array if it reaches its maximum capacity
    private void resize(int newCap) {
        Item[] newContainer = (Item[]) new Object[newCap];
        for (int i = 0; i < size; i++) {
            newContainer[i] = container[i];
        }
        container = newContainer;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == container.length) {
            resize(size * 2);
        }
        container[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        else if (size == 1) {
            Item item = container[0];
            container[0] = null;
            size--;
            return item;
        }
        else {
            int index = StdRandom.uniform(size);
            Item item = container[index];
            for (int j = index; j < size - 1; j++) {
                container[j] = container[j + 1];
            }
            container[--size] = null;
            if (size > 0 && size == container.length / 4) {
                resize(container.length / 2);
            }
            return item;
        }

    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        else if (size == 1) {
            return container[0];
        }
        else {
            int index = StdRandom.uniform(size);
            return container[index];
        }
    }

    // return an independent iterator over items in random order
    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] copyContainer;
        private int copySize;

        public RandomizedQueueIterator() {
            copyContainer = (Item[]) new Object[size];
            copySize = 0;
            System.arraycopy(container, 0, copyContainer, 0, size);
            StdRandom.shuffle(copyContainer);
        }

        @Override
        public boolean hasNext() {
            return copySize < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            else {
                return copyContainer[copySize++];
            }
        }
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // unit testing (required)
    public static void testIterator() {
        int n = 8;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

    public static void main(String[] args) {
        testIterator();
    }
}
