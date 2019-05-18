import java.util.ArrayList;
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
        Long[] distances;

        NodeComparator(int qtyNodes) {
            distances = new Long[qtyNodes];
        }

        @Override
        public int compare(Node o1, Node o2) {
            if (distances[o1.index] == null) {
                if (distances[o2.index] == null) {
                    return 0;
                } else {
                    return 1;
                }
            } else if (distances[o2.index] == null) {
                return -1;
            }

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

            while (!queue.isEmpty() && !queueRev.isEmpty()) {
                Node node = queue.poll();
                if (relaxEdges(node, queue, processed, previous)) {
                    if (processedRev.containsKey(node.index)) {
                        return shortestPath(queue, queueRev, processed, processedRev,
                            previous, previousRev, fromIndex, toIndex);
                    }
                } else {
                    break;
                }

                node = queueRev.poll();
                if (relaxEdges(node, queueRev, processedRev, previousRev)) {
                    if (processed.containsKey(node.index)) {
                        return shortestPath(queue, queueRev, processed, processedRev,
                                previous, previousRev, fromIndex, toIndex);
                    }
                } else {
                    break;
                }
            }

            return null;
        }

        private boolean relaxEdges(Node node, PriorityQueue<Node> queue, Map<Integer,Node> processed,
                Node[] previous) {
            Long[] distances = ((NodeComparator)queue.comparator()).distances;
            if (distances[node.index] == null) {
                return false;
            }

            for (Edge edge : node.outgoing.values()) {
                long possibility = distances[node.index] + edge.weight;
                if (distances[edge.destination.index] == null || distances[edge.destination.index] > possibility) {
                    previous[edge.destination.index] = node;
                    queue.remove(edge.destination);
                    distances[edge.destination.index] = possibility;
                    queue.add(edge.destination);
                }
            }
            processed.put(node.index, node);
            return true;
        }

        private void initializeQueues(PriorityQueue<Node> queue, PriorityQueue<Node> queueRev, int fromIndex,
            int toIndex) {
            Long[] distances = new Long[qtyNodes];
            Long[] distancesRev = new Long[qtyNodes];
            distances[fromIndex] = 0L;
            distancesRev[toIndex] = 0L;

            NodeComparator comparator = ((NodeComparator) queue.comparator());
            comparator.distances = distances;

            NodeComparator comparatorRev = ((NodeComparator) queueRev.comparator());
            comparatorRev.distances = distancesRev;

            for (int index = 0; index < nodes.length; index++) {
                queue.add(getNode(index));
                queueRev.add(reverse.getNode(index));
            }
        }

        private Path shortestPath(PriorityQueue<Node> queue, PriorityQueue<Node> queueRev, Map<Integer,Node> processed,
                Map<Integer,Node> processedRev, Node[] previous, Node[] previousRev, int fromIndex, int toIndex) {
            Long[] distances = ((NodeComparator)queue.comparator()).distances;
            Long[] distancesRev = ((NodeComparator)queueRev.comparator()).distances;
            long minDistance = Math.min(distances[toIndex] == null ? Long.MAX_VALUE : distances[toIndex],
                    distancesRev[fromIndex] == null ? Long.MAX_VALUE : distancesRev[fromIndex]);
            Node best = null;

            for (Node node : processed.values()) {
                if (distancesRev[node.index] != null
                        && minDistance > distances[node.index] + distancesRev[node.index]) {
                    best = node;
                    minDistance = distances[node.index] + distancesRev[node.index];
                }
            }

            for (Node node : processedRev.values()) {
                if (distances[node.index] != null
                        && minDistance > distances[node.index] + distancesRev[node.index]) {
                    best = node;
                    minDistance = distances[node.index] + distancesRev[node.index];
                }
            }

            if (best == null && minDistance < Long.MAX_VALUE) {
                if (distances[toIndex] != null && distances[toIndex] == minDistance) {
                    return super.buildPath(toIndex, previous, distances);
                } else {
                    Path path = super.buildPath(fromIndex, previousRev, distancesRev);
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
                    current = previousRev[current.index]) {
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

            NodeComparator comparator = new NodeComparator(qtyNodes);
            Long[] distances = comparator.distances;
            distances[fromIndex] = 0L;

            PriorityQueue<Node> queue = new PriorityQueue<>(comparator);
            Collections.addAll(queue, nodes);

            Node[] previous = new Node[nodes.length];

            while (!queue.isEmpty()) {
                Node node = queue.poll();

                if (distances[node.index] == null) {
                    return null;
                }

                for (Edge edge : node.outgoing.values()) {
                    if (distances[edge.destination.index] == null
                            || distances[edge.destination.index] > distances[node.index] + edge.weight) {
                        previous[edge.destination.index] = node;
                        queue.remove(edge.destination);
                        distances[edge.destination.index] = distances[node.index] + edge.weight;
                        comparator.distances = distances;
                        queue.add(edge.destination);
                    }
                }
            }

            return distances[toIndex] == null ? null : buildPath(toIndex, previous, distances);
        }

        private Path buildPath(int toIndex, Node[] previous, Long[] distances) {
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
                Path path = graph.bidirectionalDijkstra(scanner.nextInt() - 1, scanner.nextInt() - 1);
                System.out.println(path == null ? -1 : path.totalWeight);
            }
        }
    }

}
