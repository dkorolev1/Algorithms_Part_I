import java.awt.Color;
import java.util.ArrayList;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static class Node {
        private final Point2D p;
        // the axis-aligned rectangle corresponding to this node
        private final RectHV rect;
        // the left/bottom subtree
        private Node lb;
        // the right/top subtree
        private Node rt;
        public Node(Point2D p, RectHV rect)
        {
            this.p = p;
            this.rect = rect;
        }

        // splitting rectangle corresponding to this node
        public RectHV splittingRect(boolean odd)
        {
            double x0 = odd ? p.x() : rect.xmin();
            double y0 = odd ? rect.ymin() : p.y();
            double x1 = odd ? p.x() : rect.xmax();
            double y1 = odd ? rect.ymax() : p.y();
            return new RectHV(x0, y0, x1, y1);
        }
    }

    private int size;
    private Node head;

    // construct an empty set of points
    public KdTree()
    {
        size = 0;
        head = null;
    }

    // is the set empty?
    public boolean isEmpty()
    {
        return size == 0;
    }

    // number of points in the set
    public int size()
    {
        return size;
    }

    // need to move in lb direction
    private boolean lbDirection(double x0, double y0, double x1, double y1, boolean odd) {
        return odd ? x0 < x1 : y0 < y1;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("Argument can't be null");
        if (head == null || !contains(p)) {
            head = insert(head, null, p, true, false);
            size++;
        }
    }

    private Node insert(Node current, Node parent, Point2D p, boolean odd, boolean lb)
    {
        if (current == null) {
            if (parent == null) {
                return new Node(p, new RectHV(0, 0, 1, 1));
            }
            RectHV newRect = null;
            RectHV parentRect = parent.rect;
            if (odd) { // vertical
                newRect = lb
                        ? new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), parent.p.y())
                        : new RectHV(parentRect.xmin(), parent.p.y(), parentRect.xmax(), parentRect.ymax());
            }
            else { // horizontal
                newRect = lb
                        ? new RectHV(parentRect.xmin(), parentRect.ymin(), parent.p.x(), parentRect.ymax())
                        : new RectHV(parent.p.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
            }
            return new Node(p, newRect);
        }
        if (lbDirection(p.x(), p.y(), current.p.x(), current.p.y(), odd)) {
            current.lb = insert(current.lb, current, p, !odd, true);
        } else {
            current.rt = insert(current.rt, current, p, !odd, false);
        }
        return current;
    }

    // does the set contain point p?
    public boolean contains(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("Argument can't be null");
        return contains(head, p, true);
    }

    private boolean contains(Node node, Point2D searchPoint, boolean odd)
    {
        if (node == null)
            return false;
        if (node.p.equals(searchPoint))
            return true;
        if (lbDirection(searchPoint.x(), searchPoint.y(), node.p.x(), node.p.y(), odd))
            return contains(node.lb, searchPoint, !odd);
        return contains(node.rt, searchPoint, !odd);
    }

    // draw all points to standard draw
    public void draw()
    {
        draw(head, true);
    }

    private void draw(Node node, boolean odd)
    {
        if (node == null)
            return;
        draw(node.lb, !odd);
        StdDraw.setPenColor(odd ? Color.RED : Color.BLUE);
        node.splittingRect(odd).draw();
        draw(node.rt, !odd);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)
            throw new IllegalArgumentException("Argument can't be null");
        ArrayList<Point2D> points = new ArrayList<>();
        range(head, rect, points, true);
        return points;
    }

    private Iterable<Point2D> range(Node node, RectHV rect, ArrayList<Point2D> points, boolean odd)
    {
        if (node == null)
            return points;
        // current rect intersects the splitting rect of the node
        if (rect.intersects(node.splittingRect(odd))) {
            // search in both directions
            range(node.lb, rect, points, !odd);
            range(node.rt, rect, points, !odd);
            if (rect.contains(node.p)) {
                points.add(node.p);
            }
        } else {
            if (lbDirection(rect.xmax(), rect.ymax(), node.p.x(), node.p.y(), odd)) {
                range(node.lb, rect, points, !odd);
            } else {
                range(node.rt, rect, points, !odd);
            }
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException("Argument can't be null");
        if (size == 0)
            return null;
        return nearest(head, p, head.p, true);
    }

    private Point2D nearest(Node node, Point2D target, Point2D nearest, boolean odd)
    {
        if (node == null)
            return nearest;
        double nearestDistance = nearest.distanceSquaredTo(target);
        // the distance from the current point to the target is greater than from the nearest so far
        boolean cPointLess = nearestDistance <= node.p.distanceSquaredTo(target);
        // the distance from the current point rectangle to the target is greater than from the nearest so far
        boolean cPointRectLess = nearestDistance <= node.rect.distanceSquaredTo(target);
        if (cPointLess && cPointRectLess) {
            // skip this point and all sub-points
            return nearest;
        }
        if (node.p.distanceSquaredTo(target) < nearestDistance) {
            nearest = node.p;
        }
        boolean lbDirection = lbDirection(target.x(), target.y(), node.p.x(), node.p.y(), odd);
        nearest = nearest(lbDirection ? node.lb : node.rt, target, nearest, !odd);
        nearest = nearest(lbDirection ?  node.rt :  node.lb, target, nearest, !odd);
        return nearest;
    }

    public static void main(String[] args)
    {
        KdTree kt = new KdTree();

        kt.insert(new Point2D(0.7, 0.2));
        kt.insert(new Point2D(0.5, 0.4));
        kt.insert(new Point2D(0.2, 0.3));
        kt.insert(new Point2D(0.4, 0.7));
        kt.insert(new Point2D(0.9, 0.6));

        var contains = kt.contains(new Point2D(0.4, 0.75));
        var nearestPoint = kt.nearest(new Point2D(0.40625, 1.0));
        var pointsInRange = kt.range(new RectHV(0.3, 0.4, 1, 0.7));

        kt.draw();
        System.out.println("contains point:" + contains);
        System.out.println("nearest point:" + nearestPoint);
        System.out.println("points in range:" + pointsInRange);
    }
}