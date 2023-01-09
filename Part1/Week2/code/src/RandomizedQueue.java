/**
 * Created by nikos on 23/2/2014.
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] originalArray;           // array of items
    private int N;                          // number of elements on stack

    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
        originalArray = (Item[]) new Object[2];
    }

    /**
     * Is the queue empty?
     * @return true if this  queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Returns the number of items on the queue.
     * @return the number of items on the queue
     */
    public int size(){
        return N;
    }

    /**
     * Add the item in the queue
     * @param item to be added in the queue
     */
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException("Cannot add null item");
        if (N == originalArray.length) resize(2* originalArray.length);    // double size of array if necessary
        originalArray[N++] = item;                            // add item
    }

    // resize the underlying array
    private void resize(int max) {
        assert max >= N;
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) {
            temp[i] = originalArray[i];
        }
        originalArray = temp;
    }

    /**
     * Deletes and return originalArray random item.
     * @return originalArray random item
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        int randomIndex = StdRandom.uniform(N);
        Item item = originalArray[randomIndex];
        originalArray[randomIndex] = originalArray[N-1];
        originalArray[N-1] = null;                                 // to avoid loitering
        N--;
        // shrink size of array if necessary
        if (N > 0 && N == originalArray.length/4) resize(originalArray.length/2);
        return item;
    }

    /**
     * Return (but does not delete) originalArray random item
     * @return originalArray random item
     */
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        return originalArray[StdRandom.uniform(N)];
    }

    /**
     * Returns an independent iterator over items in random order
     * @return an independent iterator over items in random order
     */
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomArrayIterator implements Iterator<Item> {
        private int i;
        private Item[] shuffledArray;         // array of shuffled items

        public RandomArrayIterator() {
            i = N;
            shuffledArray = (Item[]) new Object[N];
            //Needs array copy cause if this.b = originalArray we point originalArray,b to the same values (array aliasing)
            System.arraycopy(originalArray, 0, shuffledArray, 0, N );
            if (hasNext()) StdRandom.shuffle(shuffledArray, 0 , N-1 );
        }

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return shuffledArray[--i];
        }
    }

    /**
     * Basic unit testing
     * @param args
     */
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<Integer>();
        randomizedQueue.enqueue(0);
        randomizedQueue.enqueue(1);
        randomizedQueue.enqueue(2);
        randomizedQueue.enqueue(3);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(5);
        randomizedQueue.enqueue(6);
        randomizedQueue.enqueue(7);
        randomizedQueue.enqueue(8);
        randomizedQueue.dequeue();
        randomizedQueue.dequeue();
        randomizedQueue.dequeue();
        for (Integer i : randomizedQueue){
            StdOut.println(i);
        }
        StdOut.println("--------");
        for (Integer i : randomizedQueue){
            StdOut.println(i);
        }
    }
}