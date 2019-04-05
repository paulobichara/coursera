import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class RangeSum {

    private static final String NOT_FOUND = "Not found";
    private static final String FOUND = "Found";

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

        long key;
        long sum;

        Node parent;
        Node leftChild;
        Node rightChild;

        Node(long key) {
            this.key = key;
            this.sum = key;
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

        void updateSum() {
            long sumLeft = leftChild == null ? 0 : leftChild.sum;
            long sumRight = rightChild == null ? 0 : rightChild.sum;
            sum = sumLeft + sumRight + key;
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

            grandparent.updateSum();
            parent.updateSum();
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

            parent.updateSum();
            grandparent.updateSum();
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

            parent.updateSum();
            return true;
        }
    }

    static class SplayTree {
        static final SplayStrategy[] STRATEGIES = new SplayStrategy[]{new ZigStrategy(), new ZigZagStrategy(), new ZigZigStrategy()};

        static final long PRIME =  1_000_000_001;

        Node root = null;
        long lastSumValue = 0;

        Node find(long factor) {
            return find(factor, true);
        }

        private Node find(long factor, boolean mustSplay) {
            long key = calculateKey(factor);
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

        void delete(long factor) {
            Node node = find(factor, false);
            if (node == null || node.key != calculateKey(factor)) {
                return;
            }

            Node next = next(node);
            if (next != null) {
                splay(next);
            }

            splay(node);

            Node leftChild = node.leftChild;
            Node rightChild = node.rightChild;
            if (rightChild != null) {
                rightChild.setLeftChild(leftChild);
                rightChild.parent = null;
                rightChild.updateSum();
            } else if (leftChild != null) {
                leftChild.parent = null;
            }

            root = rightChild != null ? rightChild : leftChild;
        }

        void insert(long factor) {
            long key = calculateKey(factor);

            if (root == null) {
                root = new Node(key);
            } else {
                Node found = find(factor, false);
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

        long sum(long lowerFactor, long higherFactor) {
            SplayTree[] splitTreesLower = split(lowerFactor, true);
            SplayTree[] splitTreesHigher = splitTreesLower[1].split(higherFactor, false);

            lastSumValue = splitTreesHigher[0].root != null ? splitTreesHigher[0].root.sum : 0;

            SplayTree higherTree = splitTreesHigher[0];
            higherTree.merge(splitTreesHigher[1]);

            SplayTree resultTree = splitTreesLower[0];
            resultTree.merge(higherTree);

            root = resultTree.root;

            return lastSumValue;
        }

        long calculateKey(long factor) {
            return (factor + lastSumValue) % PRIME;
        }

        private void splay(Node node) {
            while (node.parent != null) {
                for (int index = 0; index < STRATEGIES.length && !STRATEGIES[index].splayIfApplicable(node); index++);
            }
            node.updateSum();
            root = node;
        }

        private SplayTree[] split(long factor, boolean inclusiveInSecond) {
            long key = calculateKey(factor);
            Node node = find(factor, true);
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
            node.updateSum();
            if (leftChild != null) {
                leftChild.parent = null;
            }
            SplayTree first = new SplayTree();
            first.lastSumValue = lastSumValue;
            first.root = leftChild;
            return new SplayTree[]{ first, this };
        }

        private SplayTree[] cutRight(Node node) {
            Node rightChild = node.rightChild;
            node.setRightChild(null);
            node.updateSum();
            if (rightChild != null) {
                rightChild.parent = null;
            }
            SplayTree second = new SplayTree();
            second.lastSumValue = lastSumValue;
            second.root = rightChild;
            return new SplayTree[]{ this, second };
        }

        private void merge(SplayTree other) {
            if (root == null) {
                root = other.root;
            } else {
                Node greatest = find(PRIME - 1 - lastSumValue, true);
                greatest.setRightChild(other.root);
                greatest.updateSum();
                other.root = root;
            }
        }

        private Node next(Node node) {
            if (node.rightChild != null) {
                // return the left descendant of the right child
                Node current = node.rightChild;
                while (current.leftChild != null) {
                    current = current.leftChild;
                }
                return current;
            } else {
                // return the right ancestor of the node
                Node current = node;
                Node lastChild = null;
                while (current.parent != null && current.parent.key < current.key) {
                    lastChild = current;
                    current = current.parent;
                }

                return lastChild != null && current.key > lastChild.key ? current : null;
            }
        }
    }

    public static void main(String[] args) {
        FastScanner in = new FastScanner();
        int queryCount = in.nextInt();
        SplayTree tree = new SplayTree();

        for (int index = 0; index < queryCount; index++) {
            switch (in.next()) {
                case "+":
                    tree.insert(in.nextInt());
                    break;
                case "-":
                    tree.delete(in.nextInt());
                    break;
                case "?":
                    int factor = in.nextInt();
                    Node found = tree.find(factor);
                    System.out.println(found == null || found.key != tree.calculateKey(factor) ? NOT_FOUND : FOUND);
                    break;
                case "s":
                    System.out.println(tree.sum(in.nextInt(), in.nextInt()));
                    break;

            }
        }
    }

}
