/**
 * Percolation system model.
 * Created by nikos on 2/7/14.
 */
public class Percolation {
    private int N;                              // grid size
    private boolean[][] grid;                       // grid
    private WeightedQuickUnionUF wqu2vn;       // wqu2vn data structure plus top and bottom virtual nodes
    private WeightedQuickUnionUF wqu1vn;       // wqu2vn data structure plus top virtual node

    /**
     * Creates the NxN grid used for the percolation. Creates WeightedQuickUnion algorithm.
     * Create one virtual top and one virtual bottom site and connects top and bottom row respectively.
     * @param N the size of the NxN grid (percolation system)
     */
    public Percolation(int N) {
        this.N = N;
        grid = new boolean[N+1][N+1];
        wqu2vn = new WeightedQuickUnionUF((N*N)+2);
        wqu1vn = new WeightedQuickUnionUF((N*N)+1);
    }

    /**
     * Receives a site using its indices (i, j). Validates the indices of the site that it receives.
     * Marks the site as open (false=blocked, true=open). Links the site in question to its open neighbors.
     * @param i row grid index
     * @param j column grid index
     * @throws java.lang.IndexOutOfBoundsException unless both 1 < = i <  N and 1 < = j <  N
     */
    public void open(int i, int j) {
        checkGridIndices(i, j);
        grid[i][j] = true;
        //check if site is on top row and connect to virtual top
        if (i == 1) {
            wqu2vn.union(0, xyto1D(i, j));
            wqu1vn.union(0, xyto1D(i, j));
        }
        //check if site is on bottom row and connect to virtual bottom
        if (i == N) {
            wqu2vn.union((N*N)+1, xyto1D(i, j));
        }
        //check top open neighbor
        if ((i-1) >= 1) {
            if (grid[i - 1][j]) {
                wqu2vn.union(xyto1D(i, j), xyto1D(i-1, j));
                wqu1vn.union(xyto1D(i, j), xyto1D(i-1, j));
            }
        }
        //check bottom open neighbor
        if ((i+1) <= N) {
            if (grid[i + 1][j]) {
                wqu2vn.union(xyto1D(i, j), xyto1D(i+1, j));
                wqu1vn.union(xyto1D(i, j), xyto1D(i+1, j));
            }
        }
        //check left open neighbor
        if ((j-1) >= 1) {
            if (grid[i][j - 1]) {
                wqu2vn.union(xyto1D(i, j), xyto1D(i, j-1));
                wqu1vn.union(xyto1D(i, j), xyto1D(i, j-1));
            }
        }
        //check right open neighbor
        if ((j+1) <= N) {
            if (grid[i][j + 1]) {
                wqu2vn.union(xyto1D(i, j), xyto1D(i, j+1));
                wqu1vn.union(xyto1D(i, j), xyto1D(i, j+1));
            }
        }
    }

    /**
     * Checks weather site (row i, column j) is open
     * @param i row grid index
     * @param j column grid index
     * @return true is open, false if blocked
     */
    public boolean isOpen(int i, int j) {
        checkGridIndices(i, j);
        return grid[i][j] == true;
    }

    /**
     * Checks weather the site (row i, column j) is full. Full site is an open site that can be
     * connected to an open site in the top row via a chain of neighboring (left, right, up, down)
     * @param i row grid index
     * @param j column grid index
     * @return true if open, false if is not
     */
    public boolean isFull(int i, int j) {
        checkGridIndices(i, j);
        return (isOpen(i, j) && wqu1vn.connected(0, xyto1D(i, j)));

    }

    /**
     * Checks weather the percolation system percolates. A system percolates if we fill all open sites
     * connected to the top row and that process fills some open site in the bottom row.
     * @return true is it percolates, false if it does not
     */
    public boolean percolates() {
        return wqu2vn.connected(0, (N*N)+2-1);
    }

    /**
     * Checks if grid indices are positive numbers or less than grids dimensions.
     * @param i row grid index
     * @param j column grid index
     * @throws java.lang.IndexOutOfBoundsException unless both 1 < = i <  N and 1 < = j <  N
     */
    private void checkGridIndices(int i, int j) {
        if (i < 1 || i > N) throw new IndexOutOfBoundsException("i must be between 1 and "+N);
        if (j < 1 || j > N) throw new IndexOutOfBoundsException("j must be between 1 and "+N);
    }

    /**
     *  Transform 2D indices to 1D index.
     * @param i row grid index
     * @param j column grid index
     * @throws java.lang.IndexOutOfBoundsException unless both 1 < = i <  N and 1   <  = j <  N
     * @return the 1D index
     */
    private int xyto1D(int i, int j) {
        checkGridIndices(i, j);
        return (i-1)*N + j;
    }

    /**
     * Test the client with a simple 4x4 grid
     * @param args commandline argument grid size
     */
    public static void main(String[] args) {
        int N = StdIn.readInt();
        Percolation percolation = new Percolation(N);
        percolation.open(1, 1);
        percolation.open(1, 2);
        percolation.open(2, 2);
        percolation.open(3, 2);
        StdOut.print(percolation.percolates());
        percolation.open(4, 3);
        StdOut.print(percolation.percolates());
        percolation.open(4, 2);
        StdOut.print(percolation.percolates());
    }
}