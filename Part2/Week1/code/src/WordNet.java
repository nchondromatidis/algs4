import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

public class WordNet {

    private Digraph synsets_DG;
    private HashMap<String, ArrayList<Integer>> nouns_to_synsets_ST = new HashMap<String, ArrayList<Integer>>();
    private HashMap<Integer, String> synsets_to_nouns_ST = new HashMap<Integer, String>();
    private Integer total_sysets = 0;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        try {
            create_nouns_ST_from_csv(synsets);
            create_hypernyms_digraph_from_csv(hypernyms);
            check_input_is_rooted_DAG();
        } catch (IOException e) {
            StdOut.println(e.getMessage());
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return  nouns_to_synsets_ST.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) throws NullPointerException
    {
        if(word == null) throw new NullPointerException("null noun given");
        return  nouns_to_synsets_ST.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if(nounA == null || nounB == null) throw new NullPointerException("null noun given");
        if(sap == null) sap = new SAP(synsets_DG);
        ArrayList<Integer> synsets_a_list = nouns_to_synsets_ST.get(nounA);
        ArrayList<Integer> synsets_b_list = nouns_to_synsets_ST.get(nounB);
        if(synsets_a_list == null || synsets_b_list == null) throw new IllegalArgumentException("Noun does not exist");
        return sap.length(synsets_a_list, synsets_b_list);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) throws IllegalArgumentException
    {
        if(nounA == null || nounB == null) throw new NullPointerException("null noun given");
        if(sap == null) sap = new SAP(synsets_DG);
        ArrayList<Integer> synsets_a_list = nouns_to_synsets_ST.get(nounA);
        ArrayList<Integer> synsets_b_list = nouns_to_synsets_ST.get(nounB);
        if(synsets_a_list == null || synsets_b_list == null) throw new IllegalArgumentException("Noun does not exist");
        int sap_common_ancestor_synset_id = sap.ancestor(synsets_a_list, synsets_b_list);
        return synsets_to_nouns_ST.get(sap_common_ancestor_synset_id);
    }

    /***********************************************************************
     *  Helper Functions
     ***********************************************************************/
    private  void check_input_is_rooted_DAG() throws  IllegalArgumentException
    {
        check_input_is_DAG();
        check_input_is_rooted();

    }

    private  void check_input_is_DAG() throws  IllegalArgumentException
    {
        DirectedCycle finder = new DirectedCycle(synsets_DG);
        if (finder.hasCycle()) throw new  IllegalArgumentException("not a DAG");
    }

    private  void check_input_is_rooted() throws  IllegalArgumentException
    {
        Integer counter=0;
        for(Integer v=0; v<synsets_DG.V(); v++ ){
            if(synsets_DG.outdegree(v)==0) counter++;
            if(counter > 1 ) throw new IllegalArgumentException("Graph is not rooted DAG");
        }
    }

    private void create_hypernyms_digraph_from_csv(String csv_file_path) throws  IOException
    {
            Integer hypernym_vertex;
            Integer hyponym_vertex;
            synsets_DG = new Digraph(total_sysets);

            String COLUMN_DELIMITER = ",";
            String line = null;
            BufferedReader reader = new BufferedReader(new FileReader(csv_file_path));
            while ((line = reader.readLine()) != null) {
                String[] line_split = line.split(COLUMN_DELIMITER);
                hypernym_vertex = Integer.parseInt(line_split[0]);
                for(int i=1;i<line_split.length;i++){
                    hyponym_vertex =  Integer.parseInt(line_split[i]);
                    synsets_DG.addEdge(hypernym_vertex, hyponym_vertex);
                }
            }
    }

    private void create_nouns_ST_from_csv(String csv_file_path) throws  IOException
    {
        Integer synset_id;
        ArrayList<Integer> noun_synset_id_list;
        String[] nouns;
        String COLUMN_DELIMITER = ",";
        String NOUNS_DELIMITER = " ";
        String line = null;
        BufferedReader reader = new BufferedReader(new FileReader(csv_file_path));
        while ((line = reader.readLine()) != null) {
            String[] line_split = line.split(COLUMN_DELIMITER);
            synset_id = Integer.parseInt(line_split[0]);
            synsets_to_nouns_ST.put(synset_id, line_split[1]);
            nouns = line_split[1].split(NOUNS_DELIMITER);
            for(String noun: nouns){
                noun_synset_id_list = new ArrayList<Integer>();
                if(nouns_to_synsets_ST.get(noun) != null){
                    noun_synset_id_list = nouns_to_synsets_ST.get(noun);
                }
                noun_synset_id_list.add(synset_id);
                nouns_to_synsets_ST.put(noun, noun_synset_id_list);
            }
            total_sysets++;
        };
    }

    /***********************************************************************
     *  Test
     ***********************************************************************/
    // do unit testing of this class
    public static void main(String[] args)
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.distance("white_marlin", "mileage"));
        StdOut.println(wordnet.distance("Black_Plague", "black_marlin"));
        StdOut.println(wordnet.distance("American_water_spaniel", "histology"));
        StdOut.println(wordnet.distance("Brown_Swiss", "barrel_roll"));
        StdOut.println(wordnet.nouns_to_synsets_ST.get("worm"));
        StdOut.println(wordnet.nouns_to_synsets_ST.get("bird"));
        StdOut.println(wordnet.sap("worm","bird"));
    }
}