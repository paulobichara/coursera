import org.junit.Assert;
import org.junit.Test;

public class ProcessPackagesTest {

    @Test
    public void testSamples() {
        Assert.assertArrayEquals(new long[] {}, processRequests(1, 0, new int[] {}, new int[] {}));
        Assert.assertArrayEquals(new long[] { 0 }, processRequests(1, 1, new int[]{ 0 }, new int[]{ 0 }));
        Assert.assertArrayEquals(new long[] { 1 }, processRequests(1, 1, new int[]{ 1 }, new int[]{ 0 }));
        Assert.assertArrayEquals(new long[] { 0, -1 }, processRequests(1, 2, new int[]{ 0, 0 }, new int[]{ 1, 1 }));
        Assert.assertArrayEquals(new long[] { 0, 1 }, processRequests(1, 2, new int[]{ 0, 1 }, new int[]{ 1, 1 }));
        Assert.assertArrayEquals(new long[] { 0, 1 }, processRequests(2, 2, new int[]{ 0, 0 }, new int[]{ 1, 1 }));
        Assert.assertArrayEquals(new long[] { 0, -1 }, processRequests(1, 2, new int[]{ 0, 0 }, new int[]{ 1, 0 }));
        Assert.assertArrayEquals(new long[] { 0, 2, 6 }, processRequests(2, 3, new int[]{ 0, 1, 5 }, new int[]{ 2, 4, 3 }));
    }

    private long[] processRequests(int queueSize, int requestQty, int[] arrivals, int[] durations) {
        if (requestQty == 0) {
            return new long[0];
        }
        Processor processor = new Processor(queueSize);
        Request[] requests = new Request[requestQty];
        for (int index = 0; index < arrivals.length; index++) {
            requests[index] = new Request(index, arrivals[index], durations[index]);
        }
        return processor.processRequests(requests);
    }

}
