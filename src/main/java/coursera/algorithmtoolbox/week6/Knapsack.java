package coursera.algorithmtoolbox.week6;

import java.util.*;

public class Knapsack {

    private static int optimalWeight(int capacity, int[] bars) {
        int[][] optimalWeights = new int[capacity + 1][bars.length + 1];

        for (int barIndex = 1; barIndex <= bars.length; barIndex++) {
            for (int currentCapacity = 1; currentCapacity <= capacity; currentCapacity++) {
                optimalWeights[currentCapacity][barIndex] = optimalWeights[currentCapacity][barIndex - 1];
                int currentBarWeight = bars[barIndex - 1];
                if (currentCapacity >= currentBarWeight) {
                    int maxValue = Math.max(optimalWeights[currentCapacity][barIndex],
                        optimalWeights[currentCapacity - currentBarWeight][barIndex - 1] + currentBarWeight);
                    optimalWeights[currentCapacity][barIndex] = maxValue;
                }
            }
        }

        return optimalWeights[capacity][bars.length];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int W, n;
        W = scanner.nextInt();
        n = scanner.nextInt();
        int[] w = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = scanner.nextInt();
        }
        System.out.println(optimalWeight(W, w));
    }
}

