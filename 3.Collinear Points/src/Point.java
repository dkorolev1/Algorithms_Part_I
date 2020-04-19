import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    // Initializes a new point.
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Draws this point to standard draw.
    public void draw() {
        StdDraw.point(x, y);
    }

    // Draws the line segment between this point and the specified point to standard draw.
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // Returns the slope between this point and the specified point.
    public double slopeTo(Point that) {
        if (x == that.x && y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }
        if (x == that.x) {
            return Double.POSITIVE_INFINITY;
        }
        if (y == that.y)
            return 0.0;

        int left = that.y - y;
        int right = that.x - x;
        double result = (double) left / right;
        return  result;
    }

    // Compares two points by y-coordinate, breaking ties by x-coordinate.
    public int compareTo(Point that) {
        if (that.x == x && that.y == y) {
            return  0;
        }
        if (y < that.y || (y == that.y && x < that.x)) {
            return -1;
        }
        return 1;
    }

    // Compares two points by the slope they make with this point.
    public Comparator<Point> slopeOrder() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                double slope1 = slopeTo(o1);
                double slope2 = slopeTo(o2);
                if (slope1 == slope2) {
                    return 0;
                } else if (slope1 < slope2) {
                    return -1;
                }
                return 1;
            }
        };
    }

    // A string representation of this point
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}