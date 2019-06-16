package coursera.strings.week3;

import java.util.Scanner;

public class PatternInText {

    private static void printAllOccurrences(String pattern, String text) {
        String composite = pattern + '$' + text;
        int[] prefixes = computePrefixes(composite);
        for (int index = pattern.length() + 1; index < composite.length(); index++) {
            if (prefixes[index] == pattern.length()) {
                System.out.print((index - 2 * pattern.length()) + " ");
            }
        }
    }

    private static int[] computePrefixes(String text) {
        int[] result = new int[text.length()];
        for (int index = 1, border = 0; index < text.length(); index++) {
            while (border > 0 && text.charAt(index) != text.charAt(border))
                border = result[border - 1];

            border = text.charAt(index) == text.charAt(border) ? border + 1 : 0;
            result[index] = border;
        }
        return result;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            printAllOccurrences(scanner.next(), scanner.next());
        }
    }

}
