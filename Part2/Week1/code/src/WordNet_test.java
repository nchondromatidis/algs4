import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class WordNet_test {
    @Test(expected=IllegalArgumentException.class)
    public void test_IllegalArgumentException_synsets3_hypernyms3InvalidTwoRoots()
    {
        WordNet wordnet = new WordNet("..\\material\\wordnet\\synsets3.txt", "..\\material\\wordnet\\hypernyms3InvalidTwoRoots.txt");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_IllegalArgumentException_synsets3_hypernyms3InvalidCycle()
    {
        WordNet wordnet = new WordNet("..\\material\\wordnet\\synsets3.txt", "..\\material\\wordnet\\hypernyms3InvalidCycle.txt");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_IllegalArgumentException_synsets6_hypernyms6InvalidTwoRoots()
    {
        WordNet wordnet = new WordNet("..\\material\\wordnet\\synsets6.txt", "..\\material\\wordnet\\hypernyms6InvalidTwoRoots.txt");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_IllegalArgumentException_synsets6_hypernyms6InvalidCycle()
    {
        WordNet wordnet = new WordNet("..\\material\\wordnet\\synsets6.txt", "..\\material\\wordnet\\hypernyms6InvalidCycle.txt");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_IllegalArgumentException_synsets6_hypernyms6InvalidCycle_Path()
    {
        WordNet wordnet = new WordNet("..\\material\\wordnet\\synsets6.txt", "..\\material\\wordnet\\hypernyms6InvalidCycle+Path.txt");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_IllegalArgumentException_distance_sap_synsets15_hypernyms15Tree_invalid_noun_invalid()
    {
        WordNet wordnet = new WordNet("..\\material\\wordnet\\synsets15.txt", "..\\material\\wordnet\\hypernyms15Tree.txt");
        wordnet.sap("invalid","invalid");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_IllegalArgumentException_distance_sap_synsets15_hypernyms15Tree_invalid_noun_eleventeen()
    {
        WordNet wordnet = new WordNet("..\\material\\wordnet\\synsets15.txt", "..\\material\\wordnet\\hypernyms15Tree.txt");
        wordnet.sap("eleventeen","eleventeen");
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_IllegalArgumentException_distance_sap_synsets15_hypernyms15Tree_INVALID()
    {
        WordNet wordnet = new WordNet("..\\material\\wordnet\\synsets15.txt", "..\\material\\wordnet\\hypernyms15Tree.txt");
        wordnet.sap("INVALID","INVALID");
    }

    @Test(expected=NullPointerException.class)
    public void test_NullPointerException_distance_sap_synsets15_hypernyms15Path_isNoun_null_arguments()
    {
        WordNet wordnet = new WordNet("..\\material\\wordnet\\synsets15.txt", "..\\material\\wordnet\\hypernyms15Tree.txt");
        wordnet.isNoun(null);
    }

    @Test
    public void exaclt_one_SAP_object_created_per_WordNet_Object()
    {

    }


    // SAP_test : timing sap() and distance() with random nouns

}
