import java.util.Comparator;

public class CircularSuffixArray3 {
    private Integer[] index;
    private char[] double_s_chars;

    // circular suffix array of s
    public CircularSuffixArray3(String s)
    {
        if(s == null) throw new NullPointerException("Null string given.");
        String double_s = s + s;
        double_s_chars = double_s.toCharArray();
        index = new Integer[s.length()];
        for(int i = 0;  i < s.length(); i++)
        {
            index[i] = i;
        }
        sort(index);
    }

    // length of s
    public int length(){ return index.length; }

    // returns index of ith sorted suffix
    public int index(int i)
    {
        if(i > this.length()-1 || i < 0) throw new IndexOutOfBoundsException("Index is out of bounds.");
        return  index[i];
    }


    /*****************************************************************************
     * Quicksort implementation
     *****************************************************************************/
    public void sort(Comparable[] a) {
        sort(a, 0, a.length - 1);
    }

    // quicksort the subarray a[lo .. hi] using 3-way partitioning
    private void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        Comparable v = a[lo];
        int i = lo;
        while (i <= gt) {
            int cmp = compareWith(a[i], v);
            if      (cmp < 0) exch(a, lt++, i++);
            else if (cmp > 0) exch(a, i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(a, lo, lt-1);
        sort(a, gt+1, hi);
    }

    // exchange a[i] and a[j]
    private void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;


    private int compareWith(Comparable v, Comparable w)
    {
        int a = (int) v;
        int b = (int) w;
        char char_v = double_s_chars[a];
        char char_w = double_s_chars[b];
        if(a==b) return 0;
        if(a >= double_s_chars.length -1 || b >= double_s_chars.length -1) return 0;
        if(char_v == char_w) return compareWith(a + 1, b + 1);
        if(char_v < char_w) return -1;
        return 1;
    }


    /*****************************************************************************
     *  Test methods
     *****************************************************************************/
    // unit testing of the methods (optional)
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String input = in.readAll();
        if(args.length > 1)
        {
            int substring_length = Integer.parseInt(args[1]);
            input = input.substring(0, substring_length);
        }
        long startTime = System.currentTimeMillis();
        CircularSuffixArray3 csa = new CircularSuffixArray3(input);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
        for(int i = 0; i<csa.index.length; i++)
        {
            StdOut.print(csa.index[i] + " ");
        }
    }
}