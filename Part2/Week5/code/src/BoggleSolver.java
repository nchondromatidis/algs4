import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BoggleSolver
{
    private BoggleBoard board;
    private final TrieST_EN<Integer> dictionary = new  TrieST_EN<Integer>();
    private Set<String> allValidWords;
    private List<Integer[]>[][] adj;
    private boolean[][] visited;
    private int W, H;


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)
    {
        for (String word:dictionary)
        {
            this.dictionary.put(word,calculate_word_score(word));
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        // initialize data structures
        allValidWords = new HashSet<String>();
        this.board = board;
        W = board.cols();
        H = board.rows();
        visited = new boolean[W][H];
        precompute_adj(W, H);
        for(int y=0; y < H; y++){
            for(int x=0; x < W; x++){
                dfs(x, y, "");
            }
        }
        return allValidWords;
    }

    /*****************************************************************************
     *  Helper Functions
     *****************************************************************************/
    private void dfs(int x, int y, String word)
    {
        visited[x][y] = true;
        word = word + board.getLetter(y,x);
        if (board.getLetter(y,x) == 'Q') word += "U";
        //StdOut.println(word);
        //print_visited();
        // MAIN OPTIMIZATION
        if(!dictionary.key_prefix_exists(word)) {
            visited[x][y] = false;
            return;
        }
        if(word.length()>=3 && dictionary.contains(word)) allValidWords.add(word);

        for (Integer[] adj_point : adj[x][y]) {
            if (!visited[adj_point[0]][adj_point[1]]) {
                dfs(adj_point[0], adj_point[1], word);
            }
        }
        visited[x][y] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
        if(!dictionary.contains(word)) return 0;
        return dictionary.get(word);
    }

    /*****************************************************************************
     *  Helper Functions
     *****************************************************************************/
    // Adds the adjacent points to a two dimensional array of linked lists. Uses integer arrays for points.
    private List<Integer[]>[][] precompute_adj(int W, int H)
    {
        adj = (LinkedList<Integer[]>[][]) new LinkedList[W][H];

        int adj_x, adj_y;
        Integer[] adj_point = new Integer[2];
        for (int y=0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                adj[x][y] = new LinkedList<Integer[]>();
                //right
                adj_x = x+1; adj_y = y;
                adj_point = new Integer[2];
                adj_point[0] = adj_x; adj_point[1] = adj_y;
                //if( (adj_x >= 0 && adj_x < W) && (adj_y >= 0 && adj_y < H) ) adj[x][y].add(board.getLetter(adj_y,adj_x));
                if( (adj_x >= 0 && adj_x < W) && (adj_y >= 0 && adj_y < H) ) adj[x][y].add(adj_point);
                // bottom-right
                adj_x = x+1; adj_y = y+1;
                adj_point = new Integer[2];
                adj_point[0] = adj_x; adj_point[1] = adj_y;
                if( (adj_x >= 0 && adj_x < W) && (adj_y >= 0 && adj_y < H) ) adj[x][y].add(adj_point);
                // bottom
                adj_x = x; adj_y = y+1;
                adj_point = new Integer[2];
                adj_point[0] = adj_x; adj_point[1] = adj_y;
                if( (adj_x >= 0 && adj_x < W) && (adj_y >= 0 && adj_y < H) ) adj[x][y].add(adj_point);
                //left-bottom
                adj_x = x-1; adj_y = y+1;
                adj_point = new Integer[2];
                adj_point[0] = adj_x; adj_point[1] = adj_y;
                if( (adj_x >= 0 && adj_x < W) && (adj_y >= 0 && adj_y < H) ) adj[x][y].add(adj_point);
                // left
                adj_x = x-1; adj_y = y;
                adj_point = new Integer[2];
                adj_point[0] = adj_x; adj_point[1] = adj_y;
                if( (adj_x >= 0 && adj_x < W) && (adj_y >= 0 && adj_y < H) ) adj[x][y].add(adj_point);
                // top-left
                adj_x = x-1; adj_y = y-1;
                adj_point = new Integer[2];
                adj_point[0] = adj_x; adj_point[1] = adj_y;
                if( (adj_x >= 0 && adj_x < W) && (adj_y >= 0 && adj_y < H) ) adj[x][y].add(adj_point);
                // top
                adj_x = x; adj_y = y-1;
                adj_point = new Integer[2];
                adj_point[0] = adj_x; adj_point[1] = adj_y;
                if( (adj_x >= 0 && adj_x < W) && (adj_y >= 0 && adj_y < H) ) adj[x][y].add(adj_point);
                // top-right
                adj_x = x + 1; adj_y = y-1;
                adj_point = new Integer[2];
                adj_point[0] = adj_x; adj_point[1] = adj_y;
                if( (adj_x >= 0 && adj_x < W) && (adj_y >= 0 && adj_y < H) ) adj[x][y].add(adj_point);
            }
        }
        return adj;
    }

    private int calculate_word_score(String word)
    {
        if(word.length() >= 0 && word.length() <= 2) return 0;
        if(word.length() >= 3 && word.length() <= 4) return 1;
        if(word.length() == 5) return 2;
        if(word.length() == 6) return 3;
        if(word.length() == 7) return 5;
        if(word.length() >= 8) return 11;
        return -1;
    }

    /*****************************************************************************
     *  Debug Functions
     *****************************************************************************/
    private void print_adj()
    {
        for (int y=0; y < adj[0].length; y++) {
            for (int x = 0; x < adj.length; x++) {
                StdOut.print(x + "," + y + " -> ");
                for (Integer[] adj_point:adj[x][y]){
                    StdOut.print( adj_point[0] + "," + adj_point[1] + " ");
                }
                StdOut.println();
            }
        }
    }

    private void print_visited()
    {
        String mark = "o";
        for (int y=0; y < visited[0].length; y++) {
            for (int x = 0; x < visited.length; x++) {
                if(visited[x][y]) mark = "x";
                StdOut.printf("[%s]", mark);
                mark = "o";
            }
            StdOut.println();
        }
        StdOut.println();
    }
}