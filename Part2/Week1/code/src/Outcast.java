public class Outcast {

    private WordNet wordNet;
    //constructor takes a WordNet object
    public Outcast(WordNet wordNet){
        this.wordNet = wordNet;
    }

    //given an array of WordNet nouns, return the outcast
    public String outcast(String[] nouns){
        Integer max_di = 0;
        String outcast = null;
        for(String outer_noun: nouns){
            Integer di = 0;
            for(String inner_noun: nouns){
                di +=  wordNet.distance(outer_noun,inner_noun);
            }
            if(max_di < di) {
                max_di = di;
                outcast = outer_noun;
            }
        }
        return outcast;
    }

    public static void main(String[] args){
        WordNet wordNet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordNet);
        for (int t = 2; t < args.length; t++){
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
