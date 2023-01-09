import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BaseballElimination {

    private int N;                                      // number of teams
    private Map<String, Integer> team_name_to_index;    // team names to index hash map
    private String[] team_index_to_name;                // team index to name array
    private int[] w;                                    // team wins
    private int[] l;                                    // team losses
    private int[] r;                                    // team games left
    private int[][] g;                                  // games left with other teams

    private FlowNetwork flowNetwork;
    private int source_vertex;
    private int sink_vertex;
    private List<Integer> source_game_vertexes;
    private List<String> certificateOfElimination;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) throws FileNotFoundException
    {
        Scanner input = new Scanner(new File(filename));
        N = input.nextInt();
        team_name_to_index = new HashMap<String, Integer>(N);
        team_index_to_name = new String[N];
        certificateOfElimination = new ArrayList<String>();
        w = new int[N];
        l = new int[N];
        r = new int[N];
        g = new int[N][N];
        String team_name;
        for(int i=0;i<N;i++) {
            team_name = input.next();
            team_name_to_index.put(team_name, i);
            team_index_to_name[i] = team_name;
            w[i] = input.nextInt();
            l[i] = input.nextInt();
            r[i] = input.nextInt();
            for(int j=0;j<N;j++) {
                g[i][j] = input.nextInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() { return N; }

    // all teams
    public Iterable<String> teams() { return team_name_to_index.keySet(); }

    // number of wins for given team
    public int wins(String team) {
        try {
            return w[team_name_to_index.get(team)];
        } catch (NullPointerException name) {
            throw new IllegalArgumentException();
        }
    }

    // number of wins for given team
    public int losses(String team) {
        try {
            return l[team_name_to_index.get(team)];
        } catch (NullPointerException name) {
            throw new IllegalArgumentException();
        }
    }

    // number of remaining games for given team
    public int remaining(String team) {
        try {
            return r[team_name_to_index.get(team)];
        } catch (NullPointerException name) {
            throw new IllegalArgumentException();
        }
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        try {
            return g[team_name_to_index.get(team1)][team_name_to_index.get(team2)];
        } catch (NullPointerException name) {
            throw new IllegalArgumentException();
        }
    }

    // is given team eliminated?
    public boolean isEliminated(String team)
    {
        try {
            int team_index = team_name_to_index.get(team);
        } catch (NullPointerException name) {
            throw new IllegalArgumentException();
        }
        certificateOfElimination = new ArrayList<String>();
        if(check_for_trivial_elimination(team)) return true;
        return check_for_non_trivial_elimination(team);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        try {
            int team_index = team_name_to_index.get(team);
        } catch (NullPointerException name) {
            throw new IllegalArgumentException();
        }
        isEliminated(team);
        if(certificateOfElimination.isEmpty()) return null;
        return certificateOfElimination;
    }

    /*****************************************************************************
     *  Helper Functions
     *****************************************************************************/

    private int factorial(int N)
    {
        int factorial = 0;
        for(int i=1; i<N;i++){
            factorial += i;
        }
        return factorial;
    }

    private boolean check_for_trivial_elimination(String target_team)
    {
        int target_team_max_wins = w[team_name_to_index.get(target_team)] + r[team_name_to_index.get(target_team)];
        for(int team_index: team_name_to_index.values()){
            int test = w[team_index]+r[team_index];
            if((w[team_index])>target_team_max_wins) {
                certificateOfElimination.add(team_index_to_name[team_index]);
                return true;
            }
        }
        return false;
    }

    private boolean check_for_non_trivial_elimination(String team)
    {
        makeFlowNetwork(team_name_to_index.get(team));
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork,source_vertex,sink_vertex);
        //StdOut.println(flowNetwork);
        for(int team_vertex=0;team_vertex<N;team_vertex++){
            if( fordFulkerson.inCut(team_vertex) == true){
                certificateOfElimination.add(team_index_to_name[team_vertex]);
            }
        }
        for(int source_game_vertex:source_game_vertexes){
            if( fordFulkerson.inCut(source_game_vertex) == true) return true;
        }
        return false;
    }

    private void makeFlowNetwork(int target_team)
    {
        source_game_vertexes = new ArrayList<Integer>();
        //create an empty FlowNetwork
        int total_game_vertexes = factorial(N);
        int total_vertexes = total_game_vertexes+N+2;
        // int total_edges = total_game_vertexes+total_game_vertexes*2+N;
        flowNetwork = new FlowNetwork(total_vertexes);
        // needed variables
        source_vertex = total_vertexes-2;
        sink_vertex = total_vertexes-1;
        int target_team_max_wins = w[target_team] + r[target_team];
        int team_sink_capacity;
        double game_team_edge_capacity;
        int source_game_edge_capacity;
        // create edges from unique team permutation
        FlowEdge team_sink_edge;
        FlowEdge game_teamA_edge;
        FlowEdge game_teamB_edge;
        FlowEdge source_game_edge;
        // i = team
        int game_edge_counter = N;
        for(int i=0; i<N; i++){
            if(i==target_team) {
                team_sink_capacity = 0;
            } else {
                team_sink_capacity = ((target_team_max_wins - w[i])<0) ? 0 : target_team_max_wins - w[i];
            }
            team_sink_edge = new FlowEdge(i,sink_vertex,team_sink_capacity,0);
            flowNetwork.addEdge(team_sink_edge);
            for(int j=i+1; j<N; j++){
                if(i==target_team || j==target_team) {
                    game_team_edge_capacity = 0;
                    source_game_edge_capacity = 0;
                } else {
                    game_team_edge_capacity = Double.POSITIVE_INFINITY;
                    source_game_edge_capacity = g[i][j];
                    source_game_vertexes.add(game_edge_counter);
                }
                game_teamA_edge = new FlowEdge(game_edge_counter, i, game_team_edge_capacity, 0);
                game_teamB_edge = new FlowEdge(game_edge_counter, j, game_team_edge_capacity, 0);
                source_game_edge = new FlowEdge(source_vertex, game_edge_counter, source_game_edge_capacity,0);
                flowNetwork.addEdge(game_teamA_edge);
                flowNetwork.addEdge(game_teamB_edge);
                flowNetwork.addEdge(source_game_edge);
                game_edge_counter++;

            }
        }
    }

    /*****************************************************************************
     *  Testing Function
     *****************************************************************************/

//    public static void main(String[] args) throws FileNotFoundException
//    {
//        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team : division.teams()) {
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            }
//            else {
//                StdOut.println(team + " is not eliminated");
//            }
//        }
//    }

//    public static void main(String[] args) throws FileNotFoundException
//    {
//        BaseballElimination division = new BaseballElimination(args[0]);
//        String team="New_York";
//
//        for (String t : division.certificateOfElimination(team)) {
//            StdOut.print(t + " ");
//        }
//
//        StdOut.println(division.isEliminated(team));
//
//        for (String t : division.certificateOfElimination(team)) {
//            StdOut.print(t + " ");
//        }
//
//    }



}
