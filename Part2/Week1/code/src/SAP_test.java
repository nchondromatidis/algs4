import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SAP_test {
    @Test
    public void test_single_sources_digraph1_3_11() {
        In in = new In("../material/wordnet/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int v = 3;
        int w = 11;
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        assertEquals(4, length);
        assertEquals(1, ancestor);
    }

    @Test
    public void test_single_sources_digraph2_1_5() {
        In in = new In("../material/wordnet/digraph2.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int v = 1;
        int w = 5;
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        assertEquals(2, length);
        assertEquals(0, ancestor);
    }

    @Test
    public void test_iterable_sources_digraph1_3_8_9_11() {
        In in = new In("../material/wordnet/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        Queue<Integer> v = new Queue<Integer>();
        Queue<Integer> w = new Queue<Integer>();
        v.enqueue(3);
        v.enqueue(8);
        w.enqueue(9);
        w.enqueue(11);
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        assertEquals(3, length);
        assertEquals(1, ancestor);
    }

    @Test
    public void test_iterable_sources_digraph2_1_2_4_5() {
        In in = new In("../material/wordnet/digraph2.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        Queue<Integer> v = new Queue<Integer>();
        Queue<Integer> w = new Queue<Integer>();
        v.enqueue(1);
        v.enqueue(2);
        w.enqueue(4);
        w.enqueue(5);
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        assertEquals(2, length);
        assertEquals(0, ancestor);
    }

    /***********************************************************************
     *  Debug Functions
     ***********************************************************************/
    // prints the Graph
    private void debug_graph(Digraph G)
    {
        StdOut.println(G);
    }

    // prints the BFS paths from source s to target v
    private void debug_BFS_path(BreadthFirstDirectedPaths bfs, int s, int v)
    {
        for (int x : bfs.pathTo(v)) {
            if (x == s) StdOut.print(x);
            else StdOut.print("->" + x);
        }
        StdOut.println();
    }
}