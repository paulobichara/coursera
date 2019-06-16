import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class SuffixTreeFromArray {

    private static class SuffixTree {
        private static class Alphabet {
            static final int SIZE = 5;

            static int getKey(char character) {
                switch (character) {
                    case '$': return 0;
                    case 'A': return 1;
                    case 'C': return 2;
                    case 'G': return 3;
                    case 'T': return 4;
                    default: throw new RuntimeException("Unexpected character '" + character + "'");
                }
            }

            static Character getCharacter(int key) {
                switch (key) {
                    case 0: return '$';
                    case 1: return 'A';
                    case 2: return 'C';
                    case 3: return 'G';
                    case 4: return 'T';
                    default: return null;
                }
            }
        }

        private static class Node {
            Node parent;
            Map<Character, Node> children;
            int stringDepth;
            Integer edgeStart;
            Integer edgeEnd;

            Node(Node parent, int stringDepth, Integer edgeStart, Integer edgeEnd) {
                this.parent = parent;
                this.children = new HashMap<>();
                this.stringDepth = stringDepth;
                this.edgeStart = edgeStart;
                this.edgeEnd = edgeEnd;
            }
        }

        Node root;
        String text;

        SuffixTree(String text, int[] suffixes, int[] lcpArray) {
            root = new Node(null, 0, null, null);
            this.text = text;

             int lcpPrev = 0;
             Node current = root;
             for (int index = 0; index < text.length(); index++) {
                 int suffixStart = suffixes[index];
                 while (current.stringDepth > lcpPrev) {
                    current = current.parent;
                 }
                 if (current.stringDepth == lcpPrev) {
                    current = createNewLeaf(current, text, suffixStart);
                 } else {
                     int edgeStart = suffixes[index - 1] + current.stringDepth;
                     int offset = lcpPrev - current.stringDepth;
                     Node midNode = breakEdge(current, text, edgeStart, offset);
                     current = createNewLeaf(midNode, text, suffixStart);
                 }
                 if (index < text.length() - 1) {
                     lcpPrev = lcpArray[index];
                 }
             }
        }

        private Node createNewLeaf(Node node, String text, int suffixStart) {
            Node leaf = new Node(node, text.length() - suffixStart,
                    suffixStart + node.stringDepth, text.length() - 1);
            node.children.put(text.charAt(leaf.edgeStart), leaf);
            return leaf;
        }

        private Node breakEdge(Node node, String text, int startIndex, int offset) {
            char startChar = text.charAt(startIndex);
            char midChar = text.charAt(startIndex + offset);
            Node midNode = new Node(node, node.stringDepth + offset, startIndex,
                    startIndex + offset - 1);
            Node child = node.children.get(startChar);
            midNode.children.put(midChar, child);
            child.parent = midNode;
            child.edgeStart += offset;
            node.children.put(startChar, midNode);
            return midNode;
        }

        void printAllEdges() {
            Stack<SuffixTree.Node> nodeStack = new Stack<>();
            Stack<Integer> nextCharKeys = new Stack<>();
            nodeStack.push(root);

            nextCharKeys.push(Alphabet.getKey(getNextChar(root, -1)));

            StringBuilder builder = new StringBuilder(text).append('\n');
            while (!nodeStack.isEmpty()) {
                SuffixTree.Node node = nodeStack.pop();
                Integer charKey = nextCharKeys.pop();
                Character character = Alphabet.getCharacter(charKey);

                if (node.children.size() == 0) {
                    continue;
                }

                Character nextChar = getNextChar(node, charKey);
                if (nextChar != null) {
                    nodeStack.push(node);
                    nextCharKeys.push(Alphabet.getKey(nextChar));
                }

                builder.append(node.children.get(character).edgeStart).append(" ")
                        .append(node.children.get(character).edgeEnd + 1).append('\n');

                if (!node.children.get(character).children.isEmpty()) {
                    nodeStack.push(node.children.get(character));
                    nextCharKeys.push(Alphabet.getKey(getNextChar(node.children.get(character), -1)));
                }
            }
            System.out.print(builder.toString());
        }

        private Character getNextChar(Node node, int currentKey) {
            int key = currentKey + 1;
            while (key < Alphabet.SIZE) {
                if (node.children.get(Alphabet.getCharacter(key)) != null) {
                    return Alphabet.getCharacter(key);
                }
                key++;
            }
            return null;
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String text = scanner.next();

            int[] suffixes = new int[text.length()];
            for (int index = 0; index < text.length(); index++) {
                suffixes[index] = scanner.nextInt();
            }

            int[] lcpArray = new int[suffixes.length - 1];
            for (int index = 0; index < lcpArray.length; index++) {
                lcpArray[index] = scanner.nextInt();
            }

            SuffixTree tree = new SuffixTree(text, suffixes, lcpArray);
            tree.printAllEdges();
        }
    }

}
