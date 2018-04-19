/******************************************************************************
 *  Compilation:  javac InteractivePercolationVisualizer.java
 *  Execution:    java InteractivePercolationVisualizer n
 *  Dependencies: PercolationVisualizer.java Percolation.java
 *                StdDraw.java StdOut.java
 *
 *  This program takes the grid size n as a command-line argument.
 *  Then, the user repeatedly clicks sites to open with the mouse.
 *  After each site is opened, it draws full sites in light blue,
 *  open sites (that aren't full) in white, and blocked sites in black.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int cnt;
    private final double[] nums;
    private double mean = -1;
    private double stddev = -1;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n, trials not right");
        }
        cnt = trials;
        nums = new double[cnt];
        double all = n * n;
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            nums[i] = percolation.numberOfOpenSites() / all;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean < 0) {
            mean = StdStats.mean(nums);
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddev < 0) {
            stddev = StdStats.stddev(nums);
        }
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        if (mean < 0) {
            mean();
        }
        if (stddev < 0) {
            stddev();
        }
        return mean - CONFIDENCE_95 * stddev / Math.sqrt(cnt);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        if (mean < 0) {
            mean();
        }
        if (stddev < 0) {
            stddev();
        }
        return mean + CONFIDENCE_95 * stddev / Math.sqrt(cnt);
    }

    // test client (described below)
    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean\t= " + percolationStats.mean());
        StdOut.println("stddev\t= " + percolationStats.stddev());
        StdOut.println("95% confidence interval\t= ["+ percolationStats.confidenceLo() + ", "+ percolationStats.confidenceHi() +"]");
    }
}