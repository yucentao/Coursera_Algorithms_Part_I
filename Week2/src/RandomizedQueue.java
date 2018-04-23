/******************************************************************************
 *  Compilation:  javac InteractivePercolationVisualizer.java
 *  Execution:    java InteractivePercolationVisualizer n
 *  Dependencies: PercolationVisualizer.java Percolation.java
 *                StdDraw.java StdOut.java
 *
 *  This program takes the grid size n as a command-line argument.
 *  Then, the user repeatedly clicks sites to open with the mouse.
 *  After each site is opened, it draws full sites in light blue,
 *  open sites (that aren't full) in white, and blocked sites in black.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int size;
    private Item[] arr;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        arr = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item null");
        }
        if (size == arr.length) {
            resize(arr.length * 2);
        }
        arr[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException("deque empty");
        }
        int num = StdRandom.uniform(size);

        Item tmp = arr[size - 1];
        arr[size - 1] = arr[num];
        arr[num] = tmp;

        Item item = arr[--size];
        arr[size] = null;
        if (size > 0 && size == arr.length / 4) {
            resize(arr.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException("deque empty");
        }
        int num = StdRandom.uniform(size);
        return arr[num];
    }

    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];
        System.arraycopy(arr, 0, copy, 0, size);
        arr = copy;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] shuffle;
        private int index;

        public RandomizedQueueIterator() {
            shuffle = (Item[]) new Object[size];
            index = 0;
            System.arraycopy(arr, 0, shuffle, 0, size);
            StdRandom.shuffle(shuffle);
        }

        @Override
        public boolean hasNext() {
            return index < shuffle.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("deque empty");
            }
            return shuffle[index++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("not support");
        }
    }
}