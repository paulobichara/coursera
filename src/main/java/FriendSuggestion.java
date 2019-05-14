import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class FriendSuggestion {

    private static class Edge {
        Node origin;
        Node destination;
        int weight;

        Edge(Node origin, Node destination, int weight) {
            this.origin = origin;
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

        long bidirectionalDijkstra(int fromIndex, int toIndex) {
            if (fromIndex == toIndex) {
                return 0;
            }

            PriorityQueue<Node> queue = new PriorityQueue<>(new NodeComparator(nodes.length));
            PriorityQueue<Node> queueRev = new PriorityQueue<>(new NodeComparator(nodes.length));
            initializeQueues(queue, queueRev, fromIndex, toIndex);

            Map<Integer,Node> processed = new HashMap<>();
            Map<Integer,Node> processedRev = new HashMap<>();

            while (!queue.isEmpty() || !queueRev.isEmpty()) {
                if (!queue.isEmpty()) {
                    Node node = queue.poll();
                    relaxEdges(node, queue, processed);
                    if (processedRev.containsKey(node.index)) {
                        return shortestPath(queue, queueRev, processed, processedRev, fromIndex, toIndex);
                    }
                }

                if (!queueRev.isEmpty()) {
                    Node node = queueRev.poll();
                    relaxEdges(node, queueRev, processedRev);
                    if (processed.containsKey(node.index)) {
                        return shortestPath(queue, queueRev, processed, processedRev, fromIndex, toIndex);
                    }
                }
            }

            return -1;
        }

        private long shortestPath(PriorityQueue<Node> queue, PriorityQueue<Node> queueRev, Map<Integer,Node> processed,
                Map<Integer,Node> processedRev, int fromIndex, int toIndex) {

            long[] distances = ((NodeComparator)queue.comparator()).distances;
            long[] distancesRev = ((NodeComparator)queueRev.comparator()).distances;
            long minDistance = Math.min(distances[toIndex], distancesRev[fromIndex]);

            for (Node node : processed.values()) {
                if (processedRev.containsKey(node.index)) {
                    minDistance = Math.min(minDistance, distances[node.index] + distancesRev[node.index]);
                }
            }

            return minDistance == Long.MAX_VALUE ? -1 : minDistance;
        }

        private void relaxEdges(Node node, PriorityQueue<Node> queue, Map<Integer,Node> processed) {
            long[] distances = ((NodeComparator)queue.comparator()).distances;
            for (Edge edge : node.outgoing.values()) {
                if (distances[edge.destination.index] == Long.MAX_VALUE
                        || distances[edge.destination.index] > distances[node.index] + edge.weight) {
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

        DirectedGraph(int qtyNodes) {
            nodes  = new Node[qtyNodes];
        }

        void addEdge(int fromIndex, int toIndex, int weight) {
            Node from = getNode(fromIndex);
            Edge edge = new Edge(from, getNode(toIndex), weight);
            from.outgoing.put(toIndex, edge);
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
