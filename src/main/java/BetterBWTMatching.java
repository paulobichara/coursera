import java.util.Scanner;

public class BetterBWTMatching {

    private static final int ALPHABET_SIZE = 5;

    private static class CharCounter {
        int[] count;

        CharCounter() {
             count = new int[ALPHABET_SIZE];
        }

        protected CharCounter clone() {
            CharCounter counter = new CharCounter();
            for (int index = 0; index < count.length; index++) {
                counter.count[index] = count[index];
            }
            return counter;
        }
    }

    private static class CountingSorter {

        String lastColumn;
        CharCounter[] occurrences;

        String firstColumn;
        int[] counter;
        int[] position;

        CountingSorter(String lastColumn) {
            counter = new int[ALPHABET_SIZE];
            position = new int[ALPHABET_SIZE];
            this.lastColumn = lastColumn;

            occurrences = new CharCounter[lastColumn.length() + 1];
            for (int index = 0; index < lastColumn.length(); index++) {
                if (index == 0) {
                    occurrences[index] = new CharCounter();
                } else {
                    occurrences[index] = occurrences[index - 1].clone();
                    occurrences[index].count[getCharIndex(lastColumn.charAt(index - 1))]++;
                }
                counter[getCharIndex(lastColumn.charAt(index))]++;
            }
            occurrences[lastColumn.length()] = occurrences[lastColumn.length() - 1].clone();
            occurrences[lastColumn.length()].count[getCharIndex(lastColumn.charAt(lastColumn.length() - 1))]++;

            this.firstColumn = getFirstColumn(initializePosition());
        }

        int betterBWTMatching(String pattern) {
            int patternIndex = pattern.length() - 1;
            int top = 0;
            int bottom = lastColumn.length() - 1;
            while (top <= bottom) {
                if (patternIndex >= 0) {
                    char current = pattern.charAt(patternIndex);
                    patternIndex--;
                    int charIndex = getCharIndex(current);
                    top = position[charIndex] + occurrences[top].count[charIndex];
                    bottom = position[charIndex] + occurrences[bottom + 1].count[charIndex] - 1;
                } else {
                    return bottom - top + 1;
                }
            }
            return 0;
        }

        private int[] initializePosition() {
            int[] positionAux = new int[ALPHABET_SIZE];
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

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            String transform = scanner.next();
            CountingSorter sorter = new CountingSorter(transform);

            int patternsQty = scanner.nextInt();
            StringBuilder builder = new StringBuilder();
            for (int index = 0; index < patternsQty; index++) {
                builder.append(sorter.betterBWTMatching(scanner.next())).append(" ");
            }
            System.out.println(builder.toString());
        }
    }
}
