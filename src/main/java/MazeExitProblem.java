import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MazeExitProblem {

    private static class Node {
        List<Node> neighbours;
        int value;
        boolean visited;

        Node(int value) {
            this.value = value;

            neighbours = new ArrayList<>();
            visited = false;
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

        boolean canReach(Node other) {
            if (this.equals(other)) {
                return true;
            }

            for (Node neighbour : neighbours) {
                if (!neighbour.visited) {
                    neighbour.visited = true;
                    if (neighbour.canReach(other)) {
                        return true;
                    }
                }
            }
            return false;
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
        int fromIndex = scanner.nextInt() - 1;
        int toIndex = scanner.nextInt() - 1;

        System.out.println(graph.getNode(fromIndex).canReach(graph.getNode(toIndex)) ? "1" : 0);
    }

}
