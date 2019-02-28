import java.util.Random;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Test;

public class ProcessPackagesTest {

    private static final Random RANDOM = new Random();
    private static final int MAX_REQUESTS = 100000;
    private static final int MAX_BUFFER_SIZE = 100000;
    private static final int MAX_ARRIVAL = 1000000;
    private static final int MAX_DURATION = 1000;
    private static final int TEST = 100000000;

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
        Assert.assertArrayEquals(new long[] { 1, -1, -1 }, processRequests(1, 3, new int[]{ 1, 1, 1 }, new int[]{ 1, 1, 1 }));
        Assert.assertArrayEquals(new long[] { 1, 1, 1 }, processRequests(1, 3, new int[]{ 1, 1, 1 }, new int[]{ 0, 0, 0 }));
        Assert.assertArrayEquals(new long[] { -1, -1, -1 }, processRequests(0, 3, new int[]{ 0, 1, 5 }, new int[]{ 2, 4, 3 }));

        Assert.assertArrayEquals(new long[] { 0, 0, 1, 3, -1, -1 }, processRequests(2, 6, new int[]{ 0, 0, 1, 1, 2, 2 }, new int[]{ 0, 0, 2, 1, 1, 1 }));
        Assert.assertArrayEquals(new long[] { 0, 0, 1, 2, 4, -1 }, processRequests(2, 6, new int[]{ 0, 0, 1, 1, 2, 2 }, new int[]{ 0, 0, 1, 2, 1, 1 }));

        Assert.assertArrayEquals(new long[] { 1000000, 1001000, 1002000 }, processRequests(3, 3, new int[]{ 1000000, 1000000, 1000000 }, new int[]{ 1000, 1000, 1000 }));
    }

    @Test
    public void testBigInput() {
        int[] arrivals = IntStream.range(0, MAX_REQUESTS).toArray();
        int[] durations = IntStream.range(0, MAX_REQUESTS).toArray();
        processRequests(100, MAX_REQUESTS, arrivals, durations);
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
