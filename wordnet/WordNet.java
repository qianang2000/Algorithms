import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class WordNet {

    // symbol table to store synsets
    private ST<Integer, String> synsetST;
    // symbol table to store hypernyms
    private ST<Integer, Queue<Integer>> hypernymST;
    // hash map to store all the nouns and corresponding index
    private HashMap<String, Queue<Integer>> nounSet;
    // record the number of synsets
    private int numSynsets;
    // digraph built based on hypernyms
    private Digraph G;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        synsetST = new ST<>();
        hypernymST = new ST<>();
        nounSet = new HashMap<>();
        numSynsets = 0;
        In readSynsets = new In(synsets);
        while (readSynsets.hasNextLine()) {
            String line = readSynsets.readLine();
            int index = Integer.parseInt(line.split(",")[0]); // get the id number
            String synset = line.split(",")[1]; // get the synsets
            Queue<String> synsetWords = new Queue<>(); // the value of st is queue
            for (String word : synset.split(" ")) {
                synsetWords.enqueue(word);
                Queue<Integer> indexQueue;
                if (!nounSet.containsKey(word)) {
                    indexQueue = new Queue<>();
                }
                else {
                    indexQueue = nounSet.get(word);
                }
                indexQueue.enqueue(index);
                nounSet.put(word, indexQueue);
            }
            synsetST.put(index, synset);
            numSynsets++;
        }

        G = new Digraph(numSynsets);
        In readHypernyms = new In(hypernyms);
        while (readHypernyms.hasNextLine()) {
            String line = readHypernyms.readLine();
            String[] vertices = line.split(",");
            int index = Integer.parseInt(vertices[0]);
            Queue<Integer> hypernymWords = new Queue<>();
            for (int i = 1; i < vertices.length; i++) {
                int target = Integer.parseInt(vertices[i]);
                hypernymWords.enqueue(target);
                G.addEdge(index, target);
            }
            hypernymST.put(index, hypernymWords);
        }
    }

    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return nounSet.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounSet.containsKey(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) throw new IllegalArgumentException();
        if (!isNoun(noun1) || !isNoun(noun2)) throw new IllegalArgumentException();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        Queue<Integer> indexQueue1 = nounSet.get(noun1);
        Queue<Integer> indexQueue2 = nounSet.get(noun2);
        int ancester = sca.ancestorSubset(indexQueue1, indexQueue2);
        return synsetST.get(ancester);
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) throw new IllegalArgumentException();
        if (!isNoun(noun1) || !isNoun(noun2)) throw new IllegalArgumentException();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        Queue<Integer> indexQueue1 = nounSet.get(noun1);
        Queue<Integer> indexQueue2 = nounSet.get(noun2);
        return sca.lengthSubset(indexQueue1, indexQueue2);
    }

    // unit testing (required)
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wn = new WordNet(synsets, hypernyms);
        StdOut.println(wn.sca("white_marlin", "mileage"));
        StdOut.println(wn.distance("white_marlin", "mileage"));
        StdOut.println(wn.sca("Black_Plague", "black_marlin"));
        StdOut.println(wn.distance("Black_Plague", "black_marlin"));
        StdOut.println(wn.sca("American_water_spaniel", "histology"));
        StdOut.println(wn.distance("American_water_spaniel", "histology"));
        StdOut.println(wn.sca("Brown_Swiss", "barrel_roll"));
        StdOut.println(wn.distance("Brown_Swiss", "barrel_roll"));
    }
}

// javac-algs4 WordNet.java
// java-algs4 WordNet synsets100000-subgraph.txt hypernyms100000-subgraph.txt

// String synsets = args[0];
// String hypernyms = args[1];
// WordNet wn = new WordNet(synsets, hypernyms);
// StdOut.println(wn.sca("white_marlin", "mileage"));
// StdOut.println(wn.distance("white_marlin", "mileage"));