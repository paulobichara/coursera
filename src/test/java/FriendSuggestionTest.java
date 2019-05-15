import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import org.junit.Assert;
import org.junit.Test;

public class FriendSuggestionTest {

    @Test
    public void dijkstraSamplesTest() {
        FriendSuggestion.DirectedGraph graph = new FriendSuggestion.DirectedGraph(4);
        graph.addEdge(0, 1, 1);
        graph.addEdge(3, 0, 2);
        graph.addEdge(1, 2, 2);
        graph.addEdge(0, 2, 5);
        FriendSuggestion.Path path = graph.dijkstra(0, 2);
        Assert.assertEquals(3L, path.totalWeight);
        Assert.assertEquals(1, path.nodeSequence.pop().intValue());
        Assert.assertEquals(2, path.nodeSequence.pop().intValue());
        Assert.assertEquals(3, path.nodeSequence.pop().intValue());
        path = graph.dijkstra(0, 0);
        Assert.assertEquals(0L, path.totalWeight);
        Assert.assertEquals(1, path.nodeSequence.pop().intValue());

        graph = new FriendSuggestion.DirectedGraph(5);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 1, 1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 4, 4);
        graph.addEdge(4, 3, 1);
        graph.addEdge(1, 4, 3);
        graph.addEdge(2, 3, 4);
        path = graph.dijkstra(0, 4);
        Assert.assertEquals(6L, path.totalWeight);
        Assert.assertEquals(1, path.nodeSequence.pop().intValue());
        Assert.assertEquals(3, path.nodeSequence.pop().intValue());
        if (path.nodeSequence.size() == 2) {
            Assert.assertEquals(2, path.nodeSequence.pop().intValue());
        }
        Assert.assertEquals(5, path.nodeSequence.pop().intValue());

        graph = new FriendSuggestion.DirectedGraph(3);
        graph.addEdge(0, 1, 7);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 2);
        path = graph.dijkstra(2, 1);
        Assert.assertNull(path);
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), 32768)) {
            StringTokenizer header = new StringTokenizer(reader.readLine());
            int qtyNodes = Integer.valueOf(header.nextToken());

            FriendSuggestion.GraphWithReverse graph = new FriendSuggestion.GraphWithReverse(qtyNodes);
            FriendSuggestion.DirectedGraph expected = new FriendSuggestion.DirectedGraph(qtyNodes);
            reader.lines().forEach(line -> {
                StringTokenizer tokenizer = new StringTokenizer(line);
                int fromIndex = Integer.valueOf(tokenizer.nextToken()) - 1;
                while (tokenizer.hasMoreTokens()) {
                    int toIndex = Integer.valueOf(tokenizer.nextToken()) - 1;
                    graph.addEdge(fromIndex, toIndex, 1);
                    expected.addEdge(fromIndex, toIndex, 1);
                }
            });
            for (int fromIndex = 0; fromIndex < qtyNodes; fromIndex++) {
                for (int toIndex = 0; toIndex < qtyNodes; toIndex++) {
                    FriendSuggestion.Path expectedPath = expected.dijkstra(fromIndex, toIndex);
                    FriendSuggestion.Path result = graph.bidirectionalDijkstra(fromIndex, toIndex);
                    Assert.assertEquals(getErrorMessage(fromIndex, toIndex, expectedPath, result),
                            expectedPath == null ? -1 : expectedPath.totalWeight,
                            result == null ? -1 : result.totalWeight);
                }
            }
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
