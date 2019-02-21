package coursera.algorithmtoolbox.week5;

import java.util.*;

public class PrimitiveCalculator {
    private static List<Integer> optimalSequenceDP(int target) {
        List<Integer> sequence = new ArrayList<>();
        int[][] minOperations = new int[3][target + 1];

        for (int operationIndex = 0; operationIndex < 3; operationIndex++) {
            minOperations[operationIndex][0] = Integer.MAX_VALUE;
        }

        for (int number = 2; number <= target; number++) {
            minOperations[0][number] = number % 3 == 0 ? findMinimunOperations(minOperations, number / 3) + 1 : Integer.MAX_VALUE;
            minOperations[1][number] = number % 2 == 0 ? findMinimunOperations(minOperations, number / 2) + 1 : Integer.MAX_VALUE;
            minOperations[2][number] = findMinimunOperations(minOperations, number - 1) + 1;
        }

        for (int number = target; number > 1;) {
            sequence.add(number);
            int qtyOperations = findMinimunOperations(minOperations, number);
            if (minOperations[0][number] == qtyOperations) {
                number = number / 3;
            } else if (minOperations[1][number] == qtyOperations) {
                number = number / 2;
            } else {
                number--;
            }
        }

        sequence.add(1);

        Collections.reverse(sequence);

        return sequence;
    }

    private static int findMinimunOperations(int[][] minOperations, int number) {
        return Math.min(Math.min(minOperations[0][number], minOperations[1][number]), minOperations[2][number]);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> sequence = optimalSequenceDP(n);
        System.out.println(sequence.size() - 1);
        for (Integer x : sequence) {
            System.out.print(x + " ");
        }
    }
}

