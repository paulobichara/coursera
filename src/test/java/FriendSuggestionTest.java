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
//            int qtyEdges = Integer.valueOf(header.nextToken());

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
            for (int index = 0; index < qtyNodes; index++) {
                for (int index2 = 0; index2 < qtyNodes; index2++) {
                    Assert.assertEquals("Failed testing nodes " + index + " and " + index2,
                            expected.dijkstra(index, index2), graph.bidirectionalDijkstra(index, index2));
                }
            }
        }
    }

}
