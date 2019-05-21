import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class BuildSuffixTree {

    private static class Edge {
        Node target;
        int startIndex;
        int length;

        Edge(Node target, int startIndex, int length) {
            this.target = target;
            this.startIndex = startIndex;
            this.length = length;
        }
    }

    private static class Node {
        Map<Character, Edge> outgoing;

        Node() {
            outgoing = new HashMap<>();
        }
    }

    private static class SuffixTree {

        Node root;
        String text;

        SuffixTree(String text) {
            this.text = text;
            root = new Node();

            for (int index = 0; index < text.length(); index++) {
                Node current = root;
                Edge edge = current.outgoing.get(text.charAt(index));
                if (edge == null) {
                    current.outgoing.put(text.charAt(index), new Edge(new Node(), index, text.length() - index));
                } else {
                    createEdgesIfRequired(index, edge);
                }
            }
        }

        private void createEdgesIfRequired(int startIndex, Edge edge) {
            int length = text.length() - startIndex;
            Edge current = edge;
            int charIndexCurrent = startIndex + 1;
            int charIndexEdge;

            while (current != null) {
                for (charIndexEdge = current.startIndex + 1;
                        charIndexEdge < text.length() && charIndexCurrent < text.length();
                        charIndexEdge++, charIndexCurrent++) {
                    if (text.charAt(charIndexEdge) != text.charAt(charIndexCurrent)) {
                        int oldLength = edge.length;
                        edge.length = charIndexEdge - edge.startIndex;
                        edge.target.outgoing.put(text.charAt(charIndexEdge),
                                new Edge(new Node(), charIndexEdge, oldLength - edge.length));
                        edge.target.outgoing.put(text.charAt(charIndexCurrent),
                                new Edge(new Node(), charIndexCurrent, length - edge.length));
                        return;
                    }
                }

                length = length - (charIndexCurrent - startIndex);

                current = edge.target.outgoing.get(text.charAt(charIndexCurrent));
                if (current == null) {
                    edge.target.outgoing.put(text.charAt(charIndexCurrent),
                        new Edge(new Node(), charIndexCurrent,
                            length - (charIndexCurrent - startIndex)));
                }
            }
        }

        void printAllEdges() {
            Stack<Node> nodeStack = new Stack<>();
            nodeStack.push(root);

            while (!nodeStack.isEmpty()) {
                Node node = nodeStack.pop();
                for (Edge edge : node.outgoing.values()) {
                    for (int index = edge.startIndex; index < edge.startIndex + edge.length; index++) {
                        System.out.print(text.charAt(index));
                    }
                    System.out.println();

                    nodeStack.push(edge.target);
                }
            }
        }

    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new SuffixTree(scanner.next()).printAllEdges();
        }
    }
}
