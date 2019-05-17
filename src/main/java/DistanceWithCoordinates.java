import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class DistanceWithCoordinates {

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

        long bidirectionalDijkstra(int fromIndex, int toIndex) {
            if (fromIndex == toIndex) {
                return 0L;
            }

            PriorityQueue<Node> queue = new PriorityQueue<>(new NodeComparator(nodes.length));
            PriorityQueue<Node> queueRev = new PriorityQueue<>(new NodeComparator(nodes.length));
            initializeQueues(queue, queueRev, fromIndex, toIndex);

            Map<Integer,Node> processed = new HashMap<>();
            Node[] previous = new Node[nodes.length];

            Map<Integer,Node> processedRev = new HashMap<>();
            Node[] previousRev = new Node[nodes.length];

            while (!queue.isEmpty() && !queueRev.isEmpty()) {
                Node node = queue.poll();
                if (relaxEdges(node, queue, processed, previous)) {
                    if (processedRev.containsKey(node.index)) {
                        return shortestPath(queue, queueRev, processed, processedRev, fromIndex, toIndex);
                    }
                } else {
                    break;
                }

                node = queueRev.poll();
                if (relaxEdges(node, queueRev, processedRev, previousRev)) {
                    if (processed.containsKey(node.index)) {
                        return shortestPath(queue, queueRev, processed, processedRev, fromIndex, toIndex);
                    }
                } else {
                    break;
                }
            }

            return -1;
        }

        private boolean relaxEdges(Node node, PriorityQueue<Node> queue, Map<Integer,Node> processed,
            Node[] previous) {
            long[] distances = ((NodeComparator)queue.comparator()).distances;
            if (distances[node.index] == Long.MAX_VALUE) {
                return false;
            }

            for (Edge edge : node.outgoing.values()) {
                if (distances[edge.destination.index] > distances[node.index] + edge.weight) {
                    previous[edge.destination.index] = node;
                    queue.remove(edge.destination);
                    distances[edge.destination.index] = distances[node.index] + edge.weight;
                    queue.add(edge.destination);
                }
            }
            processed.put(node.index, node);
            return true;
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

        private long shortestPath(PriorityQueue<Node> queue, PriorityQueue<Node> queueRev, Map<Integer,Node> processed,
            Map<Integer,Node> processedRev, int fromIndex, int toIndex) {
            long[] distances = ((NodeComparator)queue.comparator()).distances;
            long[] distancesRev = ((NodeComparator)queueRev.comparator()).distances;
            long minDistance = Math.min(distances[toIndex], distancesRev[fromIndex]);

            for (Node node : processed.values()) {
                if (distancesRev[node.index] != Long.MAX_VALUE
                    && minDistance > distances[node.index] + distancesRev[node.index]) {
                    minDistance = distances[node.index] + distancesRev[node.index];
                }
            }

            for (Node node : processedRev.values()) {
                if (distances[node.index] != Long.MAX_VALUE
                    && minDistance > distances[node.index] + distancesRev[node.index]) {
                    minDistance = distances[node.index] + distancesRev[node.index];
                }
            }

            return minDistance == Long.MAX_VALUE ? -1 : minDistance;
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
            for (int index = 1; index <= qtyNodes; index++) {
                scanner.nextInt();
                scanner.nextInt();
            }

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
