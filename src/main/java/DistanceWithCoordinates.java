import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class DistanceWithCoordinates {

    private static class NodeComparator implements Comparator<Node> {
        Long[] distances;
        SearchDirection direction;

        GraphWithReverse graph;
        int sourceIndex;
        int targetIndex;

        NodeComparator(SearchDirection direction, GraphWithReverse graph, int sourceIndex, int targetIndex) {
            this.direction = direction;
            this.graph = graph;
            this.sourceIndex = sourceIndex;
            this.targetIndex = targetIndex;
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

            return Double.compare(distances[o1.index] + getRealPotential(o1),
                distances[o2.index] + getRealPotential(o2));
        }

        private double getRealPotential(Node node) {
            double realPotential = (node.getPotential(graph.nodes[targetIndex]) -
                node.getPotential(graph.nodes[sourceIndex])) / 2;

            if (SearchDirection.BACKWARD.equals(direction)) {
                return -realPotential;
            }

            return realPotential;
        }
    }

    static class Heap {

        private Node[] array;
        private int size;
        NodeComparator comparator;

        private Integer[] heapIndexes;
        private Long[] priorities;

        Heap(int maxSize, NodeComparator comparator) {
            array = new Node[maxSize];
            priorities = new Long[maxSize];
            heapIndexes = new Integer[maxSize];
            size = 0;

            this.comparator = comparator;
            comparator.distances = priorities;
        }

        void insert(Node node, Long priority) {
            if (size == array.length) {
                throw new RuntimeException("Heap is full");
            }

            size++;
            array[size - 1] = node;
            heapIndexes[node.index] = size - 1;
            priorities[node.index] = priority;
            siftUp(size - 1);
        }

        Node poll() {
            Node result = array[0];
            array[0] = array[size - 1];
            size--;
            siftDown(0);
            return result;
        }

        void changePriority(Node node, Long priority) {
            Long oldPriority = priorities[node.index];
            priorities[node.index] = priority;
            if (oldPriority != null && priority > oldPriority) {
                siftDown(heapIndexes[node.index]);
            } else {
                siftUp(heapIndexes[node.index]);
            }

        }

        boolean isNotEmpty() {
            return size > 0;
        }

        private int parentIndex(int index) {
            return (index - 1) / 2;
        }

        private int leftChildIndex(int index) {
            return 2 * index + 1;
        }

        private int rightChildIndex(int index) {
            return 2 * index + 2;
        }

        private void siftUp(int heapIndex) {
            for (int parentIndex = parentIndex(heapIndex); heapIndex > 0
                    && comparator.compare(array[parentIndex], array[heapIndex]) > 0;
                    heapIndex = parentIndex, parentIndex = parentIndex(heapIndex)) {
                swap(parentIndex, heapIndex);
            }
        }

        private void siftDown(int index) {
            int minIndex = index;

            int leftChildIndex = leftChildIndex(index);
            if (leftChildIndex < array.length && comparator.compare(array[leftChildIndex], array[minIndex]) < 0) {
                minIndex = leftChildIndex;
            }

            int rightChildIndex = rightChildIndex(index);
            if (rightChildIndex < array.length && comparator.compare(array[rightChildIndex], array[minIndex]) < 0) {
                minIndex = rightChildIndex;
            }

            if (index != minIndex) {
                swap(index, minIndex);
                siftDown(minIndex);
            }
        }

        private void swap(int index1, int index2) {
            Node value1 = array[index1];
            array[index1] = array[index2];
            array[index2] = value1;
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
        long coordinateX;
        long coordinateY;

        Node(int index, long coordinateX, long coordinateY) {
            this.index = index;
            outgoing = new HashMap<>();
            this.coordinateX = coordinateX;
            this.coordinateY = coordinateY;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) {
                return false;
            }
            return this.index == ((Node)obj).index;
        }

        double getPotential(Node toNode) {
            return Math.sqrt(Math.pow(coordinateX - toNode.coordinateX, 2) + Math.pow(coordinateY - toNode.coordinateY, 2));
        }
    }

    private enum SearchDirection {
        FORWARD, BACKWARD
    }

    static class GraphWithReverse extends DirectedGraph {

        DirectedGraph reverse;

        GraphWithReverse(int qtyNodes) {
            super(qtyNodes);
            reverse = new DirectedGraph(qtyNodes);
        }

        @Override
        void addNode(int index, long coordinateX, long coordinateY) {
            super.addNode(index, coordinateX, coordinateY);
            reverse.addNode(index, coordinateX, coordinateY);
        }

        @Override
        void addEdge(int fromIndex, int toIndex, int weight) {
            super.addEdge(fromIndex, toIndex, weight);
            reverse.addEdge(toIndex, fromIndex, weight);
        }

        long bidirectionalDijkstra(int fromIndex, int toIndex) {
            if (fromIndex == toIndex) {
                return 0;
            }

            Heap queue = new Heap(qtyNodes, new NodeComparator(SearchDirection.FORWARD, this, fromIndex, toIndex));
            Heap queueRev = new Heap(qtyNodes, new NodeComparator(SearchDirection.BACKWARD, this, fromIndex, toIndex));

            initializeQueues(queue, queueRev, fromIndex, toIndex);

            Map<Integer,Node> processed = new HashMap<>();
            Map<Integer,Node> processedRev = new HashMap<>();

            while (queue.isNotEmpty() && queueRev.isNotEmpty()) {
                Node node = queue.poll();
                if (relaxEdges(node, queue, processed)) {
                    if (processedRev.containsKey(node.index)) {
                        long minDistance = shortestPath(queue, queueRev, processed, processedRev, fromIndex, toIndex);
                        return minDistance == Long.MAX_VALUE ? -1 : minDistance;
                    }
                } else {
                    break;
                }

                node = queueRev.poll();
                if (relaxEdges(node, queueRev, processedRev)) {
                    if (processed.containsKey(node.index)) {
                        long minDistance = shortestPath(queue, queueRev, processed, processedRev, fromIndex, toIndex);
                        return minDistance == Long.MAX_VALUE ? -1 : minDistance;
                    }
                } else {
                    break;
                }
            }

            return -1;
        }

        private boolean relaxEdges(Node node, Heap queue, Map<Integer,Node> processed) {
            if (queue.priorities[node.index] == null) {
                return false;
            }

            for (Edge edge : node.outgoing.values()) {
                long possibility = queue.priorities[node.index] + edge.weight;
                if (queue.priorities[edge.destination.index] == null
                        || queue.priorities[edge.destination.index] > possibility) {
                    queue.changePriority(edge.destination, possibility);
                }
            }
            processed.put(node.index, node);
            return true;
        }

        private void initializeQueues(Heap queue, Heap queueRev, int fromIndex, int toIndex) {
            for (int index = 0; index < nodes.length; index++) {
                if (index == fromIndex) {
                    queue.insert(nodes[index], 0L);
                } else {
                    queue.insert(nodes[index], null);
                }
                if (index == toIndex) {
                    queueRev.insert(reverse.nodes[index], 0L);
                } else {
                    queueRev.insert(reverse.nodes[index], null);
                }
            }
        }

        private long shortestPath(Heap queue, Heap queueRev, Map<Integer,Node> processed,
                Map<Integer,Node> processedRev, int fromIndex, int toIndex) {
            Long[] distances = queue.priorities;
            Long[] distancesRev = queueRev.priorities;
            long minDistance = Math.min(distances[toIndex] == null ? Long.MAX_VALUE : distances[toIndex],
                    distancesRev[fromIndex] == null ? Long.MAX_VALUE : distancesRev[fromIndex]);

            for (Node node : processed.values()) {
                if (distancesRev[node.index] != null
                        && minDistance > distances[node.index] + distancesRev[node.index]) {
                    minDistance = distances[node.index] + distancesRev[node.index];
                }
            }

            for (Node node : processedRev.values()) {
                if (distances[node.index] != null
                        && minDistance > distances[node.index] + distancesRev[node.index]) {
                    minDistance = distances[node.index] + distancesRev[node.index];
                }
            }
            return minDistance;
        }
    }

    static class DirectedGraph {
        Node[] nodes;
        int qtyNodes;

        DirectedGraph(int qtyNodes) {
            this.qtyNodes = qtyNodes;
            nodes  = new Node[qtyNodes];
        }

        void addNode(int index, long coordinateX, long coordinateY) {
            nodes[index] = new Node(index, coordinateX, coordinateY);
        }

        void addEdge(int fromIndex, int toIndex, int weight) {
            Edge edge = new Edge(nodes[toIndex], weight);
            nodes[fromIndex].outgoing.put(toIndex, edge);
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

        long dijkstra(int fromIndex, int toIndex) {
            if (fromIndex == toIndex) {
                return 0;
            }

            NodeComparator comparator = new NodeComparator(nodes.length);
            long[] distances = comparator.distances;
            Arrays.fill(distances, Integer.MAX_VALUE);
            distances[fromIndex] = 0;

            PriorityQueue<Node> queue = new PriorityQueue<>(comparator);
            Collections.addAll(queue, nodes);

            while (!queue.isEmpty()) {
                Node node = queue.poll();
                for (Edge edge : node.outgoing.values()) {
                    if (distances[edge.destination.index] > distances[node.index] + edge.weight) {
                        queue.remove(edge.destination);
                        distances[edge.destination.index] = distances[node.index] + edge.weight;
                        comparator.distances = distances;
                        queue.add(edge.destination);
                    }
                }
            }

            return distances[toIndex] == Integer.MAX_VALUE ? -1 : distances[toIndex];
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int qtyNodes = scanner.nextInt();
            int qtyEdges = scanner.nextInt();

            GraphWithReverse graph = new GraphWithReverse(qtyNodes);
            for (int index = 0; index < qtyNodes; index++) {
                graph.addNode(index, scanner.nextLong(), scanner.nextLong());
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
