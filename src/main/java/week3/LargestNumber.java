package week3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LargestNumber {
    static String largestNumber(String[] a) {
        List<String> numbers = new ArrayList<>(Arrays.asList(a));
        StringBuilder result = new StringBuilder();

        while (numbers.size() > 0) {
            int bestIndex = getBestCandidateIndex(numbers);
            result.append(numbers.get(bestIndex));
            numbers.remove(bestIndex);
        }

        return result.toString();
    }

    private static int getBestCandidateIndex(List<String> candidates) {
        int bestIndex = 0;

        for (int index = 1; index < candidates.size(); index++) {
            if (isBetterCandidateThan(candidates.get(index), candidates.get(bestIndex))) {
                bestIndex = index;
            }
        }
        return bestIndex;
    }

    private static boolean isBetterCandidateThan(String candidate1, String candidate2) {
        return Integer.parseInt(candidate1 + candidate2) > Integer.parseInt(candidate2 + candidate1);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.next();
        }
        System.out.println(largestNumber(a));
    }
}

