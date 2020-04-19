import java.util.List;
import java.util.ArrayList;

public class BruteCollinearPoints {
    private final List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument points can't be null.");
        }
        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                throw new IllegalArgumentException("Array points can't contain null value.");

        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Array points can't contain repeated points.");
                }
            }

        segments = new ArrayList<>();

        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                for (int k = j + 1; k < points.length; k++)
                    for (int p = k + 1; p < points.length; p++)
                    {
                        Point[] cPoints = {
                                points[i], points[j], points[k], points[p]
                        };
                        Point min = points[i];
                        Point max = points[p];
                        for (int c = 0; c < cPoints.length; c++)
                        {
                            if (cPoints[c].compareTo(min) < 0) {
                                min = cPoints[c];
                            }
                            if (cPoints[c].compareTo(max) > 0) {
                                max = cPoints[c];
                            }
                        }

                        double slop1 = points[i].slopeTo(points[j]);
                        double slop2 = points[i].slopeTo(points[k]);
                        double slop3 = points[i].slopeTo(points[p]);

                        boolean collinear = slop1 == slop2 && slop2 == slop3;

                        if (collinear) {
                            segments.add(new LineSegment(min, max));
                        }
                    }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] sArray = new LineSegment[segments.size()];
        return segments.toArray(sArray);
    }
}
