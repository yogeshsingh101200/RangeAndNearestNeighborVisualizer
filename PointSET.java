/* *****************************************************************************
 *  Name: Yogesh Singh
 *  Date: 15/01/2020
 *  Description: A mutable data type that represents a set of points in the unit
 *               square
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class PointSET {

    private final SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Bag<Point2D> points = new Bag<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) points.add(p);
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double min = Double.POSITIVE_INFINITY;
        Point2D t = null;
        for (Point2D x : pointSet) {
            double distance = p.distanceSquaredTo(x);
            if (distance < min) {
                min = distance;
                t = x;
            }
        }
        return t;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // initialize the data structure with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();

        StdOut.println(brute.isEmpty());

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        StdOut.println(brute.size());

        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.03);
        brute.draw();

        Point2D query = new Point2D(0.2109, 0.65234375);
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.03);
        query.draw();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.03);
        brute.nearest(query).draw();


        // draw the rectangle
        RectHV rect = new RectHV(0.0, 0.56, 0.355, 0.994);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setPenRadius();
        rect.draw();

        // draw the range search results for kd-tree in blue
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        for (Point2D p : brute.range(rect))
            p.draw();
    }
}
