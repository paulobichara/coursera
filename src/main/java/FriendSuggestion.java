import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

public class FriendSuggestion {

    static class Path {
        private static final String EDGE = " -> ";

        Stack<Integer> nodeSequence;
        long totalWeight;

        Path(int toIndex, long totalWeight) {
            nodeSequence = new Stack<>();
            nodeSequence.push(toIndex + 1);
            this.totalWeight = totalWeight;
        }

        void pushNode(Node node) {
            nodeSequence.push(node.index + 1);
        }

        @Override
        public String toString() {
            @SuppressWarnings("unchecked")
            Stack<Integer> sequence = (Stack) nodeSequence.clone();
            StringBuilder builder = new StringBuilder();

            while (!sequence.isEmpty()) {
                builder.append(sequence.pop());
                if (!sequence.isEmpty()) {
                    builder.append(EDGE);
                }
            }
            return builder.toString();
        }
    }

    private static class Edge {
        Node destination;
        int weight;

        Edge(Node destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    private static class Node {
        int index;
        Map<Integer, Edge> outgoing;

        Node(int index) {
            this.index = index;
            outgoing = new HashMap<>();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.index == ((Node)obj).index;
        }
    }

    private static class NodeComparator implements Comparator<Node> {
        long[] distances;

        NodeComparator(int qtyNodes) {
            this.distances = new long[qtyNodes];
        }

        @Override
        public int compare(Node o1, Node o2) {
            return Long.compare(distances[o1.index], distances[o2.index]);
        }
    }

    static class GraphWithReverse extends DirectedGraph {

        DirectedGraph reverse;

        GraphWithReverse(int qtyNodes) {
            super(qtyNodes);
            reverse = new DirectedGraph(qtyNodes);
        }

        @Override
        void addEdge(int fromIndex, int toIndex, int weight) {
            super.addEdge(fromIndex, toIndex, weight);
            reverse.addEdge(toIndex, fromIndex, weight);
        }

        Path bidirectionalDijkstra(int fromIndex, int toIndex) {
            if (fromIndex == toIndex) {
                return new Path(toIndex, 0);
            }

            PriorityQueue<Node> queue = new PriorityQueue<>(new NodeComparator(nodes.length));
            PriorityQueue<Node> queueRev = new PriorityQueue<>(new NodeComparator(nodes.length));
            initializeQueues(queue, queueRev, fromIndex, toIndex);

            Map<Integer,Node> processed = new HashMap<>();
            Node[] previous = new Node[nodes.length];

            Map<Integer,Node> processedRev = new HashMap<>();
            Node[] previousRev = new Node[nodes.length];

            while (!queue.isEmpty() || !queueRev.isEmpty()) {
                if (!queue.isEmpty()) {
                    Node node = queue.poll();
                    relaxEdges(node, queue, processed, previous);
                    if (processedRev.containsKey(node.index)) {
                        return shortestPath(queue, queueRev, processed, processedRev,
                                previous, previousRev, fromIndex, toIndex);
                    }
                }

                if (!queueRev.isEmpty()) {
                    Node node = queueRev.poll();
                    relaxEdges(node, queueRev, processedRev, previousRev);
                    if (processed.containsKey(node.index)) {
                        return shortestPath(queue, queueRev, processed, processedRev,
                                previous, previousRev, fromIndex, toIndex);
                    }
                }
            }

            return null;
        }

        private Path shortestPath(PriorityQueue<Node> queue, PriorityQueue<Node> queueRev, Map<Integer,Node> processed,
                Map<Integer,Node> processedRev, Node[] previous, Node[] previousRev, int fromIndex, int toIndex) {
            long[] distances = ((NodeComparator)queue.comparator()).distances;
            long[] distancesRev = ((NodeComparator)queueRev.comparator()).distances;
            long minDistance = Math.min(distances[toIndex], distancesRev[fromIndex]);
            Node best = null;

            for (Node node : processed.values()) {
                if (processedRev.containsKey(node.index) && distances[node.index] != Long.MAX_VALUE
                        && distancesRev[node.index] != Long.MAX_VALUE) {
                    if (minDistance > distances[node.index] + distancesRev[node.index]) {
                        best = node;
                        minDistance = distances[node.index] + distancesRev[node.index];
                    }
                }
            }

            if (best == null && minDistance < Long.MAX_VALUE) {
                if (distances[toIndex] == minDistance) {
                    return super.buildPath(fromIndex, toIndex, previous, distances);
                } else {
                    Path path = super.buildPath(toIndex, fromIndex, previousRev, distancesRev);
                    Collections.reverse(path.nodeSequence);
                    return path;
                }
            } else if (minDistance < Long.MAX_VALUE) {
                return buildPath(best, previous, previousRev, fromIndex, toIndex, minDistance);
            }

            return null;
        }

        private Path buildPath(Node best, Node[] previous, Node[] previousRev, int fromIndex, int toIndex, long minDistance) {
            if (minDistance == Long.MAX_VALUE) {
                return null;
            }

            if (best == null) {
                Path path = new Path(toIndex, minDistance);
                path.pushNode(nodes[fromIndex]);
                return path;
            }

            List<Node> partial = new ArrayList<>();
            for (Node current = best; current != null; current = previous[current.index]) {
                partial.add(current);
            }

            Stack<Node> partialRev = new Stack<>();
            for (Node current = previousRev[best.index]; current != null && current.index != toIndex;
                    current = previous[current.index]) {
                partialRev.push(current);
            }

            Path result = new Path(toIndex, minDistance);
            while (!partialRev.isEmpty()) {
                result.pushNode(partialRev.pop());
            }

            for (int index = 0; index < partial.size(); index++) {
                result.pushNode(partial.get(index));
            }

            return result;
        }

        private void relaxEdges(Node node, PriorityQueue<Node> queue, Map<Integer,Node> processed, Node[] previous) {
            long[] distances = ((NodeComparator)queue.comparator()).distances;
            for (Edge edge : node.outgoing.values()) {
                if (distances[node.index] != Long.MAX_VALUE && (distances[edge.destination.index] == Long.MAX_VALUE
                        || distances[edge.destination.index] > distances[node.index] + edge.weight)) {
                    previous[edge.destination.index] = node;
                    queue.remove(edge.destination);
                    distances[edge.destination.index] = distances[node.index] + edge.weight;
                    queue.add(edge.destination);
                }
            }
            processed.put(node.index, node);
        }

        private void initializeQueues(PriorityQueue<Node> queue, PriorityQueue<Node> queueRev, int fromIndex,
                int toIndex) {
            long[] distances = ((NodeComparator)queue.comparator()).distances;
            long[] distancesRev = ((NodeComparator)queueRev.comparator()).distances;

            for (int index = 0; index < nodes.length; index++) {
                distances[index] = Long.MAX_VALUE;
                distancesRev[index] = Long.MAX_VALUE;
            }

            distances[fromIndex] = 0;
            distancesRev[toIndex] = 0;

            for (int index = 0; index < nodes.length; index++) {
                queue.add(getNode(index));
                queueRev.add(reverse.getNode(index));
            }
        }

    }

    static class DirectedGraph {
        Node[] nodes;
        int qtyNodes;

        DirectedGraph(int qtyNodes) {
            this.qtyNodes = qtyNodes;
            nodes  = new Node[qtyNodes];
        }

        void addEdge(int fromIndex, int toIndex, int weight) {
            Node from = getNode(fromIndex);
            Edge edge = new Edge(getNode(toIndex), weight);
            from.outgoing.put(toIndex, edge);
        }

        Path dijkstra(int fromIndex, int toIndex) {
            if (fromIndex == toIndex) {
                return new Path(toIndex, 0);
            }

            NodeComparator comparator = new NodeComparator(nodes.length);
            long[] distances = comparator.distances;
            Arrays.fill(distances, Integer.MAX_VALUE);
            distances[fromIndex] = 0;

            PriorityQueue<Node> queue = new PriorityQueue<>(comparator);
            Collections.addAll(queue, nodes);

            Node[] previous = new Node[nodes.length];

            while (!queue.isEmpty()) {
                Node node = queue.poll();
                for (Edge edge : node.outgoing.values()) {
                    if (distances[edge.destination.index] > distances[node.index] + edge.weight) {
                        previous[edge.destination.index] = node;
                        queue.remove(edge.destination);
                        distances[edge.destination.index] = distances[node.index] + edge.weight;
                        comparator.distances = distances;
                        queue.add(edge.destination);
                    }
                }
            }

            return distances[toIndex] == Integer.MAX_VALUE ? null : buildPath(fromIndex, toIndex, previous, distances);
        }

        private Path buildPath(int fromIndex, int toIndex, Node[] previous, long[] distances) {
            Path path = null;
            for (Node current = getNode(toIndex); current != null; current = previous[current.index]) {
                if (path == null) {
                    path = new Path(current.index, distances[current.index]);
                } else {
                    path.pushNode(current);
                }
            }
            return path;
        }

        Node getNode(int index) {
            if (nodes[index] == null) {
                nodes[index] = new Node(index);
            }
            return nodes[index];
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int qtyNodes = scanner.nextInt();
            int qtyEdges = scanner.nextInt();

            GraphWithReverse graph = new GraphWithReverse(qtyNodes);
            for (int i = 0; i < qtyEdges; i++) {
                graph.addEdge(scanner.nextInt() - 1, scanner.nextInt() - 1, scanner.nextInt());
            }

            int queriesQty = scanner.nextInt();
            for (int index = 0; index < queriesQty; index++) {
                System.out.println(graph.bidirectionalDijkstra(scanner.nextInt() - 1, scanner.nextInt() - 1));
            }
        }
    }

}
