import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.stream.Stream;

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

    private static class Node {

        private long key;
        private long sum;

        private Node parent;
        private Node leftChild;
        private Node rightChild;

        Node(long key) {
            this.key = key;
            this.sum = key;
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

    //TODO optimize the number of comparisons executed by checking and applying the strategy in the same step
    private interface SplayStrategy {
        boolean isApplicable(Node node);

        void splay(Node node);

        default void updateGreatGrandparent(Node node, Node grandparent, Node greatGrandparent) {
            if (greatGrandparent != null) {
                if (greatGrandparent.leftChild.equals(grandparent)) {
                    greatGrandparent.leftChild = node;
                } else {
                    greatGrandparent.rightChild = node;
                }
            }
        }

        default void updateSumInAncestors(Node node) {
            Node current = node;
            while (current != null) {
                current.updateSum();
                current = current.parent;
            }
        }
    }

    private static class ZigZigStrategy implements  SplayStrategy {

        @Override
        public boolean isApplicable(Node node) {
            return isLeftCase(node) || isRightCase(node);
        }

        private boolean isLeftCase(Node node) {
            return node.equals(node.parent.leftChild) && node.parent.equals(node.parent.parent.leftChild);
        }

        private boolean isRightCase(Node node) {
            return node.equals(node.parent.rightChild) && node.parent.equals(node.parent.parent.rightChild);
        }

        @Override
        public void splay(Node node) {
            Node parent = node.parent;
            Node grandparent = parent == null ? null : parent.parent;
            if (parent == null || grandparent == null) {
                return;
            }

            Node greatGrandparent = grandparent.parent;

            if (isLeftCase(node)) {
                Node oldRight = node.rightChild;
                Node oldParentRight = parent.rightChild;
                node.parent = grandparent.parent;
                node.rightChild = parent;
                parent.parent = node;
                parent.leftChild = oldRight;
                parent.rightChild = grandparent;
                grandparent.leftChild = oldParentRight;
                grandparent.parent = parent;
            } else {
                Node oldLeft = node.leftChild;
                Node oldParentLeft = parent.leftChild;
                node.parent = grandparent.parent;
                node.leftChild = parent;
                parent.parent = node;
                parent.rightChild = oldLeft;
                parent.leftChild = grandparent;
                grandparent.rightChild = oldParentLeft;
                grandparent.parent = parent;
            }

            updateGreatGrandparent(node, grandparent, greatGrandparent);
            updateSumInAncestors(grandparent);
        }
    }

    private static class ZigZagStrategy implements  SplayStrategy {

        @Override
        public boolean isApplicable(Node node) {
            return isLessThanCase(node) || isGreaterThanCase(node);
        }

        private boolean isLessThanCase(Node node) {
            return node.equals(node.parent.rightChild) && node.parent.equals(node.parent.parent.leftChild);
        }

        private boolean isGreaterThanCase(Node node) {
            return node.equals(node.parent.leftChild) && node.parent.equals(node.parent.parent.rightChild);
        }

        @Override
        public void splay(Node node) {
            Node parent = node.parent;
            Node grandparent = parent == null ? null : parent.parent;
            if (parent == null || grandparent == null) {
                return;
            }

            Node greatGrandparent = grandparent.parent;

            if (isLessThanCase(node)) {
                Node oldRight = node.rightChild;
                Node oldLeft = node.leftChild;
                node.parent = grandparent.parent;
                node.rightChild = grandparent;
                node.leftChild = parent;
                parent.parent = node;
                parent.rightChild = oldLeft;
                grandparent.parent = node;
                grandparent.leftChild = oldRight;
            } else {
                Node oldRight = node.rightChild;
                Node oldLeft = node.leftChild;
                node.parent = grandparent.parent;
                node.rightChild = parent;
                node.leftChild = grandparent;
                parent.parent = node;
                parent.leftChild = oldRight;
                grandparent.parent = node;
                grandparent.rightChild = oldLeft;
            }

            updateGreatGrandparent(node, grandparent, greatGrandparent);

            parent.updateSum();
            grandparent.updateSum();
            updateSumInAncestors(node);
        }
    }

    private static class ZigStrategy implements  SplayStrategy {

        @Override
        public boolean isApplicable(Node node) {
            return node.parent != null && node.parent.parent == null;
        }

        @Override
        public void splay(Node node) {
            Node parent = node.parent;
            if (parent == null || parent.parent != null) {
                return;
            }

            node.parent = null;

            if (node.equals(parent.leftChild)) {
                Node oldRight = node.rightChild;
                node.rightChild = parent;
                parent.parent = node;
                parent.leftChild = oldRight;
            } else {
                Node oldLeft = node.leftChild;
                node.leftChild = parent;
                parent.parent = node;
                parent.rightChild = oldLeft;
            }

            updateSumInAncestors(parent);
        }
    }

    private static class SplayTree {
        static final SplayStrategy[] STRATEGIES = new SplayStrategy[]{new ZigStrategy(), new ZigZagStrategy(), new ZigZigStrategy()};

        static final long PRIME =  1_000_000_001;

        Node root = null;
        long lastSumValue = 0;

        Node find(long factor) {
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
            if (node != null) {
                splay(node);
            }
            return node;
        }

        void delete(long factor) {
            Node node = find(factor);
            if (node == null || node.key != calculateKey(factor)) {
                return;
            }

            Node next = next(node);
            if (next != null) {
                splay(next);
                splay(node);
            }

            Node leftChild = node.leftChild;
            Node rightChild = node.rightChild;
            if (rightChild != null) {
                rightChild.leftChild = leftChild;
                rightChild.parent = null;
            }
            if (leftChild != null) {
                leftChild.parent = rightChild;
            }

            root = rightChild != null ? rightChild : leftChild;
        }

        void insert(long factor) {
            long key = calculateKey(factor);

            if (root == null) {
                root = new Node(key);
            } else {
                Node found = find(factor);
                if (found.key != key) {
                    Node node = new Node(key);
                    node.parent = found;
                    if (found.key > key) {
                        found.leftChild = node;
                    } else {
                        found.rightChild = node;
                    }
                    find(factor);
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
                Stream.of(STRATEGIES).filter(strategy -> strategy.isApplicable(node)).findFirst()
                        .ifPresent(strategy -> strategy.splay(node));
            }
            root = node;
        }

        private SplayTree[] split(long factor, boolean inclusiveInSecond) {
            long key = calculateKey(factor);
            Node node = find(factor);
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
            node.leftChild = null;
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
            node.rightChild = null;
            if (rightChild != null) {
                rightChild.parent = null;
            }
            SplayTree second = new SplayTree();
            second.lastSumValue = lastSumValue;
            second.root = rightChild;
            return new SplayTree[]{ this, second };
        }

        private void merge(SplayTree other) {
            Node greatest = find(PRIME - 1 - lastSumValue);
            if (greatest == null) {
                root = other.root;
            } else {
                if (other.root != null) {
                    greatest.rightChild = other.root;
                    other.root.parent = greatest;
                }
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
