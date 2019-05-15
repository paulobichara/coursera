import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Test;

public class FriendSuggestionTest {

    // Graph with 198 nodes and 2742 edges
    private static final File GRAPH_JAZZ = new File("src/test/resources/graphs/jazz.graph");

    @Test
    public void testSmallSamples() {
        FriendSuggestion.GraphWithReverse graph = new FriendSuggestion.GraphWithReverse(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(3, 0, 2);
        graph.addEdge(1, 2, 2);
        graph.addEdge(0, 2, 5);
        assertPath(graph, 0, 2, new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 }, 3L);
        assertPath(graph, 0, 0, new int[] { 1 }, new int[] { 1 }, 0L);

        graph = new FriendSuggestion.GraphWithReverse(5);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 1, 1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 4, 4);
        graph.addEdge(4, 3, 1);
        graph.addEdge(1, 4, 3);
        graph.addEdge(2, 3, 4);
        assertPath(graph, 0, 4, new int[] { 1, 3, 5 }, new int[] { 1, 3, 5 }, 6L);

        graph = new FriendSuggestion.GraphWithReverse(3);
        graph.addEdge(0, 1, 7);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 2);
        assertPath(graph, 2, 1, null, null,-1L);
    }

    @Test
    public void testBigSamples() throws IOException {
        FriendSuggestion.GraphWithReverse graph = readGraphFromFile(GRAPH_JAZZ);
        assertPath(graph, 1, 23, new int[] { 2, 19, 24 }, new int[] { 2, 19, 24 },2L);
    }

    private void assertPath(FriendSuggestion.GraphWithReverse graph, int fromIndex, int toIndex, int[] sequence,
            int[] sequenceBi, long totalWeight) {
        FriendSuggestion.Path path = graph.dijkstra(fromIndex, toIndex);
        FriendSuggestion.Path pathBi = graph.bidirectionalDijkstra(fromIndex, toIndex);
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

    @Test
    public void jazzTest() throws IOException {
        testGraphFile(new File("src/test/resources/graphs/jazz.graph"));
    }

    @Test
    public void smallWorldTest() throws IOException {
        testGraphFile(new File("src/test/resources/graphs/smallworld.graph"));
    }

    @Test
    public void metabolicTest() throws IOException {
        testGraphFile(new File("src/test/resources/graphs/celegans_metabolic.graph"));
    }

    private void testGraphFile(File file) throws IOException {
        FriendSuggestion.GraphWithReverse graph = readGraphFromFile(file);
        for (int fromIndex = 0; fromIndex < graph.qtyNodes; fromIndex++) {
            for (int toIndex = 0; toIndex < graph.qtyNodes; toIndex++) {
                FriendSuggestion.Path expectedPath = graph.dijkstra(fromIndex, toIndex);
                FriendSuggestion.Path result = graph.bidirectionalDijkstra(fromIndex, toIndex);
                Assert.assertEquals(getErrorMessage(fromIndex, toIndex, expectedPath, result),
                    expectedPath == null ? -1 : expectedPath.totalWeight,
                    result == null ? -1 : result.totalWeight);
            }
        }
    }

    private FriendSuggestion.GraphWithReverse readGraphFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            StringTokenizer header = new StringTokenizer(reader.readLine());
            int qtyNodes = Integer.valueOf(header.nextToken());

            FriendSuggestion.GraphWithReverse graph = new FriendSuggestion.GraphWithReverse(qtyNodes);
            reader.lines().forEach(line -> {
                StringTokenizer tokenizer = new StringTokenizer(line);
                int fromIndex = Integer.valueOf(tokenizer.nextToken()) - 1;
                while (tokenizer.hasMoreTokens()) {
                    int toIndex = Integer.valueOf(tokenizer.nextToken()) - 1;
                    graph.addEdge(fromIndex, toIndex, 1);
                }
            });

            return graph;
        }
    }

    private String getErrorMessage(int fromIndex, int toIndex, FriendSuggestion.Path expected,
            FriendSuggestion.Path result) {
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
