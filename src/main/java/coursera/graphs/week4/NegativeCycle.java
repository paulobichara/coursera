package coursera.graphs.week4;

import java.util.Arrays;
import java.util.Scanner;

public class NegativeCycle {
    private static class Edge {
        Node origin;
        Node destination;
        int weight;

        Edge(Node origin, Node destination, int weight) {
            this.origin = origin;
            this.destination = destination;
            this.weight = weight;
        }
    }

    private static class Node {
        int index;

        Node(int index) {
            this.index = index;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.index == ((Node)obj).index;
        }
    }

    static class DirectedGraph {

        Node[] nodes;
        Edge[] edges;

        DirectedGraph(int qtyNodes) {
            nodes  = new Node[qtyNodes];
            for (int index = 0; index < qtyNodes; index++) {
                nodes[index] = new Node(index);
            }
        }

        boolean hasNegativeCycle() {
            long[] distances = new long[nodes.length];
            Arrays.fill(distances, Integer.MAX_VALUE);
            distances[0] = 0;

            boolean changed = false;
            for (int iteration = 0; iteration < nodes.length; iteration++) {
                changed = false;
                for (Edge edge : edges) {
                    if (distances[edge.destination.index] > distances[edge.origin.index] + edge.weight) {
                        distances[edge.destination.index] = distances[edge.origin.index] + edge.weight;
                        changed = true;
                    }
                }
            }

            return changed;
        }

        Node getNode(int index) {
            if (nodes[index] == null) {
                nodes[index] = new Node(index);
            }
            return nodes[index];
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int qtyNodes = scanner.nextInt();
        int qtyEdges = scanner.nextInt();
        DirectedGraph graph = new DirectedGraph(qtyNodes);
        Edge[] edges = new Edge[qtyEdges];
        for (int i = 0; i < qtyEdges; i++) {
            int firstIndex = scanner.nextInt() - 1;
            int secondIndex = scanner.nextInt() - 1;
            int weight = scanner.nextInt();
            Edge edge = new Edge(graph.getNode(firstIndex), graph.getNode(secondIndex), weight);
            edges[i] = edge;
        }
        graph.edges = edges;

        System.out.println(graph.hasNegativeCycle() ? "1" : "0");
    }
}
