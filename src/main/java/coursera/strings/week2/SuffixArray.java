package coursera.strings.week2;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class SuffixArray {

    static class Edge {
        Node target;
        int startIndex;
        int suffixStartIndex;
        int length;
        boolean visited;

        Edge(Node target, int startIndex, int suffixStartIndex, int length) {
            this.target = target;
            this.startIndex = startIndex;
            this.length = length;
            this.suffixStartIndex = suffixStartIndex;
        }
    }

    static class Node {
        Map<Character, Edge> outgoing;

        Node() {
            outgoing = new HashMap<>();
        }
    }

    static class SuffixTree {

        Node root;
        String text;

        SuffixTree(String text) {
            this.text = text;
            root = new Node();

            for (int startIndex = 0; startIndex < text.length(); startIndex++) {
                createEdgesIfRequired(startIndex);
            }
        }

        private void createEdgesIfRequired(int startIndex) {
            Node current = root;
            int suffixCharIndex = startIndex;

            while (current != null) {
                Edge edge = current.outgoing.get(text.charAt(suffixCharIndex));
                if (edge != null) {
                    int edgeCharIndex = edge.startIndex + 1;
                    int edgeEndIndex = edge.startIndex + edge.length - 1;
                    suffixCharIndex++;
                    while (edgeCharIndex <= edgeEndIndex && suffixCharIndex < text.length()
                        && text.charAt(suffixCharIndex) == text.charAt(edgeCharIndex)) {
                        edgeCharIndex++;
                        suffixCharIndex++;
                    }
                    if (edgeCharIndex > edgeEndIndex) {
                        if (suffixCharIndex < text.length()) {
                            current = edge.target;
                        } else {
                            return;
                        }
                    } else {
                        Edge newEdge = new Edge(new Node(), edge.startIndex, startIndex, edgeCharIndex - edge.startIndex);
                        current.outgoing.put(text.charAt(newEdge.startIndex), newEdge);

                        edge.startIndex = edgeCharIndex;
                        edge.length = edge.length - newEdge.length;
                        newEdge.target.outgoing.put(text.charAt(edgeCharIndex), edge);

                        newEdge.target.outgoing.put(text.charAt(suffixCharIndex),
                            new Edge(new Node(), suffixCharIndex, startIndex, text.length() - suffixCharIndex));
                        return;
                    }
                } else {
                    current.outgoing.put(text.charAt(suffixCharIndex),
                        new Edge(new Node(), suffixCharIndex, startIndex, text.length() - suffixCharIndex));
                    return;
                }
            }
        }

        void printAllLeafs() {
            Stack<Edge> edgeStack = new Stack<>();
            addEdgesInOrder(edgeStack, root);

            StringBuilder builder = new StringBuilder();
            while (!edgeStack.isEmpty()) {
                Edge edge = edgeStack.pop();
                if (edge.target.outgoing.size() == 0 || edge.visited) {
                    edge.visited = true;
                    builder.append(edge.suffixStartIndex).append(" ");
                } else {
                    edge.visited = true;
                    addEdgesInOrder(edgeStack, edge.target);
                }
            }
            System.out.println(builder.toString());
        }

        private void addEdgesInOrder(Stack<Edge> edgeStack, Node node) {
            Edge edge = node.outgoing.get('T');
            if (edge != null && !edge.visited) {
                edgeStack.push(edge);
            }
            edge = node.outgoing.get('G');
            if (edge != null && !edge.visited) {
                edgeStack.push(edge);
            }
            edge = node.outgoing.get('C');
            if (edge != null && !edge.visited) {
                edgeStack.push(edge);
            }
            edge = node.outgoing.get('A');
            if (edge != null && !edge.visited) {
                edgeStack.push(edge);
            }
            edge = node.outgoing.get('$');
            if (edge != null && !edge.visited) {
                edgeStack.push(edge);
            }
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new SuffixTree(scanner.next()).printAllLeafs();
        }
    }
}
