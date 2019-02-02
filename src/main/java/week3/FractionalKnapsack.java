package week3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FractionalKnapsack {
    private static double getOptimalValue(long capacity, List<Double> values, List<Long> weights) {
        List<Double> costPerWeight = new ArrayList<>(values.size());

        long capacityLeft = capacity;
        double totalValue = 0;


        for (int i = 0; i < values.size(); i++) {
            costPerWeight.add(values.get(i) / weights.get(i));
        }

        while (capacityLeft > 0 && costPerWeight.size() > 0) {
            int bestIndex = findIndexBestCostPerWeight(costPerWeight);

            long itemQty = capacityLeft > weights.get(bestIndex) ? weights.get(bestIndex) : capacityLeft;
            capacityLeft -= itemQty;
            totalValue += itemQty * costPerWeight.get(bestIndex);

            if (capacityLeft > 0) {
                costPerWeight.remove(bestIndex);
                values.remove(bestIndex);
                weights.remove(bestIndex);
            }
        }

        return totalValue;
    }

    private static int findIndexBestCostPerWeight(List<Double> costPerWeight) {
        int bestIndex = -1;
        for (int i = 0; i < costPerWeight.size(); i++) {
            double current = costPerWeight.get(i);
            bestIndex = bestIndex == -1 || costPerWeight.get(bestIndex) < current ? i : bestIndex;
        }
        return bestIndex;
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long capacity = scanner.nextInt();
        List<Double> values = new ArrayList<>(n);
        List<Long> weights = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            values.add(i, scanner.nextDouble());
            weights.add(i, scanner.nextLong());
        }
        System.out.println(getOptimalValue(capacity, values, weights));
    }
} 
