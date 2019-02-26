import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

public class TreeHeight {

	private static class Tree {
		private Node[] nodes;
		private int rootIndex;

		Tree(Node[] nodes, int rootIndex) {
			this.nodes = nodes;
			this.rootIndex = rootIndex;
		}

		Node getRoot() {
			return nodes[rootIndex];
		}
	}

	private static class Node {
		private int index;

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Node)) {
				return false;
			}
			return this.index == ((Node)obj).index;
		}

		private List<Node> children = new ArrayList<>();
		private Node parent;

		Node(int index) {
			this.index = index;
		}
	}

    private static class FastScanner {
		StringTokenizer tok = new StringTokenizer("");
		BufferedReader in;

		FastScanner() {
			in = new BufferedReader(new InputStreamReader(System.in));
		}

		String next() throws IOException {
			while (!tok.hasMoreElements())
				tok = new StringTokenizer(in.readLine());
			return tok.nextToken();
		}
		int nextInt() throws IOException {
			return Integer.parseInt(next());
		}
	}

	static int computeHeight(final int[] parents) {
		Stack<Node> treeStack = new Stack<>();
		Tree tree = createTree(parents);

		addAllChildren(treeStack, tree);

		int maxHeight = 0;
		Node previous = null;

		int height = 0;
		Node current;
		for (current = treeStack.pop(); !treeStack.isEmpty(); current = treeStack.pop()) {
			if (previous == null || parents[current.index] == previous.index) {
				height++;
			} else if (parents[current.index] != previous.index) {
				while (previous != null && parents[previous.index] != parents[current.index]) {
					height--;
					previous = previous.parent;
				}
			}
			previous = current;
			maxHeight = Math.max(maxHeight, height);
		}

		return previous == null || parents[current.index] == previous.index ? Math.max(maxHeight, height + 1) : maxHeight;
	}

	private static void addAllChildren(Stack<Node> treeStack, Tree tree) {
		int[] lastChildIndex = new int[tree.nodes.length];
		Arrays.fill(lastChildIndex, -1);

		for (Node current = tree.getRoot(); treeStack.size() != tree.nodes.length;) {
			int childIndex = lastChildIndex[current.index] + 1;
			if (childIndex < current.children.size()) {
				lastChildIndex[current.index] = childIndex;
				current = current.children.get(childIndex);
			} else {
				treeStack.push(current);
				current = current.equals(tree.getRoot()) ? current : current.parent;
			}
		}
	}

	private static Tree createTree(final int[] parents) {
		Node root = null;
		Node[] nodes = new Node[parents.length];
		for (int index = 0; index < nodes.length; index++) {
			nodes[index] = new Node(index);
		}

		for (int index = 0; index < parents.length; index++) {
			int parentIndex = parents[index];
			if (parentIndex == -1) {
				root = nodes[index];
			} else {
				nodes[parentIndex].children.add(nodes[index]);
				nodes[index].parent = nodes[parentIndex];
			}
		}

		if (root == null) {
			throw new NullPointerException("Tree without root node");
		}

		return new Tree(nodes, root.index);
	}

	public static void main(String[] args) throws IOException {
		FastScanner in = new FastScanner();
		int totalNodes = in.nextInt();
		int[] parents = new int[totalNodes];
		for (int i = 0; i < totalNodes; i++) {
			parents[i] = in.nextInt();
		}
		System.out.println(computeHeight(parents));
	}
}
