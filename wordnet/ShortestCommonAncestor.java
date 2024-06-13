import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.util.Arrays;

public class ShortestCommonAncestor {
    // create an instance variable to copy the mutable digraph
    private Digraph copyGraph;
    // store the length of shortest path from the first vertex
    private Integer[] first;
    // store the length of shortest path from the second vertex
    private Integer[] second;

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (!testDAG(G)) throw new IllegalArgumentException();
        this.copyGraph = new Digraph(G);
        this.first = new Integer[copyGraph.V()];
        this.second = new Integer[copyGraph.V()];
        for (int v = 0; v < copyGraph.V(); v++) {
            this.first[v] = copyGraph.E() + 1;
            this.second[v] = copyGraph.E() + 1;
        }
    }

    // helper method to decide if an input digraph is a DAG
    private boolean testDAG(Digraph G) {
        DirectedCycle cycle = new DirectedCycle(G);
        Topological topological = new Topological(G);
        if (cycle.hasCycle() || !topological.hasOrder()) {
            return false;
        }
        return true;
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= copyGraph.V() || w < 0 || w >= copyGraph.V()) {
            throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths sourceV = new BreadthFirstDirectedPaths(copyGraph, v);
        BreadthFirstDirectedPaths sourceW = new BreadthFirstDirectedPaths(copyGraph, w);
        int minLength = copyGraph.E() + 1;
        for (int i = 0; i < copyGraph.V(); i++) {
            if (sourceV.hasPathTo(i)) {
                first[i] = sourceV.distTo(i);
            }
            if (sourceW.hasPathTo(i)) {
                second[i] = sourceW.distTo(i);
            }
            if (first[i] + second[i] < minLength) {
                minLength = first[i] + second[i];
            }
        }
        return minLength;
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v < 0 || v >= copyGraph.V() || w < 0 || w >= copyGraph.V()) {
            throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths sourceV = new BreadthFirstDirectedPaths(copyGraph, v);
        BreadthFirstDirectedPaths sourceW = new BreadthFirstDirectedPaths(copyGraph, w);
        int minLength = copyGraph.E() + 1;
        int index = -1;
        for (int i = 0; i < copyGraph.V(); i++) {
            if (sourceV.hasPathTo(i)) {
                first[i] = sourceV.distTo(i);
            }
            if (sourceW.hasPathTo(i)) {
                second[i] = sourceW.distTo(i);
            }
            if (first[i] + second[i] < minLength) {
                minLength = first[i] + second[i];
                index = i;
            }
        }
        return index;
    }

    // method to check if two Iterable arguments are valid
    private boolean subsetValide(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        int sizeA = 0;
        int sizeB = 0;
        for (Integer v : subsetA) {
            if (v == null) return false;
            sizeA++;
        }
        for (Integer v : subsetB) {
            if (v == null) return false;
            sizeB++;
        }
        if (sizeA == 0 || sizeB == 0) {
            return false;
        }
        return true;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (!subsetValide(subsetA, subsetB)) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths sourceA = new BreadthFirstDirectedPaths(copyGraph, subsetA);
        BreadthFirstDirectedPaths sourceB = new BreadthFirstDirectedPaths(copyGraph, subsetB);
        int minLength = copyGraph.E() + 1;
        for (int i = 0; i < copyGraph.V(); i++) {
            if (sourceA.hasPathTo(i)) {
                first[i] = sourceA.distTo(i);
            }
            if (sourceB.hasPathTo(i)) {
                second[i] = sourceB.distTo(i);
            }
            if (first[i] + second[i] < minLength) {
                minLength = first[i] + second[i];
            }
        }
        return minLength;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (!subsetValide(subsetA, subsetB)) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths sourceA = new BreadthFirstDirectedPaths(copyGraph, subsetA);
        BreadthFirstDirectedPaths sourceB = new BreadthFirstDirectedPaths(copyGraph, subsetB);
        int minLength = copyGraph.E() + 1;
        int index = -1;
        for (int i = 0; i < copyGraph.V(); i++) {
            if (sourceA.hasPathTo(i)) {
                first[i] = sourceA.distTo(i);
            }
            if (sourceB.hasPathTo(i)) {
                second[i] = sourceB.distTo(i);
            }
            if (first[i] + second[i] < minLength) {
                minLength = first[i] + second[i];
                index = i;
            }
        }
        return index;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        // test length and ancestor
        // while (!StdIn.isEmpty()) {
        //     int v = StdIn.readInt();
        //     int w = StdIn.readInt();
        //     int length = sca.length(v, w);
        //     int ancestor = sca.ancestor(v, w);
        //     StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }
        // test subset length and subset ancestor
        Iterable<Integer> setA = Arrays.asList(13, 23, 24);
        Iterable<Integer> setB = Arrays.asList(6, 16, 17);
        int length = sca.lengthSubset(setA, setB);
        int ancestor = sca.ancestorSubset(setA, setB);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}

// javac-algs4 ShortestCommonAncestor.java
