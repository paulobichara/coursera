import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class TrieConstruction {

    private static class Node {
        private static volatile AtomicInteger ID = new AtomicInteger(0);

        Map<Character, Node> outgoing;
        int id;

        Node() {
            outgoing = new HashMap<>();
            id = ID.getAndIncrement();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.id == ((Node)obj).id;
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

        private static final String ARROW = "->";

        void printAllEdges() {
            Stack<Node> nodeStack = new Stack<>();
            nodeStack.push(root);

            while (!nodeStack.isEmpty()) {
                Node node = nodeStack.pop();
                for (Entry<Character, Node> entry : node.outgoing.entrySet()) {
                    System.out.println(node.id + ARROW + entry.getValue().id + ":" + entry.getKey());
                    nodeStack.push(entry.getValue());
                }
            }
        }

    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int qtyPatterns = scanner.nextInt();
            String[] patterns = new String[qtyPatterns];

            for (int index = 0; index < qtyPatterns; index++) {
                patterns[index] = scanner.next();
            }

            Trie trie = new Trie(patterns);
            trie.printAllEdges();
        }
    }

}
