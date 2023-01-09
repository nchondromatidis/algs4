import org.junit.Test;

/**
 * Created by nikos on 7/4/2014.
 */
public class SolverTest {

    @Test
    public void multipleSolverObjectsTest() {
        Solver solver1 = createSolverFromFile("8puzzle-testing/puzzle04.txt");
        Solver solver2 = createSolverFromFile("8puzzle-testing/puzzle00.txt");
        System.out.println("puzzle04.txt" + ": " + solver1.moves());
        System.out.println("puzzle00.txt" + ": " + solver2.moves());
    }

   private Solver createSolverFromFile(String filename) {
       // read in the board specified in the filename
       In in = new In(filename);
       int N = in.readInt();
       int[][] tiles = new int[N][N];
       for (int i = 0; i < N; i++) {
           for (int j = 0; j < N; j++) {
               tiles[i][j] = in.readInt();
           }
       }
       // solve the slider puzzle
       Board initial = new Board(tiles);
       Solver solver = new Solver(initial);
       return solver;
   }
}
