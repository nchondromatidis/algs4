import java.util.Comparator;

/**
 *Created by nikos on 5/4/2014.
 */
public class Solver {
    private boolean solvable = false;
    private int moves = -1;
    private Queue<Board> solution = new Queue<Board>();

    /**
     * Find a solution to the initial board (using the A* algorithm)
     * @param initial board
     */
    public Solver(Board initial) {
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial));
        //twin
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        pqTwin.insert(new SearchNode(initial.twin()));
        loop:
        while (1==1){
            SearchNode dequeued = pq.delMin();
            solution.enqueue(dequeued.board);
            if(dequeued.board.isGoal()) {
                solvable = true;
                if(dequeued.previousSearchNode == null){
                    moves = 0;
                } else {
                    moves = dequeued.previousSearchNode.moves + 1;
                }

                break loop;
            }

            Iterable<Board> neighbors = dequeued.board.neighbors();
            for(Board neighbor : neighbors) {
                if(dequeued.previousSearchNode == null) {
                    pq.insert(new SearchNode(neighbor, 1 ,dequeued));
                    continue;
                }
                if(!neighbor.equals(dequeued.previousSearchNode.board)) pq.insert(new SearchNode(neighbor,dequeued.moves + 1,dequeued));
            }
            //twin code
            SearchNode dequeuedTwin = pqTwin.delMin();
            if(dequeuedTwin.board.isGoal()) {
                solvable = false;
                moves = -1;
                break loop;
            }
            Iterable<Board> neighborsTwin = dequeuedTwin.board.neighbors();
            for(Board neighborTwin : neighborsTwin) {
                if(dequeuedTwin.previousSearchNode == null) {
                    pqTwin.insert(new SearchNode(neighborTwin, 1 ,dequeuedTwin));
                    continue;
                }
                if(!neighborTwin.equals(dequeuedTwin.previousSearchNode.board)) pqTwin.insert(new SearchNode(neighborTwin,dequeuedTwin.moves + 1,dequeuedTwin));
            }
        }
    }

    /**
     * Is the initial board solvable?
     * @return true if yes, else false
     */
    public boolean isSolvable() {
        return solvable;
    }

    /**
     * Min number of moves to solve initial board; -1 if no solution
     * @return min number of moves to solve initial board; -1 if no solution
     */
    public int moves() {
        return moves;
    }

    /**
     * Sequence of boards in a shortest solution; null if no solution
     * @return
     */
    public Iterable<Board> solution() {
        if (solvable == false) return null;
        return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode previousSearchNode;
        private int priority;

        public SearchNode(Board initial) {
            board = initial;
            moves = 0;
            previousSearchNode = null;
            priority = board.manhattan() + moves;
        }

        public SearchNode(Board initial, int moves, SearchNode previousSearchNode) {
            board = initial;
            this.moves = moves;
            this.previousSearchNode = previousSearchNode;
            priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            int diff = this.priority - that.priority;
            if( diff > 0 ) return 1;
            if( diff < 0 ) return -1;
            return 0;
        }
    }

    /**
     * Solve a slider puzzle
     * @param args
     */
    public static void main(String[] args) {
        // create initial board from file
        //In in = new In(args[0]);
        In in = new In("8puzzle-testing/puzzle30.txt");
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        //print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}