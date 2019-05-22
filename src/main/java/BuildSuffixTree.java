import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class BuildSuffixTree {

    static class Edge {
        Node target;
        int startIndex;
        int length;

        Edge(Node target, int startIndex, int length) {
            this.target = target;
            this.startIndex = startIndex;
            this.length = length;
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
                        int oldLength = edge.length;
                        edge.length = edgeCharIndex - edge.startIndex;
                        edge.target.outgoing.put(text.charAt(edgeCharIndex),
                                new Edge(new Node(), edgeCharIndex, oldLength - edge.length));
                        edge.target.outgoing.put(text.charAt(suffixCharIndex),
                                new Edge(new Node(), suffixCharIndex, text.length() - suffixCharIndex));
                        return;
                    }
                } else {
                    current.outgoing.put(text.charAt(suffixCharIndex),
                            new Edge(new Node(), suffixCharIndex, text.length() - suffixCharIndex));
                    return;
                }
            }
        }

        List<String> getAllEdges() {
            Stack<Node> nodeStack = new Stack<>();
            nodeStack.push(root);

            List<String> edges = new ArrayList<>();
            while (!nodeStack.isEmpty()) {
                Node node = nodeStack.pop();
                for (Edge edge : node.outgoing.values()) {
                    StringBuilder builder = new StringBuilder();
                    for (int index = edge.startIndex; index < edge.startIndex + edge.length; index++) {
                        builder.append(text.charAt(index));
                    }
                    edges.add(builder.toString());
                    nodeStack.push(edge.target);
                }
            }
            return edges;
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
