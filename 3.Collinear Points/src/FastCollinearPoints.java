import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class FastCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    public FastCollinearPoints(Point[] points)
    {
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

        for (int i = 0; i < points.length; i++)
        {
            Point consideringPoint = points[i];
            Point[] restPoints = new Point[points.length - 1];
            for (int j = 0; j < points.length; j++)
            {
                if (j < i) {
                    restPoints[j] = points[j];
                } else if (j > i) {
                    restPoints[j-1] = points[j];
                }
            }
            Arrays.sort(restPoints, consideringPoint.slopeOrder());

            int groupLength = 0;
            double groupSlope = 0;
            Point groupMinPoint = consideringPoint;
            Point groupMaxPoint = consideringPoint;

            for (int k = 0; k < restPoints.length; k++)
            {
                Point cPoint = restPoints[k];
                if (k == 0) {
                    groupSlope = consideringPoint.slopeTo(cPoint);
                }
                double currentSlope = consideringPoint.slopeTo(restPoints[k]);
                boolean breakGroup = currentSlope != groupSlope;
                // break the group
                if (breakGroup)
                {
                    if (groupLength >= 3) {
                        if (consideringPoint.compareTo(groupMinPoint) == 0)
                        {
                            segments.add(new LineSegment(groupMinPoint, groupMaxPoint));
                        }
                    }
                    // start new group from the current item
                    groupLength = 1;
                    groupSlope = currentSlope;
                    groupMinPoint = consideringPoint;
                    groupMaxPoint = consideringPoint;
                } else {
                    // add new item to the group
                    groupLength++;
                }

                if (cPoint.compareTo(groupMinPoint) < 0) {
                    groupMinPoint = cPoint;
                }
                if (cPoint.compareTo(groupMaxPoint) > 0) {
                    groupMaxPoint = cPoint;
                }

                if (groupLength >= 3 && k == restPoints.length - 1) {
                    if (consideringPoint.compareTo(groupMinPoint) == 0)
                    {
                        segments.add(new LineSegment(groupMinPoint, groupMaxPoint));
                    }
                }
            }
        }
    }

    public int numberOfSegments()
    {
        return segments.size();
    }

    public LineSegment[] segments()
    {
        return segments.toArray(new LineSegment[0]);
    }
}