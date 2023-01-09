/**
 * Created by nikos on 22/2/2014.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int N;         // number of elements on queue
    private Node first;    // beginning of queue
    private Node last;     // end of queue

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    /**
     * Constructs an empty deque
     */
    public Deque() {
        first = null;
        last  = null;
        N = 0;
    }

    /**
     * is the deque empty?
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Return the number of items on the deque.
     * @return the number of items in this deque
     */
    public int size() {
        return N;
    }

    /**
     * Inserts the item at the front of the deque.
     * @param item the item to add
     */
    public void addFirst(Item item){
        if (item == null) throw new NullPointerException("Cannot add null item");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.previous = null;
        if (isEmpty()){
            last = first;
        } else {
            oldfirst.previous = first;
        }
        N++;
    }

    /**
     * Inserts the item at the end of the deque.
     * @param item the item to add
     */
    public void addLast(Item item){
        if (item == null) throw new NullPointerException("Cannot add null item");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if (isEmpty()){
            first = last;
        } else {
            oldlast.next = last;
        }
        N++;
    }

    /**
     * Deletes and returns the item at the front of the deque.
     * @return the first item from the deque
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = first.item;     // save item to return
        N--;
        if (isEmpty()) {
            first = null;           // to avoid loitering
            last = null;            // to avoid loitering
        } else {
            first = first.next;     // delete first node
            first.previous = null;
        }
        return item;                // return the saved item
    }

    /**
     * Delete and returns the item at the end of the deque.
     * @return the last item from the deque
     * @throws java.util.NoSuchElementException if the deque is empty
     */
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = last.item;
        N--;
        if (isEmpty()) {
            last = null;        // to avoid loitering
            first = null;        // to avoid loitering
        } else {
            last = last.previous;   // delete first node
            last.next = null;
        }
        return item;             // return the saved item
    }

    /**
     * Returns an iterator over items in order from front to end.
     * @return an iterator over items in order from front to end
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // An iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item>{
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    /**
     * Basic unit testing
     * @param args
     */
    public static void main(String[] args){
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        deque.removeFirst();
        deque.addLast(1);
        deque.removeLast();
        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(6);
        deque.removeFirst();
        deque.removeLast();
        deque.removeFirst();
        deque.removeLast();
        for (Integer i : deque){
            StdOut.println(i);
        }
    }
}