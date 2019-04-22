import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Bipartite {

    private static class Node {
        List<Node> neighbours;
        int value;

        Node(int value) {
            this.value = value;
            neighbours = new ArrayList<>();
        }

        void addNeighbour(Node node) {
            neighbours.add(node);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.value == ((Node)obj).value;
        }
    }

    private static class Graph {
        private Node[] nodes;

        Graph(int qtyNodes) {
            nodes = new Node[qtyNodes];
            for (int index = 0; index < qtyNodes; index++) {
                nodes[index] = new Node(index);
            }
        }

        Node getNode(int index) {
            if (nodes[index] == null) {
                nodes[index] = new Node(index);
            }
            return nodes[index];
        }

        boolean isBipartite() {
            int[] distances = new int[nodes.length];

            Arrays.fill(distances, Integer.MAX_VALUE);
            distances[0] = 0;

            Queue<Integer> queue = new LinkedList<>();
            queue.add(0);
            while (!queue.isEmpty()) {
                Node current = nodes[queue.poll()];
                for (Node neighbour : current.neighbours) {
                    if (distances[neighbour.value] == Integer.MAX_VALUE) {
                        queue.add(neighbour.value);
                        distances[neighbour.value] = distances[current.value] + 1;
                    } else if (distances[neighbour.value] == distances[current.value]) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph graph = new Graph(scanner.nextInt());

        int qtyEdges = scanner.nextInt();
        for (int i = 0; i < qtyEdges; i++) {
            int firstIndex = scanner.nextInt() - 1;
            int secondIndex = scanner.nextInt() - 1;
            graph.getNode(firstIndex).addNeighbour(graph.getNode(secondIndex));
            graph.getNode(secondIndex).addNeighbour(graph.getNode(firstIndex));
        }

        System.out.println(graph.isBipartite() ? "1" : "0");
    }

}
