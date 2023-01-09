/**
 * Created by nikos on 12/4/2014.
 */
public class KdTree {
    private Node root ;         //root node
    private int N;              // tree size

    /**
     * 2D tree helper node data type
     */
    private static class Node {
        private Point2D p;              // the point
        private RectHV rect;            // the axis-aligned rectangle corresponding to this node
        private Node lb;                // the left/bottom subtree
        private Node rt;                // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    /**
     * Is the tree empty?
     * @return true is empty, false if not
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Number of points in the tree
     * @return the number of points in the tree
     */
    public int size() {
        if (root == null) return 0;
        else return N;
    }

    /**
     * Insert the key-value pair; overwrite the old value with the new value
     * if the key is already present
     * @param p
     */
    public void insert(Point2D p) {
        RectHV initialRectangle = new RectHV(0,0,1,1);
        root = insert(root, p, initialRectangle, 1);
        N++;
    }

    private Node insert(Node node, Point2D p, RectHV rect, int treeLevel) {
        if (node == null) return new Node(p,rect);
        // change the compare result according to tree level (horizontal, vertical)
        int cmp;
        int treeLevelOddEven = treeLevel % 2;
        if (treeLevelOddEven != 0) cmp = Point2D.X_ORDER.compare(p, node.p);
        else                       cmp = Point2D.Y_ORDER.compare(p, node.p);
        if (treeLevelOddEven != 0 && cmp < 0) {
            //point left
            RectHV newRect = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
            node.lb  = insert(node.lb, p, newRect, ++treeLevel);
        } else if (treeLevelOddEven != 0 && cmp >= 0) {
            if (p.compareTo(node.p) == 0) {
                N--;
                return node;
            }
            //point right
            RectHV newRect = new RectHV(node.p.x(), rect.ymin(),  rect.xmax(), rect.ymax());
            node.rt  = insert(node.rt,  p, newRect, ++treeLevel);
        } else if (treeLevelOddEven == 0 && cmp >= 0) {
            //point top
            RectHV newRect = new RectHV(rect.xmin(), node.p.y(),  rect.xmax(), rect.ymax());
            node.rt  = insert(node.rt,  p, newRect, ++treeLevel);
        } else if (treeLevelOddEven == 0 && cmp < 0) {
            if (p.compareTo(node.p) == 0) {
                N--;
                return node;
            }
            //point bottom
            RectHV newRect = new RectHV(rect.xmin(), rect.ymin(),  rect.xmax(), node.p.y());
            node.lb  = insert(node.lb,  p, newRect, ++treeLevel);
        }
        return node;
    }

    /**
     * Does the set contain the point p?
     * @param p the point to be searched in the tree
     * @return true if p found in tree; false if not
     */
    public boolean contains(Point2D p) {
        Point2D pointReturned = get(root, p, 1);
        if (pointReturned == null) return false;
        return  pointReturned.compareTo(p) == 0;
    }

    private Point2D get(Node node, Point2D p, int treeLevel) {
        if (node == null) return null;
        // change the compare result according to tree level (horizontal, vertical)
        int cmp;
        if (treeLevel % 2 != 0) cmp = Point2D.X_ORDER.compare(p, node.p);
        else                    cmp = Point2D.Y_ORDER.compare(p, node.p);
        // place node according to tree level (horizontal, vertical)
        if      (cmp > 0) return get(node.rt,  p, ++treeLevel);
        else if (cmp < 0) return get(node.lb,  p, ++treeLevel);
        else {
            int cmp2= p.compareTo(node.p);
            if(cmp2 == 0) return node.p;
            if(cmp2 > 0) return get(node.rt,  p, ++treeLevel);
            if(cmp2 < 0) return get(node.lb,  p, ++treeLevel);
        }
        return node.p;
    }

    /**
     * Draw all of the points to standard draw
     */
    public void draw() {
        Iterable<Node> queue = levelOrder();
        for(Node node : queue) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            node.p.draw();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            node.rect.draw();
        }
    }

    // level order traversal
    private Iterable<Node> levelOrder() {
        Queue<Node> queue = new Queue<Node>();
        Queue<Node> keys = new Queue<Node>();
        queue.enqueue(root);
        //keys.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x);
            queue.enqueue(x.lb);
            queue.enqueue(x.rt);
        }
        return keys;
    }

    /**
     * Returns all points in the set that are inside a rectangle.
     * @param rect the rectangle.
     * @return all points in the set that are inside the rectangle.
     */
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> rangePoints = new Queue<Point2D>();
        range(root, rect, rangePoints);
        return rangePoints;
    }

    private Iterable<Point2D> range(Node node, RectHV rect, Queue<Point2D> rangePoints) {
        if (rect.contains(node.p)) rangePoints.enqueue(node.p);
        if (node.lb == null || node.rt == null) return rangePoints;
        if (rect.intersects(node.rt.rect) || rect.intersects(node.lb.rect)) {
            range(node.lb, rect, rangePoints);
            range(node.rt, rect, rangePoints);
        } else if (!rect.intersects(node.lb.rect)) {
            range(node.rt, rect, rangePoints);
        } else if (!rect.intersects(node.rt.rect)) {
            range(node.lb, rect, rangePoints);
        }
        return rangePoints;
    }

    /**
     * Returns a nearest neighbor in the set to p; null if set is empty
     * @param p reference point for the nearest neighbor
     * @return the point nearest to the reference point provided
     */
    public Point2D nearest(Point2D p) {
        Point2D champion = root.p;
        return nearest(root, p, champion, 1);
    }

    private Point2D nearest(Node node, Point2D qp, Point2D champion, int treeLevel) {
        if (node == null) return champion;
        double minDistance = champion.distanceSquaredTo(qp);
        double distance = node.p.distanceSquaredTo(qp);
        double nodeRectQpDist = node.rect.distanceSquaredTo(qp);
        boolean test = nodeRectQpDist !=0;
        if (nodeRectQpDist !=0 && nodeRectQpDist > minDistance) return champion;
        if (distance < minDistance) champion = node.p;
        boolean treeLevelOdd = (treeLevel % 2 != 0);
        if (treeLevelOdd) {
            if(qp.x() < node.p.x()) {
                champion = nearest(node.lb, qp, champion, ++treeLevel);
                champion = nearest(node.rt, qp, champion, ++treeLevel);
            } else {
                champion = nearest(node.rt, qp, champion, ++treeLevel);
                champion = nearest(node.lb, qp, champion, ++treeLevel);
            }
        } else {
            if(qp.y() < node.p.y()) {
                champion = nearest(node.lb, qp, champion, ++treeLevel);
                champion = nearest(node.rt, qp, champion, ++treeLevel);
            } else {
                champion = nearest(node.rt, qp, champion, ++treeLevel);
                champion = nearest(node.lb, qp, champion, ++treeLevel);
            }
        }
        return champion;
    }
}