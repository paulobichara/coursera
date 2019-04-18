package coursera.graphs.week2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Acyclicity {
    private static class Clock {
        int ticks;
    }

    private static class CyclicGraphException extends RuntimeException {}

    private static class Node {
        List<Node> destinations;
        int id;
        Integer preOrder;
        Integer postOrder;

        Node(int id) {
            destinations = new ArrayList<>();
            this.id = id;
        }

        void addDestination(Node node) {
            destinations.add(node);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.id == ((Node)obj).id;
        }

        void explore(Clock clock) {
            if (preOrder != null) {
                throw new CyclicGraphException();
            }

            preOrder = clock.ticks;
            clock.ticks++;
            for (Node neighbour : destinations) {
                if (neighbour.preOrder == null) {
                    neighbour.explore(clock);
                } else if (neighbour.postOrder == null) {
                    throw new CyclicGraphException();
                }
            }
            postOrder = clock.ticks;
            clock.ticks++;
        }
    }

    private static class DirectedGraph {

        Node[] nodes;

        DirectedGraph(int qtyNodes) {
            nodes  = new Node[qtyNodes];
            for (int index = 0; index < qtyNodes; index++) {
                nodes[index] = new Node(index);
            }
        }

        boolean hasCycles() {
            Clock clock = new Clock();
            try {
                for (Node current = getNextUnvisitedNode(); current != null; current = getNextUnvisitedNode()) {
                    current.explore(clock);
                }
            } catch (CyclicGraphException e) {
                return true;
            }
            return false;
        }

        private Node getNextUnvisitedNode() {
            return Stream.of(nodes).filter(node -> node.preOrder == null).findFirst().orElse(null);
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
            graph.getNode(firstIndex).addDestination(graph.getNode(secondIndex));
        }

        System.out.println(graph.hasCycles() ? "1" : "0");
    }
}
