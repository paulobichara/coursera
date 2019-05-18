import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Test;

public class DistanceWithCoordinatesTest {

    @Test
    public void testSmallSamples() {
        DistanceWithCoordinates.GraphWithReverse graph = new DistanceWithCoordinates.GraphWithReverse(4);
        graph.addNode(0, 0, 0);
        graph.addNode(1, 0, 1);
        graph.addNode(2, 2, 1);
        graph.addNode(3, 2, 0);

        graph.addEdge(0, 1, 1);
        graph.addEdge(3, 0, 2);
        graph.addEdge(1, 2, 2);
        graph.addEdge(0, 2, 5);
        assertPath(graph, 0, 2, new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 }, 3L);
        assertPath(graph, 0, 0, new int[] { 1 }, new int[] { 1 }, 0L);
    }

    private void assertPath(DistanceWithCoordinates.GraphWithReverse graph, int fromIndex, int toIndex, int[] sequence,
            int[] sequenceBi, long totalWeight) {
        DistanceWithCoordinates.Path path = graph.dijkstra(fromIndex, toIndex);
        DistanceWithCoordinates.Path pathBi = graph.bidirectionalDijkstra(fromIndex, toIndex);
        String errorMsg = getErrorMessage(fromIndex, toIndex, path, pathBi);
        if (sequence != null) {
            Assert.assertEquals(errorMsg, totalWeight, path.totalWeight);
            Assert.assertEquals(errorMsg, totalWeight, pathBi.totalWeight);
            IntStream.of(sequence).forEach(number ->
                    Assert.assertEquals(errorMsg, path.nodeSequence.pop().intValue(), number));
            IntStream.of(sequenceBi).forEach(number ->
                    Assert.assertEquals(errorMsg, pathBi.nodeSequence.pop().intValue(), number));
        } else {
            Assert.assertNull(errorMsg, path);
            Assert.assertNull(errorMsg, pathBi);
        }
    }

    private String getErrorMessage(int fromIndex, int toIndex, DistanceWithCoordinates.Path expected,
            DistanceWithCoordinates.Path result) {
        String message = "Failed testing nodes ";
        message = message + (fromIndex + 1) + " and " + (toIndex + 1) + "\n";
        if (expected != null) {
            message = message + "EXPECTED weight: " + expected.totalWeight + ", path: " + expected.toString() + "\n";
        } else {
            message = message + "EXPECTED no path found" + "\n";
        }
        if (result != null) {
            message = message + "RESULT weight: " + result.totalWeight + ", path: " + result.toString() + "\n";
        } else {
            message = message + "RESULT no path found" + "\n";
        }
        return message;
    }

}
