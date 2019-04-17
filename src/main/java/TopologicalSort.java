import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

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

        Clock clock;
        Node[] nodes;
        int lastVisitedIndex;

        DirectedGraph(int qtyNodes) {
            clock = new Clock();
            nodes  = new Node[qtyNodes];
            for (int index = 0; index < qtyNodes; index++) {
                nodes[index] = new Node(index);
            }
        }

        Stack<Integer> getInTopologicalOrder() {
            Node current = getNextUnvisitedSource();
            Integer[] previousNodeIds = new Integer[nodes.length];
            int[] nextDestIds = new int[nodes.length];
            Stack<Integer> inOrder = new Stack<>();

            while (current != null) {
                if (current.preOrder == null) {
                    current.preOrder = clock.ticks;
                    clock.ticks++;
                }

                while (nextDestIds[current.id] < current.destinations.size()
                        && current.destinations.get(nextDestIds[current.id]).preOrder != null) {
                    nextDestIds[current.id]++;
                }

                if (nextDestIds[current.id] < current.destinations.size()) {
                    Node next = current.destinations.get(nextDestIds[current.id]);
                    nextDestIds[current.id]++;
                    previousNodeIds[next.id] = current.id;
                    current = next;
                } else {
                    current.postOrder = clock.ticks;
                    clock.ticks++;
                    inOrder.add(current.id + 1);
                    current = previousNodeIds[current.id] == null ? getNextUnvisitedSource() : nodes[previousNodeIds[current.id]];
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

        Stack<Integer> inOrderStack = graph.getInTopologicalOrder();
        while (!inOrderStack.isEmpty()) {
            System.out.print(inOrderStack.pop() + " ");
        }
    }
}
