import java.util.ArrayList;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;

public class PointSET {
    private final SET<Point2D> points;

    // construct an empty set of points
    public PointSET()
    {
        points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty()
    {
        return points.size() == 0;
    }

    // number of points in the set
    public int size()
    {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("Argument can't be null");
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p)
    {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw()
    {
        for (Point2D p : points)
        {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)
            throw new IllegalArgumentException("Argument can't be null");
        ArrayList<Point2D> insidePoint = new ArrayList<>();
        for (Point2D p : points)
        {
            if (rect.contains(p))
                insidePoint.add(p);
        }
        return insidePoint;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("Argument can't be null");

        Point2D nearestPoint = null;
        for (Point2D point : points)
        {
            if (nearestPoint == null)
            {
                nearestPoint = point;
                continue;
            }

            if (point.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p))
                nearestPoint = point;
        }
        return nearestPoint;
    }

    public static void main(String[] args)
    {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0.1, 0.1));
        ps.insert(new Point2D(0.1, 0.3));
        ps.insert(new Point2D(0.2, 0.3));
        ps.insert(new Point2D(0.5, 0.6));

        var rect = new RectHV(0.4, 0.4, 0.6, 0.8);
        Iterable<Point2D> pointsInRect = ps.range(rect);
        System.out.println("points in rect:" + pointsInRect);

        Point2D targetPoint = new Point2D(0.3, 0.2);
        Point2D nearestPoint = ps.nearest(targetPoint);
        System.out.println("nearest point:" + nearestPoint);

        System.out.println("Finish");
    }
}