import java.util.Scanner;
import java.util.stream.IntStream;

public class ChangeDP {
    private static int getChange(int money) {
        int[] coins = new int[] { 1, 3, 4 };
        int[] minCoins = new int[money + 1];

        for (int moneyIndex = 1; moneyIndex <= money; moneyIndex++) {
            final int currentMoney = moneyIndex;
            minCoins[moneyIndex] = Integer.MAX_VALUE;
            IntStream.of(coins).filter(coin -> currentMoney >= coin).forEach(coin -> {
                int numCoins = minCoins[currentMoney - coin] + 1;
                minCoins[currentMoney] = Math.min(minCoins[currentMoney], numCoins);
            });
        }

        return minCoins[money];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        System.out.println(getChange(m));

    }
}

