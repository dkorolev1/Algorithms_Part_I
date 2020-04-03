import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException("The size of the grid (n) and the number of experiments (t) should be more than 0");
        }

        int i = 0;
        double[] percolationThresholds = new double[t];

        while (i < t)
        {
            Percolation p = new Percolation(n);
            while (!p.percolates())
            {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                p.open(row, col);
            }
            percolationThresholds[i] = (double) p.numberOfOpenSites() / (n*n);
            i++;
        }

        mean = StdStats.mean(percolationThresholds);
        stddev = StdStats.stddev(percolationThresholds);

        confidenceLo = mean - (CONFIDENCE_95 * stddev) / Math.sqrt(t);
        confidenceHi = mean + (CONFIDENCE_95 * stddev) / Math.sqrt(t);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        // Stopwatch w = new Stopwatch();
        PercolationStats stats = new PercolationStats(n, t);
        // StdOut.printf("time = %s\n", w.elapsedTime());

        StdOut.printf("mean = %s\n", stats.mean());
        StdOut.printf("stddev = %s\n", stats.stddev());
        StdOut.printf("95%% confidence interval = [%s, %s]\n", stats.confidenceLo(), stats.confidenceHi());
    }
}