import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class TopologicalOrdering {
    private static class Clock {
        int ticks;
    }

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
    }

    private static class ReversePostOrder implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return Integer.compare(o2.postOrder, o1.postOrder);
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

        void orderTopologically() {
            Clock clock = new Clock();
            Node current = getNextUnvisitedNode();
            Integer[] previousNodeIds = new Integer[nodes.length];

            while (current != null) {
                if (current.preOrder == null) {
                    current.preOrder = clock.ticks;
                    clock.ticks++;
                }

                Node next = current.destinations.stream().filter(node -> node.preOrder == null).findFirst().orElse(null);
                if (next == null) {
                    current.postOrder = clock.ticks;
                    clock.ticks++;
                    current = previousNodeIds[current.id] == null ? getNextUnvisitedNode() : nodes[previousNodeIds[current.id]];
                } else {
                    previousNodeIds[next.id] = current.id;
                    current = next;
                }
            }
            Arrays.sort(nodes, new ReversePostOrder());
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
        graph.orderTopologically();
        Stream.of(graph.nodes).forEach(node -> System.out.println((node.id + 1) + " "));
    }
}
