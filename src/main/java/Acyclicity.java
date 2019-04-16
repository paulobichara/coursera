import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Acyclicity {
    private static class Node {
        List<Node> origins;
        List<Node> destinations;
        int id;
        Integer preOrder;
        Integer postOrder;

        Node(int id) {
            origins = new ArrayList<>();
            destinations = new ArrayList<>();
            this.id = id;
        }

        void addDestination(Node node) {
            destinations.add(node);
        }

        void addOrigins(Node node) {
            origins.add(node);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.id == ((Node)obj).id;
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

        void remove(Node node) {
            node.origins.forEach(origin -> origin.destinations.remove(node));
            node.destinations.forEach(destination -> destination.origins.remove(node));
            nodes[node.id] = null;
        }

        boolean hasCycles() {
            int clock = 0;
            Node[] previousNodes = new Node[nodes.length];
            Node last = null;
            for (Node current = getNextUnvisitedNode(); current != null;) {
                if (current.preOrder == null) {
                    current.preOrder = clock;
                    clock++;
                } else if (last != null && last.postOrder == null) {
                    return true;
                }

                last = current;
                if (current.destinations.size() == 0) {
                    current.postOrder = clock;
                    clock++;
                    Node sink = current;
                    current = previousNodes[current.id];
                    remove(sink);
                } else {
                    Node next = current.destinations.stream().filter(node -> node.preOrder == null).findFirst().orElse(null);
                    if (next != null) {
                        previousNodes[next.id] = current;
                        current = next;
                    } else {
                        return true;
                    }
                }

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
            graph.getNode(secondIndex).addOrigins(graph.getNode(firstIndex));
        }

        System.out.println(graph.hasCycles() ? "1" : "0");
    }
}
