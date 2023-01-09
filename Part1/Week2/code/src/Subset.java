/**
 * Created by nikos on 23/2/2014.
 */
public class Subset {
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue();
        while(!StdIn.isEmpty()) {
            String inputString = StdIn.readString();
            randomizedQueue.enqueue(inputString);
        }
        int printNumber = Integer.parseInt(args[0]);
        String[] printedStrings = new String[printNumber];
        if(printNumber > randomizedQueue.size()) printNumber=randomizedQueue.size();
        for(int i = 1; i <= printNumber; i++) {
            StdOut.println(randomizedQueue.dequeue());
        }

    }
}
