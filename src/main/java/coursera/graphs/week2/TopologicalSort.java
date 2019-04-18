package coursera.graphs.week2;

import java.util.Scanner;
import java.util.Stack;

public class TopologicalSort {
    private static class Clock {
        int ticks;
    }

    private static class Node {
        Stack<Node> unvisited;

        int id;
        Integer preOrder;
        Integer postOrder;

        Node(int id) {
            unvisited = new Stack<>();
            this.id = id;
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

        void explore(Clock clock, Stack<Integer> inOrder) {
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
                    inOrder.push((current.id + 1));
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
                nodes[index] = new Node(index);
            }
        }

        Stack<Integer> getInTopologicalOrder() {
            Stack<Integer> inOrder = new Stack<>();

            for (Node current = getNextUnvisitedSource(); current != null; current = getNextUnvisitedSource()) {
                current.explore(clock, inOrder);
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
