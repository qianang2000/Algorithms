import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // the length of percolation grid
    private int length;
    // check if the site (i, j) is open
    private WeightedQuickUnionUF isOpen;
    // check if the site (i, j) is full
    private WeightedQuickUnionUF isFull;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.length = n;
        if (n <= 0) throw new IllegalArgumentException();
        // the last position is open
        isOpen = new WeightedQuickUnionUF(n * n + 1);
        // the last position is full, second last position is open
        isFull = new WeightedQuickUnionUF(n * n + 1);
    }

    // convert the 2d grid coordinate to 1d array coordinate
    private int convert(int row, int col) {
        if (row < 0 || row >= length || col < 0 || col >= length) {
            throw new IllegalArgumentException();
        }
        return row * length + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int n = length;
        if (row < 0 || row >= n || col < 0 || col >= n)
            throw new IllegalArgumentException();
        int index = convert(row, col);
        isOpen.union(index, n * n);
        if (row == 0) isFull.union(index, n * n);
        // idea: if one of left, right, top and down sites is full, and the site is open, then union
        if (col > 0) {
            int left = convert(row, col - 1);
            if (isOpen(row, col - 1)) {
                isFull.union(left, index);
            }
            if (connected(isFull, left, n * n)) {
                isFull.union(index, n * n);
            }
        }
        if (col < n - 1) {
            int right = convert(row, col + 1);
            if (isOpen(row, col + 1)) {
                isFull.union(right, index);
            }
            if (connected(isFull, right, n * n)) {
                isFull.union(index, n * n);
            }
        }
        if (row > 0) {
            int top = convert(row - 1, col);
            if (isOpen(row - 1, col)) {
                isFull.union(top, index);
            }
            if (connected(isFull, top, n * n)) {
                isFull.union(index, n * n);
            }
        }
        if (row < n - 1) {
            int bottom = convert(row + 1, col);
            if (isOpen(row + 1, col)) {
                isFull.union(bottom, index);
            }
            if (connected(isFull, bottom, n * n)) {
                isFull.union(index, n * n);
            }
        }
    }

    // is p and q in the same component?
    private boolean connected(WeightedQuickUnionUF uf, int p, int q) {
        int n = length;
        if (p < 0 || p >= n * n + 1 || q < 0 || q >= n * n + 1) {
            throw new IllegalArgumentException();
        }
        return uf.find(p) == uf.find(q);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int n = length;
        if (row < 0 || row >= n || col < 0 || col >= n)
            throw new IllegalArgumentException();
        return connected(isOpen, convert(row, col), n * n);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int n = length;
        if (row < 0 || row >= n || col < 0 || col >= n)
            throw new IllegalArgumentException();
        int index = convert(row, col);
        return connected(isFull, index, n * n);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int n = length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (isOpen(i, j)) count++;
            }
        }
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        int n = length;
        for (int col = 0; col < n; col++) {
            if (isFull(n - 1, col)) return true;
        }
        return false;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        percolation.open(0, 0);
        percolation.open(0, 1);
        percolation.open(1, 1);
        StdOut.println(percolation.isFull(1, 1)); // true
        StdOut.println(percolation.isFull(1, 2)); // false
        StdOut.println(percolation.numberOfOpenSites());  // 3
        StdOut.println(percolation.percolates());  // false
        percolation.open(2, 2);
        StdOut.println(percolation.isFull(2, 2));  // false
        StdOut.println(percolation.numberOfOpenSites());  // 4
        StdOut.println(percolation.percolates());  // false
        percolation.open(2, 1);
        StdOut.println(percolation.isFull(2, 1));  // true
        StdOut.println(percolation.numberOfOpenSites());  // 5
        StdOut.println(percolation.percolates());  // true
    }
}

// javac-algs4 Percolation.java
// java-algs4 Percolation

// javac-algs4 PercolationVisualizer.java
// java-algs4 PercolationVisualizer input10.txt