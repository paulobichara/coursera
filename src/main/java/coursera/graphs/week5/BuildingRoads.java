package coursera.graphs.week5;

import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.DoubleStream;

public class BuildingRoads {

    private static class NodeComparator implements Comparator<Node> {
        double[] distances;

        NodeComparator(double[] distances) {
            this.distances = distances;
        }

        @Override
        public int compare(Node o1, Node o2) {
            return Double.compare(distances[o1.index], distances[o2.index]);
        }
    }

    private static class Node {
        int index;
        int coordinateX;
        int coordinateY;

        Node(int index, int coordinateX, int coordinateY) {
            this.index = index;
            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;
        }

        double distanceTo(Node other) {
            return Math.sqrt(Math.pow(coordinateX - other.coordinateX, 2) + Math.pow(coordinateY - other.coordinateY, 2));
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.index == ((Node)obj).index;
        }
    }

    static class Graph {

        Node[] nodes;

        Graph(Node[] nodes) {
            this.nodes = nodes;
        }

        private double prim() {
            double[] distances = new double[nodes.length];
            distances[0] = 0;

            for (int index = 0; index < distances.length; index++) {
                if (index != 0) {
                    distances[index] = nodes[0].distanceTo(nodes[index]);
                }
            }

            NodeComparator comparator = new NodeComparator(distances);
            PriorityQueue<Node> queue = new PriorityQueue<>(comparator);

            Collections.addAll(queue, nodes);

            while (!queue.isEmpty()) {
                Node node = queue.poll();
                for (Node other : nodes) {
                    double possibility = node.distanceTo(other);
                    if (!node.equals(other) && queue.contains(other) && distances[other.index] > possibility) {
                        queue.remove(other);
                        distances[other.index] = possibility;
                        queue.add(other);
                    }
                }
            }
            return DoubleStream.of(distances).sum();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int qtyNodes = scanner.nextInt();
        Node[] nodes = new Node[qtyNodes];

        for (int index = 0; index < qtyNodes; index++) {
            nodes[index] = new Node(index, scanner.nextInt(), scanner.nextInt());
        }

        Graph graph = new Graph(nodes);
        System.out.println(graph.prim());
    }
}
