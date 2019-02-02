package week3;

import java.util.Scanner;

public class Change {
    private static int getChange(int m) {
        int valueLeft = m;
        int totalCoins = 0;
        final short[] coinValues = new short[] { 10, 5, 1 };

        for (short coinValue : coinValues) {
            int coinQty = valueLeft / coinValue;
            totalCoins += coinQty;
            valueLeft -= coinValue * coinQty;
        }

        return totalCoins;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        System.out.println(getChange(m));

    }
}

