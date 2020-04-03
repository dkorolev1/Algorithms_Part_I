import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private int openSitesCount;
    private final boolean[] openSites;

    private final int ufVirtualTopSiteIndex;
    private final int ufVirtualBottomSiteIndex;

    private final int shadowUfVirtualTopSiteIndex;

    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF shadowUf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("The size of the grid should be more than 0");
        }

        this.n = n;
        int sitesCount = n*n;

        openSitesCount = 0;
        openSites = new boolean[sitesCount];

        ufVirtualTopSiteIndex = sitesCount;
        ufVirtualBottomSiteIndex = sitesCount + 1;
        uf = new WeightedQuickUnionUF(sitesCount + 2);

        shadowUfVirtualTopSiteIndex = sitesCount;
        shadowUf = new WeightedQuickUnionUF(sitesCount + 1);

        union(0, n - 1, ufVirtualTopSiteIndex, true);
        union(sitesCount - n, sitesCount - 1, ufVirtualBottomSiteIndex, false);
    }

    private void union(int indexFrom, int indexTo, int indexWith, boolean bothUfs)
    {
        int i = indexFrom;
        while (i <= indexTo) {
            if (bothUfs) {
                uf.union(i, indexWith);
                shadowUf.union(i, indexWith);
            } else {
                uf.union(i, indexWith);
            }
            i++;
        }
    }

    private void tryUnion(int row, int col, int indexWith, boolean bothUfs)
    {
        boolean inRange = inRange(row, col);
        if (inRange) {
            int index = toIndex(row, col);
            boolean open = openSites[index];
            if (open) {
                if (bothUfs) {
                    uf.union(index, indexWith);
                    shadowUf.union(index, indexWith);
                } else {
                    uf.union(index, indexWith);
                }
            }
        }
    }

    private int toIndex(int row, int col)
    {
        return ((row - 1) * n) + col - 1;
    }

    private boolean inRange(int row, int col)
    {
        return row >= 1 && row <= n
                && col >= 1 && col <= n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("Argument is out of range");
        }

        int index = toIndex(row, col);
        if (openSites[index]) {
            return;
        } else {
            openSites[index] = true;
            openSitesCount ++;
        }

        tryUnion(row - 1, col, index, true);
        tryUnion(row, col + 1, index, true);
        tryUnion(row + 1, col, index, true);
        tryUnion(row, col - 1, index, true);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("Argument is out of range");
        }
        int index = toIndex(row, col);
        return openSites[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("Argument is out of range");
        }
        int index = toIndex(row, col);
        return openSites[index] &&
                shadowUf.find(shadowUfVirtualTopSiteIndex) == shadowUf.find(index);
    }

    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return openSitesCount != 0 &&
                uf.find(ufVirtualBottomSiteIndex) == uf.find(ufVirtualTopSiteIndex);
    }

    // test client
    public static void main(String[] args)
    {
        Percolation p = new Percolation(3);

        p.open(1, 3);
        p.open(3, 3);
        p.open(2, 3);
        p.open(3, 1);
        p.open(2, 1);
        p.open(1, 1);

        System.out.println("percolates:" + p.percolates());
    }
}