import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class RopeProblem {

    private static class FastScanner {
        private BufferedReader br;
        private StringTokenizer st;

        FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    static class Node {

        int key;
        int size;
        char character;

        Node parent;
        Node leftChild;
        Node rightChild;

        Node(int key, char character) {
            this.key = key;
            this.character = character;
        }

        void setLeftChild(Node node) {
            leftChild = node;
            if (node != null) {
                node.parent = this;
            }
        }

        void setRightChild(Node node) {
            rightChild = node;
            if (node != null) {
                node.parent = this;
            }
        }

        void updateSize() {
            int sizeLeft = leftChild == null ? 0 : leftChild.size;
            int sizeRight = rightChild == null ? 0 : rightChild.size;
            size = sizeLeft + sizeRight + 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.key == ((Node)obj).key;
        }
    }

    static class SplayTree {

        private interface SplayStrategy {
            boolean splayIfApplicable(Node node);

            default void updateGreatGrandparent(Node node, Node grandparent, Node greatGrandparent) {
                if (greatGrandparent != null) {
                    if (grandparent.equals(greatGrandparent.leftChild)) {
                        greatGrandparent.setLeftChild(node);
                    } else {
                        greatGrandparent.setRightChild(node);
                    }
                } else {
                    node.parent = null;
                }
            }
        }

        private static class ZigZigStrategy implements  SplayStrategy {

            private boolean isLeftCase(Node node) {
                return node.equals(node.parent.leftChild) && node.parent.equals(node.parent.parent.leftChild);
            }

            private boolean isRightCase(Node node) {
                return node.equals(node.parent.rightChild) && node.parent.equals(node.parent.parent.rightChild);
            }

            @Override
            public boolean splayIfApplicable(Node node) {
                Node parent = node.parent;
                Node grandparent = parent == null ? null : parent.parent;
                if (parent == null || grandparent == null) {
                    return false;
                }

                Node greatGrandparent = grandparent.parent;

                if (isLeftCase(node)) {
                    Node oldRight = node.rightChild;
                    Node oldParentRight = parent.rightChild;
                    node.setRightChild(parent);
                    parent.setLeftChild(oldRight);
                    parent.setRightChild(grandparent);
                    grandparent.setLeftChild(oldParentRight);
                } else if (isRightCase(node)) {
                    Node oldLeft = node.leftChild;
                    Node oldParentLeft = parent.leftChild;
                    node.setLeftChild(parent);
                    parent.setRightChild(oldLeft);
                    parent.setLeftChild(grandparent);
                    grandparent.setRightChild(oldParentLeft);
                } else {
                    return false;
                }

                updateGreatGrandparent(node, grandparent, greatGrandparent);

                grandparent.updateSize();
                parent.updateSize();
                return true;
            }
        }

        private static class ZigZagStrategy implements  SplayStrategy {

            private boolean isLessThanCase(Node node) {
                return node.equals(node.parent.rightChild) && node.parent.equals(node.parent.parent.leftChild);
            }

            private boolean isGreaterThanCase(Node node) {
                return node.equals(node.parent.leftChild) && node.parent.equals(node.parent.parent.rightChild);
            }

            @Override
            public boolean splayIfApplicable(Node node) {
                Node parent = node.parent;
                Node grandparent = parent == null ? null : parent.parent;
                if (parent == null || grandparent == null) {
                    return false;
                }

                Node greatGrandparent = grandparent.parent;

                Node oldRight = node.rightChild;
                Node oldLeft = node.leftChild;

                if (isLessThanCase(node)) {
                    node.setRightChild(grandparent);
                    node.setLeftChild(parent);
                    parent.setRightChild(oldLeft);
                    grandparent.setLeftChild(oldRight);
                } else if (isGreaterThanCase(node)) {
                    node.setLeftChild(grandparent);
                    node.setRightChild(parent);
                    parent.setLeftChild(oldRight);
                    grandparent.setRightChild(oldLeft);
                } else {
                    return false;
                }

                updateGreatGrandparent(node, grandparent, greatGrandparent);

                parent.updateSize();
                grandparent.updateSize();
                return true;
            }
        }

        private static class ZigStrategy implements  SplayStrategy {

            @Override
            public boolean splayIfApplicable(Node node) {
                Node parent = node.parent;
                if (parent == null || parent.parent != null) {
                    return false;
                }

                node.parent = null;

                if (node.equals(parent.leftChild)) {
                    Node oldRight = node.rightChild;
                    node.setRightChild(parent);
                    parent.setLeftChild(oldRight);
                } else {
                    Node oldLeft = node.leftChild;
                    node.setLeftChild(parent);
                    parent.setRightChild(oldLeft);
                }

                parent.updateSize();
                return true;
            }
        }

        static final SplayStrategy[] STRATEGIES = new SplayStrategy[]{new ZigStrategy(), new ZigZagStrategy(), new ZigZigStrategy()};

        Node root = null;

        private Node find(int orderNumber, boolean mustSplay) {
            int currentNumber = orderNumber;
            Node current = root;
            while (current != null) {
                int leftSize = current.leftChild == null ? 0 : current.leftChild.size;
                if (currentNumber == leftSize + 1) {
                    break;
                } else if (currentNumber > leftSize + 1) {
                    current = current.rightChild;
                    currentNumber = currentNumber - leftSize - 1;
                } else {
                    current = current.leftChild;
                }

            }
            if (mustSplay && current != null) {
                splay(current);
            }
            return current;
        }

        void insert(int key, char character) {
            if (root == null) {
                root = new Node(key, character);
            } else {
                Node found = find(key, false);
                if (found.key != key) {
                    Node node = new Node(key, character);
                    if (found.key > key) {
                        found.setLeftChild(node);
                    } else {
                        found.setRightChild(node);
                    }
                    splay(node);
                }
            }
        }

        int size() {
            return root == null ? 0 : root.size;
        }

        String convertToString() {
            Map<Integer,Node> previousNodes = new HashMap<>();
            StringBuilder builder =  new StringBuilder();

            for (Node current = root; current != null && builder.length() != size();) {
                if (shouldProcessLeftChild(current, previousNodes.get(current.key))) {
                    previousNodes.put(current.key, current.leftChild);
                    current = current.leftChild;
                } else if (shouldProcessCurrent(current, previousNodes.get(current.key))) {
                    builder.append(current.character);
                    previousNodes.put(current.key, current);
                } else if (shouldProcessRightChild(current, previousNodes.get(current.key))) {
                    previousNodes.put(current.key, current.rightChild);
                    current = current.rightChild;
                } else {
                    current = current.parent;
                }
            }
            return builder.toString();
        }

        private boolean shouldProcessLeftChild(Node current, Node previous) {
            return previous == null && current.leftChild != null;
        }

        private boolean shouldProcessCurrent(Node current, Node previous) {
            return (previous == null && current.leftChild == null) ||
                (previous != null && previous.equals(current.leftChild));
        }

        private boolean shouldProcessRightChild(Node current, Node previous) {
            return current.equals(previous) && current.rightChild != null;
        }

        private void splay(Node node) {
            while (node.parent != null) {
                for (int index = 0; index < STRATEGIES.length && !STRATEGIES[index].splayIfApplicable(node); index++);
            }
            node.updateSize();
            root = node;
        }

        SplayTree[] split(int orderNumber, boolean inclusiveInSecond) {
            Node node = find(orderNumber, true);
            if (node != null) {
                if (inclusiveInSecond) {
                    return cutLeft(node);
                } else {
                    return cutRight(node);
                }
            }
            return new SplayTree[] {new SplayTree(), new SplayTree()};
        }

        private SplayTree[] cutLeft(Node node) {
            Node leftChild = node.leftChild;
            node.setLeftChild(null);
            node.updateSize();
            if (leftChild != null) {
                leftChild.parent = null;
            }
            SplayTree first = new SplayTree();
            first.root = leftChild;
            return new SplayTree[]{ first, this };
        }

        private SplayTree[] cutRight(Node node) {
            Node rightChild = node.rightChild;
            node.setRightChild(null);
            node.updateSize();
            if (rightChild != null) {
                rightChild.parent = null;
            }
            SplayTree second = new SplayTree();
            second.root = rightChild;
            return new SplayTree[]{ this, second };
        }

        void merge(SplayTree other) {
            if (root == null) {
                root = other.root;
            } else {
                Node greatest = find(size(), true);
                greatest.setRightChild(other.root);
                greatest.updateSize();
                other.root = root;
            }
        }
    }

    static class Rope {

        private SplayTree tree;

        Rope(String text) {
            tree = new SplayTree();
            for (int index = 0; index < text.length(); index++) {
                tree.insert(index, text.charAt(index));
            }
        }

        void processChange(int lowerIndex, int higherIndex, int k) {
            SplayTree[] firstSplit = tree.split(lowerIndex + 1, true);
            SplayTree[] secondSplit = firstSplit[1].split(higherIndex + 1 - firstSplit[0].size(), false);

            SplayTree movingPart = secondSplit[0];

            SplayTree remaining = firstSplit[0];
            remaining.merge(secondSplit[1]);

            SplayTree result;
            if (k > 0) {
                SplayTree[] finalSplit = remaining.split(k, false);
                result = finalSplit[0];
                movingPart.merge(finalSplit[1]);
                result.merge(movingPart);
            } else {
                result = movingPart;
                result.merge(remaining);
            }
            tree = result;
        }

        String getText() {
            return tree.convertToString();
        }
    }

    public static void main(String[] args) {
        FastScanner in = new FastScanner();

        String text = in.next();
        int queryCount = in.nextInt();

        Rope rope = new RopeProblem.Rope(text);

        for (int index = 0; index < queryCount; index++) {
            rope.processChange(in.nextInt(), in.nextInt(), in.nextInt());
        }

        System.out.println(rope.getText());
    }


}
