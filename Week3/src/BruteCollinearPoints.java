import java.util.ArrayList;

public class BruteCollinearPoints {

    private final Point[] points;
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
            for (int i = 0; i < points.length - 3; i++) {
                for (int j = i + 1; j < points.length - 2; j++) {
                    for (int m = j + 1; m < points.length - 1; m++) {
                        if (Double.compare(points[m].slopeTo(points[j]), points[j].slopeTo(points[i])) != 0) {
                            continue;
                        }
                        for (int n = m + 1; n < points.length; n++) {
                            if (Double.compare(points[n].slopeTo(points[m]), points[m].slopeTo(points[j])) != 0) {
                                continue;
                            }
                            Point min = points[i];
                            Point max = points[i];
                            Point[] candidates = {points[j], points[m], points[n]};
                            for (Point can : candidates) {
                                if (can.compareTo(min) < 0) {
                                    min = can;
                                }
                                if (can.compareTo(max) > 0) {
                                    max = can;
                                }
                            }
                            list.add(new LineSegment(min, max));
                        }
                    }
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