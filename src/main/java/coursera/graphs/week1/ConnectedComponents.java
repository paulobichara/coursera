package coursera.graphs.week1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class ConnectedComponents {

    private static class Node {
        List<Node> neighbours;
        Integer componentId;
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

        void explore(int componentId) {
            visited = true;
            for (Node neighbour : neighbours) {
                if (!neighbour.visited) {
                    neighbour.visited = true;
                    neighbour.componentId = componentId;
                    neighbour.explore(componentId);
                }
            }
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

        int getConnectedComponentsQty() {
            Node node;
            int componentId = 0;
            while ((node = getNextUnvisitedNode()) != null) {
                componentId++;
                node.explore(componentId);
            }
            return componentId;
        }

        private Node getNextUnvisitedNode() {
            Optional<Node> optional = Stream.of(nodes).filter(node -> !node.visited).findFirst();
            return optional.isPresent() ? optional.get() : null;
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

        System.out.println(graph.getConnectedComponentsQty());
    }

}
