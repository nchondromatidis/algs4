import org.junit.Test;

import java.util.*;

/**
 * Created by nikos on 6/4/2014.
 */
public class BoardTest {

    @Test
    public void testHamming1(){
        int[][] blocks = new int[][]{
                { 8, 1, 3},
                { 4, 0, 2},
                { 7, 6, 5}
        };
        Board board = new Board(blocks);
        int hamming = board.hamming();
        org.junit.Assert.assertEquals("testHamming1 - wrong hamming number", 5, hamming);
    }

    @Test
    public void testHamming2(){
        int[][] blocks = new int[][]{
                { 2, 1, 3, 4 },
                { 5, 0, 7, 8 },
                { 9, 10, 12, 11 },
                { 13, 14, 6, 15}
        };
        Board board = new Board(blocks);
        int hamming = board.hamming();
        org.junit.Assert.assertEquals("testHamming2 - wrong hamming number", 6, hamming);
    }

    @Test
    public void testManhattan1(){
        int[][] blocks = new int[][]{
                { 8, 1, 3},
                { 4, 0, 2},
                { 7, 6, 5}
        };
        Board board = new Board(blocks);
        int manhattan = board.manhattan();
        org.junit.Assert.assertEquals("testManhattan1 - wrong manhattan number", 10, manhattan);
    }

    @Test
    public void testManhattan2(){
        int[][] blocks = new int[][]{
                { 5, 1, 8},
                { 2, 7, 3},
                { 4, 0, 6}
        };
        Board board = new Board(blocks);
        int manhattan = board.manhattan();
        org.junit.Assert.assertEquals("testManhattan2 - wrong manhattan number", 13, manhattan);
    }

    @Test
    public void testIsGoalFalse(){
        int[][] blocks = new int[][]{
                { 1, 2, 0},
                { 4, 5, 3},
                { 7, 8, 6}
        };
        Board board = new Board(blocks);
        boolean isGoal = board.isGoal();
        org.junit.Assert.assertEquals("testManhattan1 - wrong manhattan number", false, isGoal);
    }

    @Test
    public void testIsGoalTrue(){
        int[][] blocks = new int[][]{
                { 1, 2, 3},
                { 4, 5, 6},
                { 7, 8, 0}
        };
        Board board = new Board(blocks);
        boolean isGoal = board.isGoal();
        org.junit.Assert.assertEquals("testManhattan1 - wrong manhattan number", true, isGoal);
    }

    @Test
    public void testEqualsTrue() {
        int[][] blocks = new int[][]{
                { 1, 2, 3},
                { 4, 5, 6},
                { 7, 8, 0}
        };
        Board board1 = new Board(blocks);
        Board board2 = new Board(blocks);
        boolean equals = board1.equals(board2);
        org.junit.Assert.assertEquals("testEqualsTrue - error", true, equals);
    }

    @Test
    public void testEqualsFalse() {
        int[][] blocks1 = new int[][]{
                { 1, 2, 3},
                { 4, 5, 6},
                { 7, 8, 0}
        };
        int[][] blocks2 = new int[][]{
                { 1, 2, 3},
                { 4, 0, 6},
                { 7, 8, 5}
        };
        Board board1 = new Board(blocks1);
        Board board2 = new Board(blocks2);
        boolean equals = board1.equals(board2);
        org.junit.Assert.assertEquals("testEqualsFalse - error", false, equals);
    }

    @Test
    public void toStringTest() {
        int[][] blocks = new int[][]{
                { 8, 1, 3},
                { 4, 0, 2},
                { 7, 6, 5}
        };
        Board board = new Board(blocks);
        String expectedString = "3" + "\n" + " 8  1  3 " + "\n" + " 4  0  2 " + "\n" + " 7  6  5 " + "\n";
        org.junit.Assert.assertEquals("testEqualsFalse - error", expectedString, board.toString());
    }


    @Test
    public void twinTest2() {
        Board source = new Board(new int[][] {{1, 0}, {2, 3}});
        Board twin = source.twin();
        Board expected = new Board(new int[][] {{1, 0}, {3, 2}});
        org.junit.Assert.assertEquals("twinTest2 - error", twin, expected);
    }

    @Test
    public void twinTest3() {
        Board source = new Board(new int[][] {{0, 2}, {3, 1}});
        Board twin = source.twin();
        Board expected = new Board(new int[][] {{0, 2}, {1, 3}});
        org.junit.Assert.assertEquals("twinTest3 - error", twin, expected);
    }

    /***********************************************************************
     * Visual Assertion
     **********************************************************************/


    @Test
    public void iterableTest() {
        int[][] blocks = new int[][]{
                { 1, 8, 3},
                { 4, 2, 0},
                { 7, 6, 5}
        };
        Board board = new Board(blocks);
        Iterable<Board> neighbors = board.neighbors();
        for(Board b : neighbors){
            //StdOut.println(b.toString());
        }
        String expectedString1 = "3" + "\n" + " 1  0  3 " + "\n" + " 4  8  2 " + "\n" + " 7  6  5 " + "\n";
        String expectedString2 = "3" + "\n" + " 1  8  3 " + "\n" + " 4  6  2 " + "\n" + " 7  0  5 " + "\n";
        String expectedString3 = "3" + "\n" + " 1  8  3 " + "\n" + " 0  4  2 " + "\n" + " 7  6  5 " + "\n";
        String expectedString4 = "3" + "\n" + " 1  8  3 " + "\n" + " 4  2  0 " + "\n" + " 7  6  5 " + "\n";
    }

    @Test
    public void iterableTest2() {
        int[][] blocks = new int[][]{
                { 0, 1 },
                { 2, 3 }
        };
        Board board = new Board(blocks);
        Iterable<Board> neighbors = board.neighbors();
        for(Board b : neighbors){
            StdOut.println(b.toString());
        }
    }


}
