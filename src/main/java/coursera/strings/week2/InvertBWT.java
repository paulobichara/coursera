package coursera.strings.week2;

import java.util.Scanner;

public class InvertBWT {

    private static class CountingSorter {

        String lastColumn;
        int[] occurrenceNumbers;

        String firstColumn;
        int[] counter;
        int[] position;

        CountingSorter(String lastColumn) {
            counter = new int[5];
            position = new int[5];
            this.lastColumn = lastColumn;

            occurrenceNumbers = new int[lastColumn.length()];
            for (int index = 0; index < lastColumn.length(); index++) {
                occurrenceNumbers[index] = counter[getCharIndex(lastColumn.charAt(index))];
                counter[getCharIndex(lastColumn.charAt(index))]++;
            }

            this.firstColumn = getFirstColumn(initializePosition());
        }

        String invertBWT() {
            char currentChar = firstColumn.charAt(0);
            StringBuilder builder = new StringBuilder();
            builder.append(currentChar);
            int currentIndex = 0;

            while (builder.length() < firstColumn.length()) {
                char lastColChar = lastColumn.charAt(currentIndex);
                builder.append(lastColChar);
                currentIndex = position[getCharIndex(lastColChar)] + occurrenceNumbers[currentIndex];
            }

            return builder.reverse().toString();
        }

        private int[] initializePosition() {
            int[] positionAux = new int[5];
            for (int index = 1; index < counter.length; index++) {
                position[index] = position[index - 1] + counter[index - 1];
                positionAux[index] = position[index];
            }
            return positionAux;
        }

        private String getFirstColumn(int[] positionAux) {
            char[] sorted = new char[lastColumn.length()];
            for (int index = 0; index < lastColumn.length(); index++) {
                sorted[positionAux[getCharIndex(lastColumn.charAt(index))]] = lastColumn.charAt(index);
                positionAux[getCharIndex(lastColumn.charAt(index))]++;
            }
            return new String(sorted);
        }

        private int getCharIndex(char character) {
            switch (character) {
                case '$': return 0;
                case 'A': return 1;
                case 'C': return 2;
                case 'G': return 3;
                case 'T': return 4;
                default: return -1;
            }
        }
    }

    private static String invertBurrowsWheelerTransform(String transform) {
        CountingSorter sorter = new CountingSorter(transform);
        return sorter.invertBWT();
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println(invertBurrowsWheelerTransform(scanner.next()));
        }
    }

}
