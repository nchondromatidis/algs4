import org.junit.Assert;
import org.junit.Test;

/**
 * Created by nikos on 12/4/2014.
 */
public class PointSetTest {

    @Test
    public void test() {
        PointSET pointSet = new PointSET();
        pointSet.draw();
        Point2D point1 = new Point2D(0.1, 0.9);
        Point2D point2 = new Point2D(0.2, 0.3);
        Point2D point3 = new Point2D(0.4, 0.5);
        Point2D point4 = new Point2D(0.6, 0.8);
        Point2D point5 = new Point2D(0.2, 0.2);
        pointSet.insert(point1);
        pointSet.insert(point2);
        pointSet.insert(point3);
        pointSet.insert(point4);
        pointSet.insert(point5);

        org.junit.Assert.assertEquals("contains-true", true, pointSet.contains(new Point2D(0.1, 0.9)));
        org.junit.Assert.assertEquals("contains-false", false, pointSet.contains(new Point2D(0.2,0.9)));

        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        pointSet.draw();
        RectHV rectangle = new RectHV(0.1, 0.1, 0.35, 0.35);
        rectangle.draw();
        StdDraw.show();

        Iterable<Point2D> range = pointSet.range(rectangle);
        for(Point2D p : range) {
            //StdOut.println(p);
        }

    }

    @Test
    public void rangeTest() {
        PointSET pointSet = new PointSET();
        Point2D point1 = new Point2D(0.7, 0.2);
        Point2D point2 = new Point2D(0.5, 0.4);
        Point2D point3 = new Point2D(0.2, 0.3);
        Point2D point4 = new Point2D(0.4, 0.7);
        Point2D point5 = new Point2D(0.9, 0.6);
        pointSet.insert(point1);
        pointSet.insert(point2);
        pointSet.insert(point3);
        pointSet.insert(point4);
        pointSet.insert(point5);
        RectHV rect = new RectHV(0.1, 0.2, 0.5, 0.6);
        Iterable<Point2D> result = pointSet.range(rect);
        for (Point2D p : result) {
            //StdOut.println(p);
        }
    }


    @Test
    public void nearestTest() {
        PointSET pointSet = new PointSET();
        Point2D point1 = new Point2D(0.7, 0.2);
        Point2D point2 = new Point2D(0.5, 0.4);
        Point2D point3 = new Point2D(0.2, 0.3);
        Point2D point4 = new Point2D(0.4, 0.7);
        Point2D point5 = new Point2D(0.9, 0.6);
        pointSet.insert(point1);
        pointSet.insert(point2);
        pointSet.insert(point3);
        pointSet.insert(point4);
        pointSet.insert(point5);
        Point2D nearest = pointSet.nearest(new Point2D(0.42, 0.74));
        StdOut.println(nearest);
    }


}
