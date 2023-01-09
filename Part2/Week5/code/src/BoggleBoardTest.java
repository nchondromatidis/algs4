import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BoggleBoardTest {


    @Test
    public void correctness_reference() {
        // create dictionary
        In in = new In("../material/boggle/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        // create the board

        Map<String,Integer> filenames = new HashMap<String,Integer>();

        filenames.put("board-points0", 0);
        filenames.put("board-points1", 1);
        filenames.put("board-points2", 2);
        filenames.put("board-points3", 3);
        filenames.put("board-points4", 4);
        filenames.put("board-points5", 5);
        filenames.put("board-points100", 100);
        filenames.put("board-points200", 200);
        filenames.put("board-points300", 300);
        filenames.put("board-points400", 400);
        filenames.put("board-points500", 500);
        filenames.put("board-points750", 750);
        filenames.put("board-points1000", 1000);
        filenames.put("board-points1250", 1250);
        filenames.put("board-points1500", 1500);
        filenames.put("board-points2000", 2000);
        filenames.put("board-points4410", 4410);
        filenames.put("board-points4527", 4527);
        filenames.put("board-points4540", 4540);
        filenames.put("board-points13464", 13464);
        filenames.put("board-points26539", 26539);

        String path = "../material/boggle/";
        String full_filename_path = "";
        int correct_score;
        for(String filename: filenames.keySet()){
            full_filename_path = path + filename +".txt";
            BoggleBoard board = new BoggleBoard(full_filename_path);
            solver.getAllValidWords(board);
            int score = 0;
            for(String word:solver.getAllValidWords(board)){
                score += solver.scoreOf(word);
            }
            correct_score = filenames.get(filename);
            assertEquals(correct_score, score);
        }

    }


    @Test
    public void getAllValidWords_algs4_board_q() {
        // create dictionary
        In in = new In("../material/boggle/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        // create the board
        String filename = "../material/boggle/board-points13464.txt";
        BoggleBoard board = new BoggleBoard(filename);
        // time
        long startTime = System.currentTimeMillis();
        solver.getAllValidWords(board);
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        StdOut.println("millis : " + duration);
        int score = 0;
        for(String word:solver.getAllValidWords(board)){
            score += solver.scoreOf(word);
        }
        StdOut.println("score  : " + score);
    }
}
