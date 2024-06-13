import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wn;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wn = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDist = 0;
        String candidate = "";
        for (String noun : nouns) {
            int dist = 0;
            for (String other : nouns) {
                dist += wn.distance(noun, other);
            }
            if (dist > maxDist) {
                maxDist = dist;
                candidate = noun;
            }
        }
        return candidate;
    }

    // test client (see below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}

// javac-algs4 Outcast.java