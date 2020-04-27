import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int[][] tiles;

    private int twinRow1;
    private int twinCol1;
    private int twinRow2;
    private int twinCol2;

    private int hamming;
    private int manhattan;

    private int blankSquareRow;
    private int blankSquareCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        int n = tiles.length;
        this.tiles = new int[n][n];

        hamming = 0;
        manhattan = 0;

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                int element = tiles[i][j];
                this.tiles[i][j] = element;
                if (element == 0)  {
                    blankSquareRow = i;
                    blankSquareCol = j;
                    continue;
                }
                int correctRow = element % n == 0 ? element/n - 1 : element/n;
                int correctCol = element - (correctRow * n) - 1;
                boolean wrongPosition = correctRow != i || correctCol != j;
                if (wrongPosition)  {
                    hamming++;
                    manhattan += Math.abs(correctRow - i) + Math.abs(correctCol - j);
                }
            }
        }

        do {
            twinRow1 = StdRandom.uniform(n);
            twinCol1 = StdRandom.uniform(n);
            twinRow2 = StdRandom.uniform(n);
            twinCol2 = StdRandom.uniform(n);
        } while (tiles[twinRow1][twinCol1] == 0
                || tiles[twinRow2][twinCol2] == 0
                || tiles[twinRow1][twinCol1] == tiles[twinRow2][twinCol2]);
    }

    // string representation of this board
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(tiles.length + "\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension()
    {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming()
    {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        return manhattan == 0 && hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        // self check
        if (this == y)
            return true;
        // null check
        if (y == null)
            return false;
        // type check and cast
        if (getClass() != y.getClass())
            return false;
        Board b = (Board) y;
        return Arrays.deepEquals(tiles, b.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        List<Board> neighbors = new ArrayList<>();
        if (blankSquareCol != 0)  {
            int[][] tilesNeighbor = copyAndSwipe(blankSquareRow, blankSquareCol, blankSquareRow, blankSquareCol - 1);
            neighbors.add(new Board(tilesNeighbor));
        }
        if (blankSquareRow != 0)  {
            int[][] tilesNeighbor = copyAndSwipe(blankSquareRow, blankSquareCol, blankSquareRow - 1, blankSquareCol);
            neighbors.add(new Board(tilesNeighbor));
        }
        if (blankSquareCol != tiles.length - 1)  {
            int[][] tilesNeighbor = copyAndSwipe(blankSquareRow, blankSquareCol, blankSquareRow, blankSquareCol + 1);
            neighbors.add(new Board(tilesNeighbor));
        }
        if (blankSquareRow != tiles.length - 1)  {
            int[][] tilesNeighbor = copyAndSwipe(blankSquareRow, blankSquareCol, blankSquareRow + 1, blankSquareCol);
            neighbors.add(new Board(tilesNeighbor));
        }
        return neighbors;
    }

    private int[][] copyAndSwipe(int row1, int col1, int row2, int col2)
    {
        int[][] tilesCopy = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles.length; j++)
                tilesCopy[i][j] = tiles[i][j];

        int buf = tilesCopy[row1][col1];
        tilesCopy[row1][col1] = tilesCopy[row2][col2];
        tilesCopy[row2][col2] = buf;
        return tilesCopy;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        return new Board(copyAndSwipe(twinRow1, twinCol1, twinRow2, twinCol2));
    }

    // unit testing (not graded)
    public static void main(String[] args)
    {
        int[][] twinsTest = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board b = new Board(twinsTest);
        System.out.println(b.neighbors().toString());
    }
}