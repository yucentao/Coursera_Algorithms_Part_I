import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

public class Board {

    private final int[][] blocks;
    private int hamming;
    private int manhattan;
    private final int n;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        hamming = 0;
        manhattan = 0;
        n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(blocks[i], 0, this.blocks[i], 0, n);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) {
                    break;
                }
                if (blocks[i][j] != i * n + j + 1) {
                    hamming++;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    continue;
                }
                int x = (blocks[i][j] - 1) / n;
                int y = (blocks[i][j] - 1) % n;
                manhattan += (Math.abs(x - i) + Math.abs(y - j));
            }
        }
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of blocks out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board twin = null;
        if (blocks[0][0] == 0) {
            swap(blocks, 0, 1, 1, 0);
            twin = new Board(blocks);
            swap(blocks, 0, 1, 1, 0);
        } else if (blocks[0][1] == 0) {
            swap(blocks, 0, 0, 1, 0);
            twin = new Board(blocks);
            swap(blocks, 0, 0, 1, 0);
        } else {
            swap(blocks, 0, 0, 0, 1);
            twin = new Board(blocks);
            swap(blocks, 0, 0, 0, 1);
        }
        return twin;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }
        return Arrays.deepEquals(blocks, that.blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<>();
        int zeroi = 0;
        int zeroj = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    zeroi = i;
                    zeroj = j;
                    break;
                }
            }
        }
        if (zeroi > 0) {
            swap(blocks, zeroi, zeroj, zeroi - 1, zeroj);
            queue.enqueue(new Board(blocks));
            swap(blocks, zeroi, zeroj, zeroi - 1, zeroj);
        }
        if (zeroj > 0) {
            swap(blocks, zeroi, zeroj, zeroi, zeroj - 1);
            queue.enqueue(new Board(blocks));
            swap(blocks, zeroi, zeroj, zeroi, zeroj - 1);
        }
        if (zeroi < n - 1) {
            swap(blocks, zeroi, zeroj, zeroi + 1, zeroj);
            queue.enqueue(new Board(blocks));
            swap(blocks, zeroi, zeroj, zeroi + 1, zeroj);
        }
        if (zeroj < n - 1) {
            swap(blocks, zeroi, zeroj, zeroi, zeroj + 1);
            queue.enqueue(new Board(blocks));
            swap(blocks, zeroi, zeroj, zeroi, zeroj + 1);
        }
        return queue;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private void swap(int[][] b, int fromi, int fromj, int toi, int toj) {
        int tmp = b[fromi][fromj];
        b[fromi][fromj] = b[toi][toj];
        b[toi][toj] = tmp;
    }
}