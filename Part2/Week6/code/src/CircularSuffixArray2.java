public class CircularSuffixArray2 {
    private Integer[] index;
    private char[] double_s_chars;

    // circular suffix array of s
    public CircularSuffixArray2(String s)
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

    private void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j - 1);
        sort(a, j+1, hi);
    }

    private int partition(Comparable[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];
        while (true) {

            // find item on lo to swap
            while (less(a[++i], v))
                if (i == hi) break;

            // find item on hi to swap
            while (less(v, a[--j]))
                if (j == lo) break;      // redundant since a[lo] acts as sentinel

            // check if pointers cross
            if (i >= j) break;

            exch(a, i, j);
        }

        // put partitioning item v at a[j]
        exch(a, lo, j);

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
    }

    private boolean less(Comparable v, Comparable w)
    {
        int a = (int) v;
        int b = (int) w;
        char char_v = double_s_chars[a];
        char char_w = double_s_chars[b];
        if(a==b) return false;
        if(a >= double_s_chars.length -1 || b >= double_s_chars.length -1) return true;
        if(char_v == char_w) return less(a+1,b+1);
        return (char_v < char_w);
    }

    private void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
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
        CircularSuffixArray2 csa = new CircularSuffixArray2(input);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
        for(int i = 0; i<csa.index.length ; i++)
        {
            StdOut.print(csa.index[i] + " ");
        }
    }
}