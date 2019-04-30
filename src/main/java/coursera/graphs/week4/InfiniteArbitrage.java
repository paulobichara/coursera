package coursera.graphs.week4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.LongStream;

public class InfiniteArbitrage {
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
        List<Edge> outgoing;

        int index;

        Node(int index) {
            outgoing = new ArrayList<>();
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

        private static final long NEGATIVE_INFINITY = Long.MIN_VALUE;
        private static final long POSITIVE_INFINITY = Long.MAX_VALUE;

        Node[] nodes;
        Edge[] edges;

        DirectedGraph(int qtyNodes) {
            nodes  = new Node[qtyNodes];
            for (int index = 0; index < qtyNodes; index++) {
                nodes[index] = new Node(index);
            }
        }

        long[] detectInfiniteArbitrage(int from) {
            long[] distances = new long[nodes.length];
            Arrays.fill(distances, POSITIVE_INFINITY);
            distances[from] = 0;

            for (int iteration = 0; iteration < nodes.length - 1; iteration++) {
                for (Edge edge : edges) {
                    if (distances[edge.origin.index] != POSITIVE_INFINITY
                            && distances[edge.destination.index] > distances[edge.origin.index] + edge.weight) {
                        distances[edge.destination.index] = distances[edge.origin.index] + edge.weight;
                    }
                }
            }

            Set<Node> relaxed = new HashSet<>();
            for (Edge edge : edges) {
                if (distances[edge.origin.index] != POSITIVE_INFINITY
                        && distances[edge.destination.index] > distances[edge.origin.index] + edge.weight) {
                    distances[edge.destination.index] = distances[edge.origin.index] + edge.weight;
                    relaxed.add(edge.destination);
                }
            }

            Queue<Integer> queue = new LinkedList<>();
            relaxed.forEach(node -> queue.add(node.index));
            breadthFirstSearch(queue, distances);

            return distances;
        }

        Node getNode(int index) {
            if (nodes[index] == null) {
                nodes[index] = new Node(index);
            }
            return nodes[index];
        }

        void breadthFirstSearch(Queue<Integer> queue, long[] realDistances) {
            if (queue == null || queue.isEmpty()) {
                return;
            }

            long[] distances = new long[nodes.length];
            Arrays.fill(distances, POSITIVE_INFINITY);
            distances[queue.peek()] = 0;

            while (!queue.isEmpty()) {
                Node current = nodes[queue.poll()];
                realDistances[current.index] = NEGATIVE_INFINITY;
                for (Edge edge : current.outgoing) {
                    Node neighbour = edge.destination;
                    if (distances[neighbour.index] == POSITIVE_INFINITY) {
                        queue.add(neighbour.index);
                        distances[neighbour.index] = distances[current.index] + edge.weight;
                        realDistances[neighbour.index] = NEGATIVE_INFINITY;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int qtyNodes = scanner.nextInt();
            int qtyEdges = scanner.nextInt();
            DirectedGraph graph = new DirectedGraph(qtyNodes);
            Edge[] edges = new Edge[qtyEdges];
            for (int i = 0; i < qtyEdges; i++) {
                int firstIndex = scanner.nextInt() - 1;
                int secondIndex = scanner.nextInt() - 1;
                int weight = scanner.nextInt();
                Edge edge = new Edge(graph.getNode(firstIndex), graph.getNode(secondIndex), weight);
                graph.getNode(firstIndex).outgoing.add(edge);
                edges[i] = edge;
            }
            graph.edges = edges;

            long[] distances = graph.detectInfiniteArbitrage(scanner.nextInt() - 1);
            LongStream.of(distances).forEach(distance -> {
                if (distance == DirectedGraph.POSITIVE_INFINITY) {
                    System.out.println("*");
                } else if (distance == DirectedGraph.NEGATIVE_INFINITY) {
                    System.out.println("-");
                } else {
                    System.out.println(distance);
                }
            });
        }
    }
}
