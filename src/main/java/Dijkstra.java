import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Dijkstra {
    private static class Edge {
        Node destination;
        int weight;

        Edge(Node destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    private static class NodeComparator implements Comparator<Node> {
        long[] distances;

        NodeComparator(long[] distances) {
            this.distances = distances;
        }

        @Override
        public int compare(Node o1, Node o2) {
            return Long.compare(distances[o1.index], distances[o2.index]);
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

        Node[] nodes;

        DirectedGraph(int qtyNodes) {
            nodes  = new Node[qtyNodes];
            for (int index = 0; index < qtyNodes; index++) {
                nodes[index] = new Node(index);
            }
        }

        long getMinimumFlightCost(int origin, int destination) {
            long[] distances = dijkstra(nodes[origin]);
            return distances[destination] == Integer.MAX_VALUE ? -1 : distances[destination];
        }

        private long[] dijkstra(Node start) {
            long[] distances = new long[nodes.length];
            Arrays.fill(distances, Integer.MAX_VALUE);
            distances[start.index] = 0;
            NodeComparator comparator = new NodeComparator(distances);

            PriorityQueue<Node> queue = new PriorityQueue<>(comparator);
            Collections.addAll(queue, nodes);

            while (!queue.isEmpty()) {
                Node node = queue.poll();
                for (Edge edge : node.outgoing) {
                    if (distances[edge.destination.index] > distances[node.index] + edge.weight) {
                        queue.remove(edge.destination);
                        distances[edge.destination.index] = distances[node.index] + edge.weight;
                        comparator.distances = distances;
                        queue.add(edge.destination);
                    }
                }
            }
            return distances;
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
        DirectedGraph graph = new DirectedGraph(scanner.nextInt());

        int qtyEdges = scanner.nextInt();
        for (int i = 0; i < qtyEdges; i++) {
            int firstIndex = scanner.nextInt() - 1;
            int secondIndex = scanner.nextInt() - 1;
            int weight = scanner.nextInt();
            graph.getNode(firstIndex).outgoing.add( new Edge(graph.getNode(secondIndex), weight));
        }

        int origIndex = scanner.nextInt() - 1;
        int destIndex = scanner.nextInt() - 1;
        System.out.println(graph.getMinimumFlightCost(origIndex, destIndex));
    }
}
