/* *****************************************************************************
 *  Name: Yogesh Singh
 *  Date: 16/01/2020
 *  Description: A mutable data type that uses a 2d-tree to implement the same
 *               API (but replace PointSET with KdTree).
 * **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {

    private class Node {
        private final Point2D point;
        private RectHV rect;
        private int count;
        private final int level;
        private Node left;
        private Node right;

        public Node(Point2D point, int level) {
            this.point = point;
            this.rect = null;
            this.count = 1;
            this.level = level;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    // nearest neighbour global variables
    private double champion;
    private Point2D nearestPoint;

    // range search global variable
    private Bag<Point2D> points;

    // construct an empty set of points
    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.count;
    }

    private int compare(Point2D p, Node x) {
        if (isEven(x.level)) return Double.compare(p.x(), x.point.x());
        else return Double.compare(p.y(), x.point.y());
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, 0);
        if (root.rect == null) root.rect = new RectHV(0, 0, 1, 1);
    }

    private Node insert(Node x, Point2D p, int level) {
        if (x == null) return new Node(p, level);

        int cmp = compare(p, x);

        if (cmp < 0) {
            x.left = insert(x.left, p, level + 1);
            if (x.left.rect == null) x.left.rect = createRect(x, cmp);
        }
        else {
            if (!x.point.equals(p)) {
                x.right = insert(x.right, p, level + 1);
                if (x.right.rect == null) x.right.rect = createRect(x, cmp);
            }
        }

        x.count = 1 + size(x.left) + size(x.right);

        return x;
    }

    private RectHV createRect(Node x, int cmp) {
        if (cmp < 0) {
            if (isEven(x.level)) // left
                return new RectHV(x.rect.xmin(), x.rect.ymin(), x.point.x(), x.rect.ymax());

            else // bottom
                return new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.point.y());
        }
        else {
            if (isEven(x.level)) // right
                return new RectHV(x.point.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());

            else // top
                return new RectHV(x.rect.xmin(), x.point.y(), x.rect.xmax(), x.rect.ymax());

        }
    }

    private boolean isEven(int n) {
        return n % 2 == 0;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node x = root;
        while (x != null) {
            int cmp = compare(p, x);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else {
                if (p.compareTo(x.point) == 0) return true;
                else x = x.right;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node x) {
        if (x == null) return;
        draw(x.left);
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.03);
        x.point.draw();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);
        x.rect.draw();
        draw(x.right);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        points = new Bag<>();
        range(root, rect);
        return points;
    }

    private void range(Node x, RectHV rect) {
        if (x == null) return;
        if (x.rect.intersects(rect)) {
            if (rect.contains(x.point)) points.add(x.point);
            range(x.left, rect);
            range(x.right, rect);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        champion = Double.POSITIVE_INFINITY;
        nearestPoint = null;
        nearest(root, p);
        return nearestPoint;
    }

    private void nearest(Node x, Point2D p) {
        if (x == null) return;

        double distance = p.distanceSquaredTo(x.point);
        if (distance < champion) {
            champion = distance;
            nearestPoint = x.point;
        }

        int cmp = compare(p, x);

        if (cmp < 0) {
            nearest(x.left, p);
            if (x.right != null && champion > x.right.rect.distanceSquaredTo(p))
                nearest(x.right, p);
        }
        else {
            nearest(x.right, p);
            if (x.left != null && champion > x.left.rect.distanceSquaredTo(p))
                nearest(x.left, p);
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        // initialize the data structure with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();

        StdOut.println(kdtree.isEmpty());

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        StdOut.println(kdtree.size());

        kdtree.draw();

        Point2D query = new Point2D(0.2109, 0.65234375);
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.03);
        query.draw();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.03);
        kdtree.nearest(query).draw();


        // draw the rectangle
        RectHV rect = new RectHV(0.18, 0.56, 0.61, 0.69);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setPenRadius();
        rect.draw();

        // draw the range search results for kd-tree in blue
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        for (Point2D p : kdtree.range(rect))
            p.draw();
    }
}
