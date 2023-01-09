import junit.framework.Assert;
import org.junit.Test;

import java.security.Principal;

/**
 * Created by nikos on 12/4/2014.
 */
public class KdTreeTest {

    @Test
    public void insertTest() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.5, 0.5);
        Point2D point2 = new Point2D(0.3, 0.3);
        Point2D point3 = new Point2D(0.7, 0.5);
        Point2D point4 = new Point2D(0.4, 0.2);
        Point2D point5 = new Point2D(0.4, 0.6);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
    }

    @Test
    public void containsTest() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.5, 0.5);
        Point2D point2 = new Point2D(0.3, 0.3);
        Point2D point3 = new Point2D(0.7, 0.5);
        Point2D point4 = new Point2D(0.4, 0.2);
        Point2D point5 = new Point2D(0.4, 0.6);
        Point2D point6 = new Point2D(0.9, 0.9);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        org.junit.Assert.assertEquals("containsTest 1", true, kdtree.contains(point1));
        org.junit.Assert.assertEquals("containsTest 2", true, kdtree.contains(point4));
        org.junit.Assert.assertEquals("containsTest 3", false, kdtree.contains(point6));
    }


    @Test
    public void containsTest2() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.5, 0.5);
        Point2D point2 = new Point2D(0.5, 0.5);
        Point2D point3 = new Point2D(0.7, 0.5);
        Point2D point4 = new Point2D(0.4, 0.2);
        Point2D point5 = new Point2D(0.4, 0.2);
        Point2D point6 = new Point2D(0.9, 0.9);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        kdtree.insert(point6);
        org.junit.Assert.assertEquals("containsTest2 1", true, kdtree.contains(point1));
        org.junit.Assert.assertEquals("containsTest2 2", true, kdtree.contains(point2));
        org.junit.Assert.assertEquals("containsTest2 3", true, kdtree.contains(point3));
        org.junit.Assert.assertEquals("containsTest2 4", true, kdtree.contains(point4));
        org.junit.Assert.assertEquals("containsTest2 5", true, kdtree.contains(point5));
        org.junit.Assert.assertEquals("containsTest2 6", true, kdtree.contains(point6));
    }

    @Test
    public void containsTest3() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.5, 0.5);
        Point2D point2 = new Point2D(0.3, 0.3);
        Point2D point3 = new Point2D(0.7, 0.5);
        Point2D point4 = new Point2D(0.4, 0.2);
        Point2D point5 = new Point2D(0.4, 0.6);
        Point2D point6 = new Point2D(0.9, 0.9);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        org.junit.Assert.assertEquals("containsTest 1", true, kdtree.contains(point1));
        org.junit.Assert.assertEquals("containsTest 2", true, kdtree.contains(point4));
        org.junit.Assert.assertEquals("containsTest 3", false, kdtree.contains(point6));
    }

    @Test
    public void containsTestRandom100K() {
        Queue<Point2D> queue = new Queue<Point2D>();
        KdTree kdtree = new KdTree();
        for (int i = 0; i < 100000; i++) {
            queue.enqueue(new Point2D(StdRandom.uniform(),StdRandom.uniform()));
        }
        int i = 0;
        for(Point2D p : queue) {
            Point2D dequeuedPoint = queue.dequeue();
            kdtree.insert(dequeuedPoint);
            org.junit.Assert.assertEquals("containsTestRandom1K "+i, true, kdtree.contains(dequeuedPoint));
            i++;
        }
    }
    @Test
    public void containsTest4() {
        Point2D point1 = new Point2D(0, 0);
        Point2D point2 = new Point2D(0, 1);
        Point2D point3 = new Point2D(1, 1);
        Point2D point4 = new Point2D(0.4, 0.3);
        Point2D point5 = new Point2D(0.5, 0.3);
        Point2D point6 = new Point2D(0.6, 0.3);
        KdTree kdTree = new KdTree();

        kdTree.insert(point1);   // Not in range.
        kdTree.insert(point2);   // Not in range.
        kdTree.insert(point3);   // Not in range.

        kdTree.insert(point4); // In range.
        kdTree.insert(point5); // In range.
        kdTree.insert(point6); // In range.

        org.junit.Assert.assertEquals("containsTest4 1", true, kdTree.contains(point1));
        org.junit.Assert.assertEquals("containsTest4 2", true, kdTree.contains(point2));
        org.junit.Assert.assertEquals("containsTest4 3", true, kdTree.contains(point3));
        org.junit.Assert.assertEquals("containsTest4 4", true, kdTree.contains(point4));
        org.junit.Assert.assertEquals("containsTest4 5", true, kdTree.contains(point5));
        org.junit.Assert.assertEquals("containsTest4 6", true, kdTree.contains(point6));
    }

    @Test
    public void sizeTest() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.5, 0.5);
        Point2D point2 = new Point2D(0.3, 0.3);
        Point2D point3 = new Point2D(0.7, 0.5);
        Point2D point4 = new Point2D(0.4, 0.2);
        Point2D point5 = new Point2D(0.4, 0.6);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        org.junit.Assert.assertEquals("sizeTest", 5, kdtree.size());
    }

    @Test
    public void sizeTest2() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.5, 0.5);
        Point2D point2 = new Point2D(0.5, 0.5);
        Point2D point3 = new Point2D(0.3, 0.3);
        Point2D point4 = new Point2D(0.4, 0.2);
        Point2D point5 = new Point2D(0.4, 0.6);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        org.junit.Assert.assertEquals("sizeTest", 4, kdtree.size());
    }

    @Test
    public void rectangleTest() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.7, 0.2);
        Point2D point2 = new Point2D(0.5, 0.4);
        Point2D point3 = new Point2D(0.2, 0.3);
        Point2D point4 = new Point2D(0.4, 0.7);
        Point2D point5 = new Point2D(0.9, 0.6);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        org.junit.Assert.assertEquals("sizeTest", 5, kdtree.size());
    }

    @Test
    public void drawTest() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.7, 0.2);
        Point2D point2 = new Point2D(0.5, 0.4);
        Point2D point3 = new Point2D(0.2, 0.3);
        Point2D point4 = new Point2D(0.4, 0.7);
        Point2D point5 = new Point2D(0.9, 0.6);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        //kdtree.draw();
        //StdDraw.show();
        //StdOut.println("check visually draw");
    }

    @Test
    public void rangeTest() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.7, 0.2);
        Point2D point2 = new Point2D(0.5, 0.4);
        Point2D point3 = new Point2D(0.2, 0.3);
        Point2D point4 = new Point2D(0.4, 0.7);
        Point2D point5 = new Point2D(0.9, 0.6);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        RectHV rect = new RectHV(0.1, 0.2, 0.5, 0.6);
        Iterable<Point2D> result = kdtree.range(rect);
        for (Point2D p : result) {
            //StdOut.println(p);
        }
    }

    @Test
    public void nearestTest() {
        KdTree kdtree = new KdTree();
        Point2D point1 = new Point2D(0.7, 0.2);
        Point2D point2 = new Point2D(0.5, 0.4);
        Point2D point3 = new Point2D(0.2, 0.3);
        Point2D point4 = new Point2D(0.4, 0.7);
        Point2D point5 = new Point2D(0.9, 0.6);
        kdtree.insert(point1);
        kdtree.insert(point2);
        kdtree.insert(point3);
        kdtree.insert(point4);
        kdtree.insert(point5);
        Point2D referencePoint = new Point2D(0.42, 0.74);
        Point2D nearest = kdtree.nearest(referencePoint);
        //StdOut.println(nearest);
    }


    @Test
    public void circle10Test() {
        KdTree kdtree = new KdTree();
        String filename = "./kdtree/circle10.txt";
        In in = new In(filename);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        Point2D point1 = new Point2D(0.81, 0.3);
        Point2D point2 = new Point2D(0.206107, 0.095492);

        org.junit.Assert.assertEquals("circle10Test size", 10, kdtree.size());
        org.junit.Assert.assertEquals("circle10Test contains false", false, kdtree.contains(point1));
        org.junit.Assert.assertEquals("circle10Test2 contains true", true, kdtree.contains(point2));
        org.junit.Assert.assertEquals("circle10Test2", point2, kdtree.nearest(point2));
    }
}
