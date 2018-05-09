import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

public class Solver {

    private boolean isSolvable;
    private Node finalNode = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("arg null");
        }
        final MinPQ<Node> q1 = new MinPQ<>();
        final MinPQ<Node> q2 = new MinPQ<>();
        q1.insert(new Node(initial, null, 0));
        q2.insert(new Node(initial.twin(), null, 0));
        isSolvable = false;
        Node n1 = q1.delMin();
        Node n2 = q2.delMin();
        while (!n1.board.isGoal() && !n2.board.isGoal()) {
            for (Board neighbor : n1.board.neighbors()) {
                if (n1.prev == null || !n1.prev.board.equals(neighbor)) {
                    q1.insert(new Node(neighbor, n1, n1.move + 1));
                }
            }
            for (Board neighbor : n2.board.neighbors()) {
                if (n2.prev == null || !n2.prev.board.equals(neighbor)) {
                    q2.insert(new Node(neighbor, n2, n2.move + 1));
                }
            }
            n1 = q1.delMin();
            n2 = q2.delMin();
        }
        if (n1.board.isGoal()) {
            isSolvable = true;
            finalNode = n1;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable) {
            return -1;
        }
        return finalNode.move;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable) {
            return null;
        }
        Stack<Board> stack = new Stack<>();
        Node node = finalNode;
        while (node != null) {
            stack.push(node.board);
            node = node.prev;
        }
        return stack;
    }

    private static class Node implements Comparable<Node> {
        private final Board board;
        private final Node prev;
        private final int move;

        public Node(Board board, Node prev, int move) {
            this.board = board;
            this.prev = prev;
            this.move = move;
        }

        @Override
        public int compareTo(Node n) {
            int sum1 = board.manhattan() + move;
            int sum2 = n.board.manhattan() + n.move;
            return Integer.compare(sum1, sum2);
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}