import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ExtendedTrieMatching {
    private static class Node {
        Map<Character, Node> outgoing;
        boolean isPatternEnd;

        Node() {
            outgoing = new HashMap<>();
            isPatternEnd = false;
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
                current.isPatternEnd = true;
            }
        }

        void matches(String text) {
            Map<Integer, Boolean> countByStart = new HashMap<>();
            StringBuilder builder = new StringBuilder();

            for (int index = 0; index < text.length(); index++) {
                char currentSymbol = text.charAt(index);
                Node currentNode = root;

                for (int currentIndex = index; currentIndex < text.length() && !currentNode.isPatternEnd
                    && currentNode.outgoing.containsKey(currentSymbol); currentIndex++) {
                    currentNode = currentNode.outgoing.get(currentSymbol);
                    currentSymbol = currentIndex + 1 < text.length() ? text.charAt(currentIndex + 1) : '\u0000';
                }

                if (currentNode.isPatternEnd) {
                    if (!countByStart.containsKey(index)) {
                        builder.append(index).append(" ");
                    }
                    countByStart.put(index, true);
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
