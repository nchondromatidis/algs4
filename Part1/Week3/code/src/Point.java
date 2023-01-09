/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new bySlopeOrder();

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if (that.y == y && that.x == x) return Double.NEGATIVE_INFINITY;    // same point
        if (that.y == y) return 0;                                          // horizontal
        if (that.x == x) return Double.POSITIVE_INFINITY;                   // vertical
        return (double) (that.y - y) / (that.x - x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (y > that.y) return 1;
        if (y < that.y) return -1;
        if (y == that.y && x > that.x) return 1;
        if (y == that.y && x < that.x) return -1;
        return 0;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    private class bySlopeOrder implements Comparator<Point> {
        public int compare(Point pointA, Point pointB) {
            double slopeA = slopeTo(pointA);
            double slopeB = slopeTo(pointB);
            return Double.compare(slopeA, slopeB);
        }
    }

    // unit test for input10.txt
    public static void main(String[] args) {
        String filename = "collinear/input40.txt";
        In in = new In(filename);
        int N = in.readInt();
        Point[] points = new Point[N];
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
        StdDraw.show(0);
        //sort array natural order
        Arrays.sort(points);
        for (Point point:points) {
            StdOut.println(point);
        }
        StdOut.println("----------");
        //choose point to find slopes and sort
        Point selectedPoint = points[17];
        //print slopes
        for (int i=0; i<N; i++) {
            //StdOut.println( "point " + points[1] + " slope with point " + points[i] + " is " + points[1].slopeTo(points[i]));
            StdOut.println(selectedPoint + ";" + points[i] + ";" + selectedPoint.slopeTo(points[i]));
        }
        Arrays.sort(points, selectedPoint.SLOPE_ORDER);
        StdOut.println("----------");
        for (Point point:points) {
            StdOut.println(point);
        }

    }
}