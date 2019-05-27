package coursera.strings.week1;

import coursera.strings.week1.BuildSuffixTree;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class BuildSuffixTreeTest {

    @Test
    public void testSamples() {
        assertString("A$", new String[] { "A$", "$" });
        assertString("ACA$", new String[] { "$", "$", "CA$", "CA$", "A" });
        assertString("ATAAATG$", new String[] { "AAATG$", "G$", "T", "ATG$", "TG$", "A", "A", "AAATG$", "G$", "T", "G$", "$" });
        assertString("AAA$", new String[] { "$", "$", "$", "A", "A", "A$" });
    }

    private void assertString(String text, String[] expected) {
        BuildSuffixTree.SuffixTree tree = new BuildSuffixTree.SuffixTree(text);
        List<String> original = tree.getAllEdges();
        List<String> edges = new ArrayList<>(original);
        for (String value : expected) {
            if (!edges.remove(value)) {
                Assert.fail("Failed testing '" + text + "'");
            }
        }
        Assert.assertEquals("Failed testing '" + text + "'", 0, edges.size());
    }

}
