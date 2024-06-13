import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Autocomplete {
    private Term[] terms;

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        if (terms == null) {
            throw new IllegalArgumentException();
        }
        for (Term term : terms) {
            if (term == null) {
                throw new IllegalArgumentException();
            }
        }
        this.terms = terms;
    }

    // Returns all terms that start with the given prefix,
    // in descending order of weight.
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        Arrays.sort(terms, Comparator.naturalOrder());
        Term key = new Term(prefix, 0);
        int begin = BinarySearchDeluxe.firstIndexOf(terms, key,
                                                    Term.byPrefixOrder(prefix.length()));
        int end = BinarySearchDeluxe.lastIndexOf(terms, key,
                                                 Term.byPrefixOrder(prefix.length()));
        if (begin == -1 || end == -1) {
            return new Term[0];
        }
        Term[] result = Arrays.copyOfRange(terms, begin, end + 1);
        Arrays.sort(result, Term.byReverseWeightOrder());
        return result;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        Arrays.sort(terms, Comparator.naturalOrder());
        Term key = new Term(prefix, 0);
        int begin = BinarySearchDeluxe.firstIndexOf(terms, key,
                                                    Term.byPrefixOrder(prefix.length()));
        int end = BinarySearchDeluxe.lastIndexOf(terms, key,
                                                 Term.byPrefixOrder(prefix.length()));
        if (begin == -1 || end == -1) {
            return 0;
        }
        return end - begin + 1;
    }

    // unit testing (required)
    // @Test
    // public static void testNumberOfMatches() {
    //     Term termA = new Term("substring", 12);
    //     Term termB = new Term("subway", 22);
    //     Term termC = new Term("super", 14);
    //     Term termD = new Term("sun", 19);
    //     Term termE = new Term("subcome", 20);
    //     Term termF = new Term("section", 31);
    //     Term termG = new Term("subtask", 32);
    //     Term[] terms = { termA, termB, termC, termD, termE, termF, termG };
    //     Autocomplete autocomplete = new Autocomplete(terms);
    //     Assert.assertEquals(4, autocomplete.numberOfMatches("sub"));
    //     Assert.assertEquals(1, autocomplete.numberOfMatches("sup"));
    //     Assert.assertEquals(0, autocomplete.numberOfMatches("son"));
    // }
    //
    // @Test
    // public static void testAllMatches() {
    //     Term termA = new Term("substring", 12);
    //     Term termB = new Term("subway", 22);
    //     Term termC = new Term("super", 14);
    //     Term termD = new Term("sun", 19);
    //     Term termE = new Term("subcome", 20);
    //     Term termF = new Term("section", 31);
    //     Term termG = new Term("subtask", 32);
    //     Term[] terms = { termA, termB, termC, termD, termE, termF, termG };
    //     Autocomplete autocomplete = new Autocomplete(terms);
    //     Term[] resultOne = autocomplete.allMatches("sub");
    //     Assert.assertEquals(4, resultOne.length);
    //     Assert.assertEquals(termG, resultOne[0]);
    //     Assert.assertEquals(termB, resultOne[1]);
    //     Assert.assertEquals(termE, resultOne[2]);
    //     Assert.assertEquals(termA, resultOne[3]);
    //     Term[] resultTwo = autocomplete.allMatches("sup");
    //     Assert.assertEquals(1, resultTwo.length);
    //     Assert.assertEquals(termC, resultTwo[0]);
    //     Term[] resultThree = autocomplete.allMatches("son");
    //     Assert.assertEquals(0, resultThree.length);
    // }

    public static void main(String[] args) {
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and print the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            StdOut.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }
    }
}
