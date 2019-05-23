import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TrieMatching {
    private static class Node {
        Map<Character, Node> outgoing;

        Node() {
            outgoing = new HashMap<>();
        }
    }

    private static class Trie {

        Node root;

        Trie(String[] patterns) {
            root = new Node();
            for (String pattern : patterns) {
                Node current = root;
                for (int index = 0; index < pattern.length(); index++) {
                    char currentSymbol = pattern.charAt(index);
                    if (current.outgoing.containsKey(currentSymbol)) {
                        current = current.outgoing.get(currentSymbol);
                    } else {
                        Node node = new Node();
                        current.outgoing.put(currentSymbol, node);
                        current = node;
                    }
                }
            }
        }

        void matches(String text) {
            StringBuilder builder = new StringBuilder();
            for (int index = 0; index < text.length(); index++) {
                char currentSymbol = text.charAt(index);
                Node currentNode = root;

                for (int currentIndex = index; currentIndex < text.length() && !currentNode.outgoing.isEmpty()
                        && currentNode.outgoing.containsKey(currentSymbol); currentIndex++) {
                    currentNode = currentNode.outgoing.get(currentSymbol);
                    currentSymbol = currentIndex + 1 < text.length() ? text.charAt(currentIndex + 1) : '\u0000';
                }

                if (currentNode.outgoing.isEmpty()) {
                    builder.append(index).append(" ");
                }
            }
            System.out.println(builder.toString());
        }

    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String text = scanner.next();

            int qtyPatterns = scanner.nextInt();
            String[] patterns = new String[qtyPatterns];

            for (int index = 0; index < qtyPatterns; index++) {
                patterns[index] = scanner.next();
            }

            new Trie(patterns).matches(text);
        }
    }
}
