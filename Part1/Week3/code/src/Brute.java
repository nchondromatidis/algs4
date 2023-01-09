import java.util.Arrays;

/**
 * Created by nikos on 3/6/14.
 */
public class Brute {
    public static void main(String[] args) {
        // read in the input
        String filename = args[0];
        //String filename = "collinear/input10.txt";
        In in = new In(filename);
        int N = in.readInt();
        Point points[] = new Point[N];
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        //create the point objects
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            points[i].draw();
        }
        //sort point in their natural order
        Arrays.sort(points);
        //Brute force algorithm to find 4 collinear points
        for (int p = 0; p < N; p++) {
            for (int q = p+1; q < N; q++) {
                for (int r = q+1; r < N; r++) {
                    for (int s = r+1; s < N; s++) {
                        double pqSlope = points[p].slopeTo(points[q]);
                        double prSlope = points[p].slopeTo(points[r]);
                        double psSlope = points[p].slopeTo(points[s]);
                        if ( pqSlope == prSlope && pqSlope == psSlope && prSlope==psSlope ) {
                            StdOut.println(points[p]+" -> "+points[q]+" -> "+points[r]+" -> "+points[s]);
                            points[p].drawTo(points[s]);
                        }
                    }
                }
            }
            StdDraw.show(0);
        }
    }
}