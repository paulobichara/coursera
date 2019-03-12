package coursera.datastructures.week1;

import coursera.datastructures.week1.TreeHeight;
import org.junit.Assert;
import org.junit.Test;

public class TreeHeightTest {

    @Test
    public void testSamples() {
        Assert.assertEquals(3, TreeHeight.computeHeight(new int[] { 4, -1, 4, 1, 1 }));
        Assert.assertEquals(4, TreeHeight.computeHeight(new int[] { -1, 0, 4, 0, 3 }));
        Assert.assertEquals(4, TreeHeight.computeHeight(new int[] { 9, 7, 5, 5, 2, 9, 9, 9, 2, -1 }));
        Assert.assertEquals(6, TreeHeight.computeHeight(new int[] { 8, 8, 5, 6, 7, 3, 1, 6, -1, 5 }));
    }

}
