import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
public class KdTree {

    private Node root = null;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("input null");
        }
        root = insert(root, p, new RectHV(0, 0, 1, 1), 0);
    }

    private Node insert(Node node, Point2D p, RectHV rect, int level) {
        if (node == null) {
            size++;
            return new Node(p, rect);
        }
        if (node.p.equals(p)) {
            return node;
        }
        if (level % 2 == 0) {
            if (Point2D.X_ORDER.compare(p, node.p) < 0) {
                node.lb = insert(node.lb, p, new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax()), level + 1);
            } else {
                node.rt = insert(node.rt, p, new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax()), level + 1);
            }
        } else {
            if (Point2D.Y_ORDER.compare(p, node.p) < 0) {
                node.lb = insert(node.lb, p, new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y()), level + 1);
            } else {
                node.rt = insert(node.rt, p, new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax()), level + 1);
            }
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("input null");
        }
        return contains(root, p, 0);
    }

    private boolean contains(Node node, Point2D p, int level) {
        if (node == null) {
            return false;
        }
        if (node.p.equals(p)) {
            return true;
        }
        if (level % 2 == 0) {
            if (Point2D.X_ORDER.compare(p, node.p) < 0) {
                return contains(node.lb, p, level + 1);
            } else {
                return contains(node.rt, p, level + 1);
            }
        } else {
            if (Point2D.Y_ORDER.compare(p, node.p) < 0) {
                return contains(node.lb, p, level + 1);
            } else {
                return contains(node.rt, p, level + 1);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, 0);
    }

    private void draw(Node node, int level) {
        if (node == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        StdDraw.setPenRadius();
        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        draw(node.lb, level + 1);
        draw(node.rt, level + 1);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("input null");
        }
        List<Point2D> result = new ArrayList<>();
        range(root, rect, result);
        return result;
    }

    private void range(Node node, RectHV rect, List<Point2D> list) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.p)) {
            list.add(node.p);
        }
        if (node.lb != null && rect.intersects(node.lb.rect)) {
            range(node.lb, rect, list);
        }
        if (node.rt != null && rect.intersects(node.rt.rect)) {
            range(node.rt, rect, list);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("input null");
        }
        return nearest(root, p).p;
    }

    private ResultType nearest(Node node, Point2D p) {
        if (node == null) {
            return new ResultType(null, Double.POSITIVE_INFINITY);
        }
        ResultType result = new ResultType(node.p, node.p.distanceSquaredTo(p));
        if (node.lb != null && result.min > node.lb.rect.distanceSquaredTo(p)) {
            ResultType left = nearest(node.lb, p);
            if (Double.compare(left.min, result.min) < 0) {
                result = left;
            }
        }
        if (node.rt != null && result.min > node.rt.rect.distanceSquaredTo(p)) {
            ResultType right = nearest(node.rt, p);
            if (Double.compare(right.min, result.min) < 0) {
                result = right;
            }
        }
        return result;
    }

    private static class ResultType {
        private Point2D p;
        private double min;

        public ResultType(Point2D p, double min) {
            this.p = p;
            this.min = min;
        }
    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    public static void main(String[] args) {

        String filename = "kdtree/circle10.txt";
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();
        StdDraw.show();
    }
}
