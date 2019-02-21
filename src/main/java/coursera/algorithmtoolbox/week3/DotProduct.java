package coursera.algorithmtoolbox.week3;

import java.math.BigInteger;
import java.util.*;

public class DotProduct {
    private static BigInteger maxDotProduct(List<Integer> a, List<Integer> b) {
        List<Integer> numbers1 = new ArrayList<>();
        List<Integer> numbers2 = new ArrayList<>();

        for (int i = 0; a.size() > 0; i++) {
            int smallerIndex1 = findSmallerNumberIndex(a);
            numbers1.add(i, a.get(smallerIndex1));
            a.remove(smallerIndex1);

            int smallerIndex2 = findSmallerNumberIndex(b);
            numbers2.add(i, b.get(smallerIndex2));
            b.remove(smallerIndex2);
        }

        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < numbers1.size(); i++) {
            result = result.add(BigInteger.valueOf(numbers1.get(i)).multiply(BigInteger.valueOf(numbers2.get(i))));
        }
        return result;
    }

    private static Integer findSmallerNumberIndex(List<Integer> numbers) {
        int smaller = -1;
        for (int i = 0; i < numbers.size(); i++) {
            smaller = smaller == -1 || numbers.get(i) < numbers.get(smaller) ? i : smaller;
        }
        return smaller;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> a = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            a.add(i, scanner.nextInt());
        }
        List<Integer> b = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            b.add(i, scanner.nextInt());
        }
        System.out.println(maxDotProduct(a, b));
    }
}

