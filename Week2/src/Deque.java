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

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int size;

    // construct an empty deque
    public Deque() {
        head = new Node(null);
        tail = new Node(null);
        head.next = tail;
        tail.prev = head;
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
            throw new IllegalArgumentException("item null");
        }
        Node node = new Node(item);
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
        node.prev = head;
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item null");
        }
        Node node = new Node(item);
        node.prev = tail.prev;
        tail.prev.next = node;
        tail.prev = node;
        node.next = tail;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("deque empty");
        }
        Node node = head.next;
        head.next = node.next;
        node.next.prev = head;
        size--;
        return node.item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("deque empty");
        }
        Node node = tail.prev;
        tail.prev = node.prev;
        node.prev.next = tail;
        size--;
        return node.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        Node cur = head;

        @Override
        public boolean hasNext() {
            return cur.next != tail;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("deque empty");
            }
            cur = cur.next;
            return cur.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("not support");
        }
    }

    private class Node {
        private Item item;
        private Node prev;
        private Node next;

        public Node(Item item) {
            this.item = item;
        }
    }
}