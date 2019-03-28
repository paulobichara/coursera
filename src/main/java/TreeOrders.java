import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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

    private static class Node {
        private int index;
        private int key;
        private Node leftChild;
        private Node rightChild;
        private Node parent;

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

    private static class BinarySearchTree {
        private Node[] nodes;

        BinarySearchTree(Node[] nodes) {
            this.nodes = nodes;
        }

        List<Integer> listInOrder() {
            List<Integer> inOrderNodes = new Stack<>();
            Node[] previousNodes = new Node[nodes.length];

            for (Node current = getRoot(); inOrderNodes.size() != nodes.length;) {
                if (previousNodes[current.index] == null) {
                    if (current.leftChild != null) {
                        previousNodes[current.index] = current.leftChild;
                        current = current.leftChild;
                    } else {
                        inOrderNodes.add(current.key);
                        if (current.rightChild != null) {
                            previousNodes[current.index] = current.rightChild;
                            current = current.rightChild;
                        } else {
                            previousNodes[current.index] = current;
                            current = current.parent;
                        }
                    }
                } else if (previousNodes[current.index].equals(current.leftChild)) {
                    inOrderNodes.add(current.key);
                    if (current.rightChild != null) {
                        previousNodes[current.index] = current.rightChild;
                        current = current.rightChild;
                    } else {
                        previousNodes[current.index] = current;
                        current = current.parent;
                    }
                } else if (previousNodes[current.index].equals(current.rightChild)) {
                    current = current.parent;
                }
            }
            return inOrderNodes;
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

    public static void main(String[] args) {
        FastScanner in = new FastScanner();
        Node[] nodes = new Node[in.nextInt()];
        Node current;

        for (int index = 0; index < nodes.length; index++) {
            createNodeIfNeeded(nodes, index);
            current = nodes[index];
            current.key = in.nextInt();

            int leftChildIndex = in.nextInt();
            if (leftChildIndex != -1) {
                createNodeIfNeeded(nodes, leftChildIndex);
                current.leftChild = nodes[leftChildIndex];
                nodes[leftChildIndex].parent = current;
            }

            int rightChildIndex = in.nextInt();
            if (rightChildIndex != -1) {
                createNodeIfNeeded(nodes, rightChildIndex);
                current.rightChild = nodes[rightChildIndex];
                nodes[rightChildIndex].parent = current;
            }
        }

        printResponse(new BinarySearchTree(nodes));
    }

    private static void printResponse(BinarySearchTree tree) {
        tree.listInOrder().forEach(key -> System.out.print(key + " "));
        System.out.println();
        tree.listPreOrder(tree.getRoot(), new ArrayList<>()).forEach(key -> System.out.print(key + " "));
        System.out.println();
        tree.listPostOrder(tree.getRoot(), new ArrayList<>()).forEach(key -> System.out.print(key + " "));
    }

    private static void createNodeIfNeeded(Node[] nodes, int index) {
        if (nodes[index] == null) {
            nodes[index] = new Node(index);
        }
    }
}
