import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

    // number of trials
    private int T;
    // array to store the percolation threshold of each trial
    private double[] threshold;
    // number of 97.5% quantile
    private final double quant = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        T = trials;
        threshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            int count = 0;
            while (!percolation.percolates()) {
                int index = StdRandom.uniformInt(n * n);
                int row = index / n;
                int col = index % n;
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                    count++;
                }
            }
            threshold[i] = (double) count / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - quant * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + quant * stddev() / Math.sqrt(T);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        Stopwatch stopwatch = new Stopwatch();
        PercolationStats percolationStats = new PercolationStats(n, trials);
        StdOut.println("mean()           = " + String.format("%6f", percolationStats.mean()));
        StdOut.println("stddev()         = " + String.format("%6f", percolationStats.stddev()));
        StdOut.println(
                "confidenceLow()  = " + String.format("%6f", percolationStats.confidenceLow()));
        StdOut.println(
                "confidenceHigh() = " + String.format("%6f", percolationStats.confidenceHigh()));
        double time = stopwatch.elapsedTime();
        StdOut.println("elapsed time     = " + String.format("%3f", time));
    }
}
