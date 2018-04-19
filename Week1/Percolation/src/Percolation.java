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

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF weightedQuickUnionUFA;
    private final WeightedQuickUnionUF weightedQuickUnionUFB;
    private final int size;
    private int openSites;
    private boolean[] sitesStatus;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be larger than 0");
        }
        openSites = 0;
        sitesStatus = new boolean[n * n];
        size = n;
        weightedQuickUnionUFA = new WeightedQuickUnionUF(n * n + 2);
        weightedQuickUnionUFB = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 1; i <= n; i++) {
            weightedQuickUnionUFA.union(0, i);
            weightedQuickUnionUFB.union(0, i);
            weightedQuickUnionUFA.union(n * n + 1, n * n + 1 - i);
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isIndexValid(row, col)) {
            throw new IllegalArgumentException("index not valid");
        }
        if (!isOpen(row, col)) {
            openSites++;
            sitesStatus[(row - 1) * size + col - 1] = true;
            int index = indexCal(row, col);
            connect(index, row, col + 1);
            connect(index, row, col - 1);
            connect(index, row + 1, col);
            connect(index, row - 1, col);
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isIndexValid(row, col)) {
            throw new IllegalArgumentException("index not valid");
        }
        return sitesStatus[(row - 1) * size + col - 1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isIndexValid(row, col)) {
            throw new IllegalArgumentException("index not valid");
        }
        return isOpen(row, col) && weightedQuickUnionUFB.connected(0, indexCal(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (size == 1) {
            return isOpen(1, 1);
        }
        return weightedQuickUnionUFA.connected(0, size * size + 1);
    }

    private void connect(int index, int row, int col) {
        if (isIndexValid(row, col) && isOpen(row, col)) {
            weightedQuickUnionUFA.union(index, indexCal(row, col));
            weightedQuickUnionUFB.union(index, indexCal(row, col));
        }
    }

    private int indexCal(int row, int col) {
        return (row - 1) * size + col - 1 + 1;
    }

    private boolean isIndexValid(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            return false;
        }
        return true;
    }
}