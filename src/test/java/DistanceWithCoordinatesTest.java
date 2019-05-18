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
        assertPath(graph, 0, 2);
        assertPath(graph, 0, 0);
    }

    private void assertPath(DistanceWithCoordinates.GraphWithReverse graph, int fromIndex, int toIndex) {
        Assert.assertEquals(graph.dijkstra(fromIndex, toIndex), graph.bidirectionalDijkstra(fromIndex, toIndex));
    }

}
