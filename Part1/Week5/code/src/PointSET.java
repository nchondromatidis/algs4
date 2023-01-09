/**
 * Created by nikos on 12/4/2014.
 */
public class PointSET {
    private SET<Point2D> points;
    /**
     * Construct an empty set of points
     */
    public PointSET() {
        points = new SET<Point2D>();
    }

    /**
     * Is the set empty?
     * @return
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * Returns the number of points in the set.
     * @return the number of points in the set
     */
    public int size() {
        return points.size();
    }

    /**
     * Adds the point p to the set (if it is not already in the set)
     * @param p point to be inserted in set
     */
    public void insert(Point2D p) {
        points.add(p);
    }

    /**
     * Does the set contain the point p?
     * @param p
     * @return
     */
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    /**
     * Draws all of the points to standard draw
     */
    public void draw() {
        for(Point2D point : points) {
            point.draw();
        }
    }

    /**
     * Returns all points in the set that are inside the rectangle.
     * @param rect
     * @return all points in the set that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> pointsContained = new Queue<Point2D>();
        for(Point2D point : points) {
            if(rect.contains(point)) pointsContained.enqueue(point);
        }
        return  pointsContained;
    }

    /**
     * Returns a nearest neighbor in the set to p; null if set is empty;
     * @param p point to find its nearest neighbor
     * @return  nearest neighbor in the set to p; null if set is empty;
     */
    public Point2D nearest(Point2D p) {
        Point2D nearestNeighbor = null;
        double minDistance = 1;
        for(Point2D point : points) {
            if(minDistance > p.distanceSquaredTo(point)) {
                minDistance = p.distanceSquaredTo(point);
                nearestNeighbor = point;
            }
        }
        return  nearestNeighbor;
    }
}