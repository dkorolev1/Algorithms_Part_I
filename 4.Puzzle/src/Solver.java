import java.util.ArrayDeque;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver
{
    private class SearchNode implements Comparable<SearchNode> {
        private final int moves;
        private final Board board;
        private final int priority;
        private final SearchNode prev;

        public SearchNode(Board board, int moves, SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            priority = moves + board.manhattan();
            prev = previousNode;
        }

        public int compareTo(SearchNode that) {
            return (this.priority - that.priority);
        }
    }

    private int moves;
    private boolean isSolvable;
    private ArrayDeque<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if (initial == null)
            throw new IllegalArgumentException("Initial board can't be null");

        MinPQ<SearchNode> mainQueue = new MinPQ<>();
        MinPQ<SearchNode> twinQueue = new MinPQ<>();

        mainQueue.insert(new SearchNode(initial, 0, null));
        twinQueue.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode lastNode = null;

        while (!mainQueue.isEmpty() || !twinQueue.isEmpty())
        {
            var current = mainQueue.min();
            var currentTwin = twinQueue.min();

            mainQueue.delMin();
            twinQueue.delMin();

            if (current.board.isGoal()) {
                isSolvable = true;
                lastNode = current;
                break;
            }
            if (currentTwin.board.isGoal()) {
                isSolvable = false;
                break;
            }
            for(var next : current.board.neighbors())
            {
                boolean addNeighbor = current.prev == null
                        || !current.prev.board.equals(next);
                if (addNeighbor) {
                    mainQueue.insert(new SearchNode(next, current.moves + 1, current));
                }
            }
            for(var next : currentTwin.board.neighbors())
            {
                boolean addNeighbor = currentTwin.prev == null
                        || !currentTwin.prev.board.equals(next);
                if (addNeighbor) {
                    twinQueue.insert(new SearchNode(next, currentTwin.moves + 1, currentTwin));
                }
            }
        }

        if (isSolvable)
        {
            moves = lastNode.moves;
            solution = new ArrayDeque<>();
            while (lastNode.prev != null)
            {
                solution.push(lastNode.board);
                lastNode = lastNode.prev;
            }
            solution.push(initial);
        } else {
            moves = -1;
            solution = null;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return isSolvable;
    }

    // min number of moves to solve initial board
    public int moves()
    {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution()
    {
        return solution;
    }

    // test client (see below)
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}