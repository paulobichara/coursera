package week3;

import java.math.BigInteger;
import java.util.*;

public class DifferentSummands {
    private static List<Integer> optimalSummands(int n) {
        List<Integer> summands = new ArrayList<>();
        BigInteger sum = BigInteger.ZERO;

        for (int i = 1; sum.add(BigInteger.valueOf(i)).compareTo(BigInteger.valueOf(n)) <= 0; i++) {
            summands.add(i);
            sum = sum.add(BigInteger.valueOf(i));
        }
        if (sum.compareTo(BigInteger.valueOf(n)) < 0) {
            int lastNumber = summands.get(summands.size() - 1);
            lastNumber = lastNumber + (BigInteger.valueOf(n).subtract(sum).intValue());
            summands.set(summands.size() - 1, lastNumber);
        }

        return summands;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> summands = optimalSummands(n);
        System.out.println(summands.size());
        for (Integer summand : summands) {
            System.out.print(summand + " ");
        }
    }
}

