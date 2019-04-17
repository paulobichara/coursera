import java.util.Scanner;
import java.util.Stack;

public class StronglyConnected {

    private static class Clock {
        int ticks;
    }

    private static class Node {
        Clock clock;
        Stack<Node> unvisited;

        int id;
        Integer preOrder;
        Integer postOrder;

        Node(int id, Clock clock) {
            unvisited = new Stack<>();
            this.id = id;
            this.clock = clock;
        }

        void addDestination(Node node) {
            unvisited.push(node);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.id == ((Node)obj).id;
        }

        void explore() {
            explore(null);
        }

        void explore(Stack<Integer> inOrder) {
            Node current = this;
            Stack<Node> previousNodes = new Stack<>();

            while (current != null) {
                if (current.preOrder == null) {
                    current.preOrder = clock.ticks;
                    clock.ticks++;
                }

                Node next = current.unvisited.isEmpty() ? null : current.unvisited.pop();
                while (!current.unvisited.isEmpty() && next.preOrder != null) {
                    next = current.unvisited.pop();
                }

                if (next != null && next.preOrder == null) {
                    previousNodes.push(current);
                    current = next;
                } else {
                    current.postOrder = clock.ticks;
                    clock.ticks++;
                    if (inOrder != null) {
                        inOrder.push((current.id));
                    }
                    current = previousNodes.isEmpty() ? null : previousNodes.pop();
                }
            }
        }
    }

    static class DirectedGraph {

        Clock clock;
        Node[] nodes;

        Stack<Integer> inOrder;
        int lastVisitedIndex;

        DirectedGraph(int qtyNodes) {
            clock = new Clock();
            nodes  = new Node[qtyNodes];
            inOrder = new Stack<>();
            for (int index = 0; index < qtyNodes; index++) {
                nodes[index] = new Node(index, clock);
            }
        }

        void addEdge(int fromIndex, int toIndex) {
            nodes[fromIndex].addDestination(nodes[toIndex]);
        }

        Stack<Integer> getInTopologicalOrder() {
            Stack<Integer> inOrder = new Stack<>();

            for (Node current = getNextUnvisitedSource(); current != null; current = getNextUnvisitedSource()) {
                current.explore(inOrder);
            }

            return inOrder;
        }

        private Node getNextUnvisitedSource() {
            Node current;
            for (int index = lastVisitedIndex; index < nodes.length; index++) {
                current = nodes[index];
                if (current.preOrder == null) {
                    lastVisitedIndex = index + 1;
                    return current;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int nodeQty = scanner.nextInt();
        DirectedGraph graph = new DirectedGraph(nodeQty);
        DirectedGraph reverse = new DirectedGraph(nodeQty);

        int qtyEdges = scanner.nextInt();
        for (int i = 0; i < qtyEdges; i++) {
            int firstIndex = scanner.nextInt() - 1;
            int secondIndex = scanner.nextInt() - 1;
            graph.addEdge(firstIndex, secondIndex);
            reverse.addEdge(secondIndex, firstIndex);
        }

        Stack<Integer> inOrderStack = reverse.getInTopologicalOrder();
        Node current;
        int countSCCs = 0;
        while (!inOrderStack.isEmpty()) {
            current = graph.nodes[inOrderStack.pop()];
            if (current.preOrder == null) {
                current.explore();
                countSCCs++;
            }
        }
        System.out.println(countSCCs);
    }
}
