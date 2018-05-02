import java.util.ArrayList;
import java.util.Arrays;

/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

public class FastCollinearPoints {
    // finds all line segments containing 4 or more points
    private final Point[] points;
    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("array null");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("point null");
            }
        }
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("point equal");
                }
            }
        }
        this.points = new Point[points.length];
        System.arraycopy(points, 0, this.points, 0, points.length);
    }

    // the number of line segments
    public int numberOfSegments() {
        if (lineSegments == null) {
            lineSegments = segments();
        }
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        if (lineSegments == null) {
            ArrayList<LineSegment> list = new ArrayList<>();
            for (int i = 0; i < points.length; i++) {
                Arrays.sort(points);
                Arrays.sort(points, points[i].slopeOrder());
                int min = 1;
                int max = 2;
                while (min < points.length) {
                    while (max < points.length && points[0].slopeOrder().compare(points[min], points[max]) == 0) {
                        max++;
                    }
                    if (max - min >= 3 && points[0].compareTo(points[min]) < 0) {
                        list.add(new LineSegment(points[0], points[max - 1]));
                    }
                    min = max;
                }

            }
            lineSegments = new LineSegment[list.size()];
            for (int i = 0; i < list.size(); i++) {
                lineSegments[i] = list.get(i);
            }
        }
        LineSegment[] result = new LineSegment[lineSegments.length];
        System.arraycopy(lineSegments, 0, result, 0, lineSegments.length);
        return result;
    }
}