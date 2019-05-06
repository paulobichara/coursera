package coursera.graphs.week5;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class Clustering {

    private static class Edge implements Comparable<Edge> {
        Node first;
        Node second;
        double weight;

        Edge(Node first, Node second) {
            this.first = first;
            this.second = second;
            weight = first.distanceTo(second);
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(weight, other.weight);
        }
    }

    private static class Node {
        int index;
        int coordinateX;
        int coordinateY;
        Map<Integer, Edge> neighbours;

        Node(int index, int coordinateX, int coordinateY) {
            this.index = index;
            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;
            neighbours = new HashMap<>();
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

    static class DisjointSet {
        DisjointSet parent;
        int rank;
        Node node;
        int size;

        DisjointSet(Node node) {
            this.node = node;
            rank = 0;
            parent = this;
            size = 1;
        }

        DisjointSet getParent() {
            if (parent != this) {
                parent = parent.getParent();
            }
            return parent;
        }

        void merge(DisjointSet other) {
            DisjointSet firstRoot = getParent();
            DisjointSet secondRoot = other.getParent();

            if (firstRoot == secondRoot) {
                return;
            }

            if (firstRoot.rank > secondRoot.rank) {
                secondRoot.parent = firstRoot;
                firstRoot.size = firstRoot.size + secondRoot.size + 1;
                secondRoot.size = 0;
            } else {
                firstRoot.parent = secondRoot;
                secondRoot.size = secondRoot.size + firstRoot.size + 1;
                firstRoot.size = 0;
                if (secondRoot.rank == firstRoot.rank) {
                    secondRoot.rank++;
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof  DisjointSet)) {
                return false;
            }
            return node.equals(((DisjointSet)obj).node);
        }
    }

    static class Graph {

        Node[] nodes;
        Edge[] edges;

        Graph(Node[] nodes) {
            this.nodes = nodes;
            edges = new Edge[(nodes.length * (nodes.length - 1)) / 2];

            int edgeIndex = 0;
            for (Node node : nodes) {
                for (Node other : nodes) {
                    if (!node.equals(other) && !node.neighbours.containsKey(other.index)) {
                        Edge edge = new Edge(node, other);
                        node.neighbours.put(other.index, edge);
                        other.neighbours.put(node.index, edge);
                        edges[edgeIndex] = edge;
                        edgeIndex++;
                    }
                }
            }
        }

        double modifiedKruskal(int clusterQty) {
            Map<Integer, DisjointSet> setMap = new HashMap<>();
            Stream.of(nodes).forEach(node -> setMap.put(node.index, new DisjointSet(node)));
            Arrays.sort(edges);
            int numClusters = nodes.length;

            for (int index = 0; index < edges.length && numClusters > clusterQty; index++) {
                Edge edge = edges[index];
                DisjointSet first = setMap.get(edge.first.index).getParent();
                DisjointSet second = setMap.get(edge.second.index).getParent();
                if (!first.node.equals(second.node)) {
                    first.merge(second);
                    numClusters--;
                }
            }

            double minDistance = Double.MAX_VALUE;
            for (Node node : nodes) {
                for (Node other : nodes) {
                    DisjointSet setNode = setMap.get(node.index);
                    DisjointSet setOther = setMap.get(other.index);
                    DisjointSet nodeParent = setNode.getParent();
                    DisjointSet otherParent = setOther.getParent();
                    if (!nodeParent.equals(otherParent)) {
                        minDistance = Math.min(minDistance, node.distanceTo(other));
                    }
                }

            }
            return minDistance;
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
        System.out.println(String.format("%.7f", graph.modifiedKruskal(scanner.nextInt())));
    }

}
