/**
 * Created by nikos on 5/4/2014.
 */
import java.util.Arrays;


public class Board {
    private final int[][] board;
    private final int N;
    private int manhattan = -1;

    /**
     * Constructs a board from an N-by-N array of blocks
     * (where blocks[i][j] = block in row i, column j)
     * @param blocks
     */
    public Board(int[][] blocks) {
        N = blocks.length;
        board = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = blocks[i][j];
            }
        }
    }

    /**
     * Returns the board dimensions
     * @return the board dimensions
     */
    public int dimension() {
        return N;
    }

    /**
     * Hamming priority function
     * @return number of blocks out of place
     */
    public int hamming() {
        int hamming = 0;
        for(int i=0;i<N;i++){
            for(int j=0;j<N;j++){
                if(board[i][j] == 0) continue;
                if(board[i][j] != (i*N+j+1)) hamming++;
            }
        }
        return hamming;
    }

    /**
     * Manhattan priority function (cache manhattan)
     * @return sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {
        if(this.manhattan != - 1) return this.manhattan;
        int manhattan = 0;
        int xGoal;
        int yGoal;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) continue;
                if (board[i][j] <= N) {
                    xGoal = 0;
                    yGoal = board[i][j] - 1;
                } else {
                    if ((board[i][j] % N) == 0) {
                        xGoal = board[i][j] / N - 1;
                        yGoal = N - 1;
                    } else {
                        xGoal = board[i][j] / N;
                        yGoal = board[i][j] % N - 1;
                    }
                }
                manhattan = manhattan + Math.abs(i - xGoal) + Math.abs(j - yGoal);
            }
        }
        this.manhattan = manhattan;
        return this.manhattan;
    }

    /**
     * Is this board the goal board? + Optimization using cached manhattan
     * @return true if it is, false if not
     */
    public boolean isGoal() {
        if(manhattan != - 1) return manhattan() == 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 0) continue;
                if (board[i][j] != (i * N + j + 1)) return false;
            }
        }
        return true;
    }

    /**
     * Creates a board obtained by exchanging two adjacent blocks in the same row.
     * @return A board obtained by exchanging two adjacent blocks in the same row
     */
    public Board twin() {
        int[][] boardTwin = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                boardTwin[i][j] = board[i][j];
            }
        }
        int temp = boardTwin[0][0];
        loop:
        for(int i = 0 ; i < N ; i++) {
            for(int j = 0 ; j < N-1 ; j++) {
                if(boardTwin[i][j] != 0 && boardTwin[i][j+1] !=0){
                    int swap = boardTwin[i][j];
                    boardTwin[i][j] = boardTwin[i][j+1];
                    boardTwin[i][j+1] = swap;
                    break loop;
                }
            }
        }
        return new Board(boardTwin);
    }

    /**
     * Does this board equal y.
     * @param y object to be compared
     * @return true if object y is equals to this board, else false
     */
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (N!=that.N) return false;
        return Arrays.deepEquals(board, that.board);
    }

    /**
     *  Returns all neighboring boards.
     * @return all neighboring boards
     */
    public Iterable<Board> neighbors() {
        //find 0 position
        int i = 0;
        int j = 0;
        loop:
        for(i=0;i<N;i++) {
            for (j=0; j < N; j++) {
                if (board[i][j] == 0)  break loop;
            }
        }
        //find neighbors and enqueue them
        Queue<Board> q = new Queue<Board>();
        if(i > 0) q.enqueue(new Board(exch(i, j, i - 1, j)));
        if(i < N-1) q.enqueue(new Board(exch(i, j, i + 1, j)));
        if(j > 0) q.enqueue(new Board(exch(i, j, i , j - 1)));
        if(j < N-1) q.enqueue(new Board(exch(i, j, i , j + 1)));
        return q;
    }

    /**\
     * String representation of the board
     * @return
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /***********************************************************************
     * Helper functions
     **********************************************************************/
    private int[][] exch(int x0, int y0, int x1, int y1) {
        int[][] boardClone = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                boardClone[i][j] = board[i][j];
            }
        }
        int swap =  boardClone[x0][y0];
        boardClone[x0][y0] = boardClone[x1][y1];
        boardClone[x1][y1] = swap;
        return boardClone;
    }
}