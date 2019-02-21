import java.util.*;
import java.util.stream.IntStream;

public class Partition3 {

    private static int partition3(int[] souvenirs) {
        int totalValue = IntStream.of(souvenirs).sum();
        if (totalValue % 3 > 0) {
            return 0;
        }

        int targetForEach = totalValue / 3;
        int[][][] possibleToGetMax = new int[souvenirs.length + 1][targetForEach + 1][targetForEach + 1];

        for (int souvenirIndex = 0; souvenirIndex <= souvenirs.length; souvenirIndex++) {
            int souvenirValue = souvenirIndex == 0 ? 0 : souvenirs[souvenirIndex - 1];
            possibleToGetMax[souvenirIndex][souvenirValue][0] = 1;
            possibleToGetMax[souvenirIndex][0][souvenirValue] = 1;
            possibleToGetMax[souvenirIndex][0][0] = 1;
        }

        for (int index = 1; index <= souvenirs.length; index++) {
            int value = souvenirs[index - 1];
            for (int capacityP1 = 1; capacityP1 <= targetForEach; capacityP1++) {
                for (int capacityP2 = 1; capacityP2 <= targetForEach; capacityP2++) {
                    setPossibleP1P2(possibleToGetMax, index, value, capacityP1, capacityP2,
                        isPossibleToGetMax(possibleToGetMax, index, value, capacityP1, capacityP2));
                }
            }
        }

        return possibleToGetMax[souvenirs.length][targetForEach][targetForEach];
    }

    private static int isPossibleToGetMax(int[][][] possibleToGetMax, int souvenirIndex, int souvenirValue, int capacityP1,
            int capacityP2) {
        int possible = 0;

        if (capacityP1 >= souvenirValue) {
            possible = possibleToGetMax[souvenirIndex - 1][capacityP1 - souvenirValue][capacityP2];
        }

        if (capacityP2 >= souvenirValue) {
            possible = Math.max(possible, possibleToGetMax[souvenirIndex - 1][capacityP1][capacityP2 - souvenirValue]);
        }

        possible = Math.max(possible, possibleToGetMax[souvenirIndex - 1][capacityP1][capacityP2]);

        return possible;
    }

    private static void setPossibleP1P2(int[][][] possibleToGetMax, int souvenirIndex, int souvenirValue,
            int capacityP1, int capacityP2, int possible) {
        if (possible == 1) {
            possibleToGetMax[souvenirIndex][capacityP1][capacityP2] = possible;
            possibleToGetMax[souvenirIndex][capacityP2][capacityP1] = possible;

            if (capacityP1 >= souvenirValue) {
                possibleToGetMax[souvenirIndex][capacityP1 - souvenirValue][capacityP2] = possible;
                possibleToGetMax[souvenirIndex][capacityP2][capacityP1 - souvenirValue] = possible;
            }

            if (capacityP2 >= souvenirValue) {
                possibleToGetMax[souvenirIndex][capacityP1][capacityP2 - souvenirValue] = possible;
                possibleToGetMax[souvenirIndex][capacityP2 - souvenirValue][capacityP1] = possible;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }
        System.out.println(partition3(A));
    }
}

