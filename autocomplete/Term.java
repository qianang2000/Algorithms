import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class Term implements Comparable<Term> {
    private String query;
    private long weight;

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null || weight < 0) {
            throw new IllegalArgumentException();
        }
        this.query = query;
        this.weight = weight;

    }

    // Get the weight of the term.
    public long getWeight() {
        return this.weight;
    }

    // Get the query of the term
    public String getQuery() {
        return this.query;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        Comparator<Term> byReverseWeightOrder = (t1, t2) -> {
            return Long.compare(t2.getWeight(), t1.getWeight());
        };
        return byReverseWeightOrder;
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0) {
            throw new IllegalArgumentException();
        }
        Comparator<Term> byPrefixOrder = (t1, t2) -> {
            return t1.getQuery().substring(0, r).compareTo(t2.getQuery().substring(0, r));
        };
        return byPrefixOrder;
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        return this.getQuery().compareTo(that.getQuery());
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return this.weight + " " + this.query;
    }

    // unit testing (required)
    @Test
    public static void testCompare() {
        Term termA = new Term("text", 7);
        Term termB = new Term("test", 5);
        Term termC = new Term("tenure", 11);
        Term termD = new Term("terry", 9);
        Assert.assertEquals(5, termA.compareTo(termB));
        Assert.assertEquals(10, termA.compareTo(termC));
        Assert.assertEquals(6, termA.compareTo(termD));
        Assert.assertEquals(5, termB.compareTo(termC));
        Assert.assertEquals(1, termB.compareTo(termD));
        Assert.assertEquals(-4, termC.compareTo(termD));
    }

    @Test
    public static void testReverseOrder() {
        Term termA = new Term("text", 7);
        Term termB = new Term("test", 5);
        Term termC = new Term("tenure", 11);
        Term termD = new Term("terry", 9);
        Term[] terms = { termA, termB, termC, termD };
        Arrays.sort(terms, byReverseWeightOrder());
        Assert.assertEquals(11, terms[0].getWeight());
        Assert.assertEquals(9, terms[1].getWeight());
        Assert.assertEquals(7, terms[2].getWeight());
        Assert.assertEquals(5, terms[3].getWeight());

    }

    @Test
    public static void testPrefixOrder() {
        Term termA = new Term("text", 7);
        Term termB = new Term("test", 5);
        Term termC = new Term("tenure", 11);
        Term termD = new Term("terry", 9);
        Term[] terms = { termA, termB, termC, termD };
        Arrays.sort(terms, byPrefixOrder(4));
        Assert.assertEquals(11, terms[0].getWeight());
        Assert.assertEquals(9, terms[1].getWeight());
        Assert.assertEquals(5, terms[2].getWeight());
        Assert.assertEquals(7, terms[3].getWeight());
    }

    public static void main(String[] args) {
        testCompare();
        testReverseOrder();
        testPrefixOrder();
    }

}
