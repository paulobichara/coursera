import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TopologicalSort {
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

    static class DirectedGraph {

        Node[] nodes;
        int lastVisitedIndex;

        DirectedGraph(int qtyNodes) {
            nodes  = new Node[qtyNodes];
            for (int index = 0; index < qtyNodes; index++) {
                nodes[index] = new Node(index);
            }
        }

        List<Integer> getInReverseOrder() {
            Clock clock = new Clock();
            Node current = getNextUnvisitedSource();
            Integer[] previousNodeIds = new Integer[nodes.length];
            List<Integer> inOrder = new ArrayList<>(nodes.length);

            while (current != null) {
                if (current.preOrder == null) {
                    current.preOrder = clock.ticks;
                    clock.ticks++;
                }
                Node next = current.destinations.stream().filter(node -> node.preOrder == null).findFirst().orElse(null);
                if (next == null) {
                    current.postOrder = clock.ticks;
                    clock.ticks++;
                    inOrder.add(current.id + 1);
                    current = previousNodeIds[current.id] == null ? getNextUnvisitedSource() : nodes[previousNodeIds[current.id]];
                } else {
                    previousNodeIds[next.id] = current.id;
                    current = next;
                }
            }
            return inOrder;
        }

        private Node getNextUnvisitedSource() {
            Node current;
            for (int index = lastVisitedIndex; index < nodes.length; index++) {
                current = getNode(index);
                if (current.preOrder == null) {
                    lastVisitedIndex = index + 1;
                    return current;
                }
            }
            return null;
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
        List<Integer> inOrderReverse = graph.getInReverseOrder();
        for (int index = inOrderReverse.size() - 1; index >= 0; index--) {
            System.out.print(inOrderReverse.get(index) + " ");
        }
    }
}
