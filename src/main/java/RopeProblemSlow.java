import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class RopeProblemSlow {

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

        Node parent;
        Node leftChild;
        Node rightChild;

        Node(int key) {
            this.key = key;
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

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.key == ((Node)obj).key;
        }
    }

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

            return true;
        }
    }

    static class SplayTree {
        static final SplayStrategy[] STRATEGIES = new SplayStrategy[]{new ZigStrategy(), new ZigZagStrategy(), new ZigZigStrategy()};

        Node root = null;

        private Node find(int key, boolean mustSplay) {
            Node current = root;
            Node parent = null;
            while (current != null && current.key != key) {
                parent = current;
                if (key > current.key) {
                    current = current.rightChild;
                } else {
                    current = current.leftChild;
                }
            }

            Node node = current == null ? parent : current;
            if (mustSplay && node != null) {
                splay(node);
            }
            return node;
        }

        void insert(int key) {
            if (root == null) {
                root = new Node(key);
            } else {
                Node found = find(key, false);
                if (found.key != key) {
                    Node node = new Node(key);
                    if (found.key > key) {
                        found.setLeftChild(node);
                    } else {
                        found.setRightChild(node);
                    }
                    splay(node);
                }
            }
        }

        private void splay(Node node) {
            while (node.parent != null) {
                for (int index = 0; index < STRATEGIES.length && !STRATEGIES[index].splayIfApplicable(node); index++);
            }
            root = node;
        }

        private SplayTree[] split(int key, boolean inclusiveInSecond) {
            Node node = find(key, true);
            if (node != null) {
                if (node.key > key) {
                    return cutLeft(node);
                } else if (node.key < key) {
                    return cutRight(node);
                } else {
                    if (inclusiveInSecond) {
                        return cutLeft(node);
                    } else {
                        return cutRight(node);
                    }
                }
            }
            return new SplayTree[] {new SplayTree(), new SplayTree()};
        }

        private SplayTree[] cutLeft(Node node) {
            Node leftChild = node.leftChild;
            node.setLeftChild(null);
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
            if (rightChild != null) {
                rightChild.parent = null;
            }
            SplayTree second = new SplayTree();
            second.root = rightChild;
            return new SplayTree[]{ this, second };
        }

        private void merge(SplayTree other) {
            if (root == null) {
                root = other.root;
            } else {
                Node greatest = find(Integer.MAX_VALUE, true);
                greatest.setRightChild(other.root);
                other.root = root;
            }
        }
    }

    static class Rope {

        String text;

        Rope(String text) {
            this.text = text;
        }

        void processChange(int lowerIndex, int higherIndex, int k) {
            SplayTree tree = new SplayTree();
            for (int index = 0; index < text.length(); index++) {
                tree.insert(index);
            }
            SplayTree[] firstSplit = tree.split(lowerIndex, true);
            SplayTree[] secondSplit = firstSplit[1].split(higherIndex, false);

            SplayTree movingPart = secondSplit[0];

            SplayTree remaining = firstSplit[0];
            remaining.merge(secondSplit[1]);

            SplayTree result;
            if (k > 0) {
                SplayTree[] finalSplit = remaining.split(findKthElement(remaining, k).key, false);
                result = finalSplit[0];
                movingPart.merge(finalSplit[1]);
                result.merge(movingPart);
            } else {
                result = movingPart;
                result.merge(remaining);
            }
            text = convertToString(result);
        }

        Node findKthElement(SplayTree tree, int k) {
            int elementCount = 0;
            Node last = null;
            Node[] previousNodes = new Node[text.length()];

            for (Node current = tree.root; elementCount < k && elementCount < text.length();) {
                if (previousNodes[current.key] == null && current.leftChild != null) {
                    previousNodes[current.key] = current.leftChild;
                    current = current.leftChild;
                } else if ((previousNodes[current.key] == null && current.leftChild == null)
                    || (previousNodes[current.key].equals(current.leftChild))) {
                    elementCount++;
                    last = current;
                    previousNodes[current.key] = current;
                } else if (current.equals(previousNodes[current.key]) && current.rightChild != null) {
                    previousNodes[current.key] = current.rightChild;
                    current = current.rightChild;
                } else {
                    current = current.parent;
                }
            }

            if (elementCount == k && elementCount < text.length()) {
                return last;
            }
            return null;
        }

        private String convertToString(SplayTree tree) {
            StringBuilder builder = new StringBuilder();
            Node[] previousNodes = new Node[text.length()];

            for (Node current = tree.root; builder.length() != text.length();) {
                if (previousNodes[current.key] == null && current.leftChild != null) {
                    previousNodes[current.key] = current.leftChild;
                    current = current.leftChild;
                } else if ((previousNodes[current.key] == null && current.leftChild == null)
                    || (previousNodes[current.key].equals(current.leftChild))) {
                    builder.append(text.charAt(current.key));
                    previousNodes[current.key] = current;
                } else if (current.equals(previousNodes[current.key]) && current.rightChild != null) {
                    previousNodes[current.key] = current.rightChild;
                    current = current.rightChild;
                } else {
                    current = current.parent;
                }
            }
            return builder.toString();
        }

    }

    public static void main(String[] args) {
        FastScanner in = new FastScanner();

        String text = in.next();
        int queryCount = in.nextInt();

        Rope rope = new Rope(text);

        for (int index = 0; index < queryCount; index++) {
            rope.processChange(in.nextInt(), in.nextInt(), in.nextInt());
        }

        Stream.of(rope.text).forEach(System.out::println);
    }


}
