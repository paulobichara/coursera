package coursera.datastructures.week4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BinaryTreeCheck {

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

        long nextLong() {
            return Long.parseLong(next());
        }
    }

    private static class Node {
        private int index;
        private Long key;
        private Node leftChild;
        private Node rightChild;

        Node(int index) {
            this.index = index;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.index == ((Node)obj).index;
        }
    }

    private static class InvalidBSTException extends Exception {
        InvalidBSTException() {
            super("This is not a valid binary search tree!");
        }
    }

    private static class BinaryTree {
        Node[] nodes;
        List<Node> leaves;

        BinaryTree(Node[] nodes, List<Node> leaves) {
            this.nodes = nodes;
            this.leaves = leaves;
        }

        Node find(long key) throws InvalidBSTException {
            Node current = getRoot();
            while (current != null && current.key != key) {
                if (!isNodeConsistent(current)) {
                    throw new InvalidBSTException();
                }
                if (key > current.key) {
                    current = current.rightChild;
                } else {
                    current = current.leftChild;
                }
            }
            return current;
        }

        boolean isNodeConsistent(Node node) {
            return (node.leftChild == null || node.leftChild.key < node.key)
                    && (node.rightChild == null || node.rightChild.key > node.key);
        }

        boolean isBinarySearchTree() {
            for (Node leaf : leaves) {
                try {
                    if (find(leaf.key) == null) {
                        return false;
                    }
                } catch (InvalidBSTException e) {
                    return false;
                }
            }
            return true;
        }

        Node getRoot() {
            return nodes[0];
        }
    }

    public static void main(String[] args) {
        FastScanner in = new FastScanner();
        Node[] nodes = new Node[in.nextInt()];
        List<Node> leaves = new ArrayList<>();
        Node current;

        for (int index = 0; index < nodes.length; index++) {
            createNodeIfNeeded(nodes, index);
            current = nodes[index];
            current.key = in.nextLong();

            int leftChildIndex = in.nextInt();
            if (leftChildIndex != -1) {
                createNodeIfNeeded(nodes, leftChildIndex);
                current.leftChild = nodes[leftChildIndex];
            }

            int rightChildIndex = in.nextInt();
            if (rightChildIndex != -1) {
                createNodeIfNeeded(nodes, rightChildIndex);
                current.rightChild = nodes[rightChildIndex];
            }

            if (rightChildIndex == -1 && leftChildIndex == -1) {
                leaves.add(nodes[index]);
            }
        }

        BinaryTree tree = new BinaryTree(nodes, leaves);
        System.out.println(tree.isBinarySearchTree() ? "CORRECT" : "INCORRECT");
    }

    private static void createNodeIfNeeded(Node[] nodes, int index) {
        if (nodes[index] == null) {
            nodes[index] = new Node(index);
        }
    }

}
