import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public class BinarySearchDeluxe {

    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException();
        }
        int low = 0;
        int high = a.length - 1;
        int result = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (comparator.compare(key, a[mid]) > 0) {
                low = mid + 1;
            }
            else if (comparator.compare(key, a[mid]) < 0) {
                high = mid - 1;
            }
            else {
                result = mid;
                high = mid - 1;
            }
        }
        return result;
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException();
        }
        int low = 0;
        int high = a.length - 1;
        int result = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (comparator.compare(key, a[mid]) > 0) {
                low = mid + 1;
            }
            else if (comparator.compare(key, a[mid]) < 0) {
                high = mid - 1;
            }
            else {
                result = mid;
                low = mid + 1;
            }
        }
        return result;
    }

    // unit testing (required)
    @Test
    public static void testlexicographicOrder() {
        Term termA = new Term("sydney", 25);
        Term termB = new Term("noah", 36);
        Term termC = new Term("noah", 14);
        Term termD = new Term("noah", 36);
        Term termE = new Term("letsile", 14);
        Term termF = new Term("noah", 36);
        Term termG = new Term("fred", 51);
        Term termH = new Term("simone", 19);
        Term[] terms = { termG, termE, termB, termC, termD, termF, termA };

        Assert.assertEquals(2, firstIndexOf(terms, termB, Comparator.naturalOrder()));
        Assert.assertEquals(5, lastIndexOf(terms, termB, Comparator.naturalOrder()));
        Assert.assertEquals(1, firstIndexOf(terms, termE, Comparator.naturalOrder()));
        Assert.assertEquals(1, lastIndexOf(terms, termE, Comparator.naturalOrder()));
        Assert.assertEquals(-1, firstIndexOf(terms, termH, Comparator.naturalOrder()));
        Assert.assertEquals(-1, lastIndexOf(terms, termH, Comparator.naturalOrder()));
    }

    @Test
    public static void testReverseOrder() {
        Term termA = new Term("sydney", 75);
        Term termB = new Term("noah", 66);
        Term termC = new Term("noah", 64);
        Term termD = new Term("noah", 61);
        Term termE = new Term("letsile", 61);
        Term termF = new Term("noah", 61);
        Term termG = new Term("fred", 53);
        Term termH = new Term("simone", 49);
        Term[] terms = { termA, termB, termC, termD, termE, termF, termG };
        Assert.assertEquals(3, firstIndexOf(terms, termD, Term.byReverseWeightOrder()));
        Assert.assertEquals(5, lastIndexOf(terms, termD, Term.byReverseWeightOrder()));
        Assert.assertEquals(6, firstIndexOf(terms, termG, Term.byReverseWeightOrder()));
        Assert.assertEquals(6, lastIndexOf(terms, termG, Term.byReverseWeightOrder()));
        Assert.assertEquals(-1, firstIndexOf(terms, termH, Term.byReverseWeightOrder()));
        Assert.assertEquals(-1, lastIndexOf(terms, termH, Term.byReverseWeightOrder()));
    }

    public static void main(String[] args) {
        testlexicographicOrder();
        testReverseOrder();
    }
}

