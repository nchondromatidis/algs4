import java.util.Arrays;

public class SAP {

    private static final int INFINITY = Integer.MAX_VALUE;
    private Digraph G;
    private int[] ancestral_paths_lengths;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
        this.G = new Digraph(G);
        ancestral_paths_lengths = new int[G.V()];
    }

    private void find_ancestral_paths_lengths(int v, int w){
        for (int x = 0; x < G.V(); x++) ancestral_paths_lengths[x] = INFINITY;
        // debug_graph(G);
        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(G, w);
        //debug_BFS_path(bfs_v, v, 0);
        //debug_BFS_path(bfs_w, w, 0);
        for (int x = 0; x < G.V(); x++) {
            if(bfs_v.hasPathTo(x) && bfs_w.hasPathTo(x)){
                ancestral_paths_lengths[x] = bfs_v.distTo(x) + bfs_w.distTo(x);
            }
        }
    }

    private void find_ancestral_paths_lengths(Iterable<Integer> v, Iterable<Integer> w){
        for (int x = 0; x < G.V(); x++) ancestral_paths_lengths[x] = INFINITY;
        BreadthFirstDirectedPaths bfs_v = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfs_w = new BreadthFirstDirectedPaths(G, w);
        for (int x = 0; x < G.V(); x++) {
            if(bfs_v.hasPathTo(x) && bfs_w.hasPathTo(x)){
                ancestral_paths_lengths[x] = bfs_v.distTo(x) + bfs_w.distTo(x);
            }
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        find_ancestral_paths_lengths(v, w);
        return getMinValue(ancestral_paths_lengths);

    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
        find_ancestral_paths_lengths(v, w);
        return getMinValueKey(ancestral_paths_lengths);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        find_ancestral_paths_lengths(v, w);
        return getMinValue(ancestral_paths_lengths);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        find_ancestral_paths_lengths(v, w);
        return getMinValueKey(ancestral_paths_lengths);
    }

    /***********************************************************************
     *  Helper Functions
     ***********************************************************************/
    // Method for getting the minimum value in an integer array or -1 if min is INFINITY
    private int getMinValue(int[] inputArray){
        int minValue = inputArray[0];
        for(int i=1;i<inputArray.length;i++){
            if(inputArray[i] < minValue){
                minValue = inputArray[i];
            }
        }
        return (minValue == INFINITY) ? -1 : minValue;
    }
    // Method for getting the key for the minimun value in the array, or -1 if min is INFINITY
    private int getMinValueKey(int[] inputArray){
        int minValue = inputArray[0];
        int minValueKey = 0;
        for(int i=1;i<inputArray.length;i++){
            if(inputArray[i] < minValue){
                minValue = inputArray[i];
                minValueKey = i;
            }
        }
        return (minValue == INFINITY) ? -1 : minValueKey;
    }

    /***********************************************************************
     *  Test
     ***********************************************************************/
    // do unit testing of this class
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}