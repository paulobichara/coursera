import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TreeOrders {

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

    private static class BinarySearchTree {
        private Node[] nodes;

        BinarySearchTree(Node[] nodes) {
            this.nodes = nodes;
        }

        List<Integer> listInOrder(Node node, List<Integer> nodeKeys) {
            if (node.leftChild != null) {
                listInOrder(node.leftChild, nodeKeys);
            }

            nodeKeys.add(node.key);

            if (node.rightChild != null) {
                listInOrder(node.rightChild, nodeKeys);
            }
            return nodeKeys;
        }

        List<Integer> listPreOrder(Node node, List<Integer> nodeKeys) {
            nodeKeys.add(node.key);

            if (node.leftChild != null) {
                listPreOrder(node.leftChild, nodeKeys);
            }

            if (node.rightChild != null) {
                listPreOrder(node.rightChild, nodeKeys);
            }
            return nodeKeys;
        }

        List<Integer> listPostOrder(Node node, List<Integer> nodeKeys) {
            if (node.leftChild != null) {
                listPostOrder(node.leftChild, nodeKeys);
            }

            if (node.rightChild != null) {
                listPostOrder(node.rightChild, nodeKeys);
            }
            nodeKeys.add(node.key);
            return nodeKeys;
        }

        Node getRoot() {
            return nodes[0];
        }
    }

    private static class Node {
        private int index;
        private int key;
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

    public static void main(String[] args) {
        FastScanner in = new FastScanner();
        Node[] nodes = new Node[in.nextInt()];
        Node current;

        for (int index = 0; index < nodes.length; index++) {
            if (nodes[index] == null) {
                nodes[index] = new Node(index);
            }
            current = nodes[index];
            current.key = in.nextInt();

            int leftChildIndex = in.nextInt();
            if (leftChildIndex != -1) {
                if (nodes[leftChildIndex] == null) {
                    nodes[leftChildIndex] = new Node(leftChildIndex);
                }
                current.leftChild = nodes[leftChildIndex];
            }

            int rightChildIndex = in.nextInt();
            if (rightChildIndex != -1) {
                if (nodes[rightChildIndex] == null) {
                    nodes[rightChildIndex] = new Node(rightChildIndex);
                }
                current.rightChild = nodes[rightChildIndex];
            }
        }

        BinarySearchTree tree = new BinarySearchTree(nodes);
        tree.listInOrder(tree.getRoot(), new ArrayList<>()).forEach(key -> System.out.print(key + " "));
        System.out.println();
        tree.listPreOrder(tree.getRoot(), new ArrayList<>()).forEach(key -> System.out.print(key + " "));
        System.out.println();
        tree.listPostOrder(tree.getRoot(), new ArrayList<>()).forEach(key -> System.out.print(key + " "));
    }
}
