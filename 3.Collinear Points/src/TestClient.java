public class TestClient {
    public static void main(String[] args) {
        Point[] points = new Point[] {
                new Point(1,1),
                new Point(3,3),
                new Point(4,4),
                new Point(7,7),
                new Point(9,9),
                new Point(2,1),
                new Point(5,1),
                new Point(6,1),
                new Point(8,1),
                new Point(9,1),
                new Point(9,3),
                new Point(9,4),
                new Point(9,5),
                new Point(9,7),
                new Point(1,3),
                new Point(1,5),
                new Point(1,6),
                new Point(1,8),
                new Point(3,8),
                new Point(4,8),
                new Point(6,8),
                new Point(7,8)
        };

        FastCollinearPoints cp = new FastCollinearPoints(points);
        LineSegment[] segments = cp.segments();

        System.out.println("Finish");
    }
}
