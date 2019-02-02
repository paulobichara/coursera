package week2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FibonacciHugeFast {
    static long getFibonacciHuge(long n, int m) {
        List<Integer> sequence = new ArrayList<>();

        int periodLength = findPisanoPeriodLength(m, sequence);
        int fNModLength = sequence.get(Math.toIntExact(n % periodLength));

        return fNModLength % m;
    }

    private static int findPisanoPeriodLength(int m, List<Integer> sequence) {
        List<Integer> period = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        boolean foundPeriod = false;

        for (int i = 0; !foundPeriod; i++) {
            if (i <= 1) {
                sequence.add(i, i);
                period.add(i);
            } else {
                sequence.add(i, (sequence.get(i-2) + sequence.get(i-1)) % m);
                current.add(sequence.get(i));

                if (!period.subList(0, current.size()).equals(current)) {
                    period.addAll(current);
                    current.clear();
                }
            }
            foundPeriod = period.equals(current);
        }
        return period.size();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        int m = scanner.nextInt();
        System.out.println(getFibonacciHuge(n, m));
    }
}

