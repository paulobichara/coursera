import java.io.PrintWriter;
import java.util.Scanner;
import java.util.stream.IntStream;

public class BuildSuffixArray {

    private static final int ALPHABET_SIZE = 5;

    private static int[] buildSuffixArray(String text) {
        int[] order = sortCharacters(text);
        int[] classes = computeCharClasses(text, order);
        int length = 1;

        while (length < text.length()) {
            order = sortDoubled(text, length, order, classes);
            classes = updateClasses(order, classes, length);
            length *= 2;
        }

        return order;
    }

    private static int[] sortCharacters(String text) {
        int[] count = new int[ALPHABET_SIZE];
        for (int index = 0; index < text.length(); index++) {
            count[getCharIndex(text.charAt(index))]++;
        }
        for (int index = 1; index < ALPHABET_SIZE; index++) {
            count[index] += count[index - 1];
        }
        int[] order = new int[text.length()];
        for (int index = text.length() - 1; index >= 0; index--) {
            int current = getCharIndex(text.charAt(index));
            count[current]--;
            order[count[current]] = index;
        }
        return order;
    }

    private static int[] computeCharClasses(String text, int[] order) {
        int[] classes = new int[text.length()];
        classes[order[0]] = 0;
        for (int index = 1; index < text.length(); index++) {
            if (text.charAt(order[index]) != text.charAt(order[index - 1])) {
                classes[order[index]] = classes[order[index - 1]] + 1;
            } else {
                classes[order[index]] = classes[order[index - 1]];
            }
        }
        return classes;
    }

    private static int[] sortDoubled(String text, int length, int[] order, int[] classes) {
        int[] count = new int[text.length()];
        for (int index = 0; index < text.length(); index++) {
            count[classes[index]]++;
        }
        for (int index = 1; index < text.length(); index++) {
            count[index] += count[index - 1];
        }
        int[] newOrder = new int[text.length()];
        for (int index = text.length() - 1; index >= 0; index--) {
            int start = (order[index] - length + text.length()) % text.length();
            int clazz = classes[start];
            count[clazz]--;
            newOrder[count[clazz]] = start;
        }
        return newOrder;
    }

    private static int[] updateClasses(int[] newOrder, int[] classes, int length) {
        int[] newClasses = new int[newOrder.length];
        newClasses[newOrder[0]] = 0;
        for (int index = 1; index < newClasses.length; index++) {
            int current = newOrder[index], previous = newOrder[index - 1];
            int middle = (current + length) % newClasses.length;
            int middlePrev = (previous + length) % newClasses.length;
            if (classes[current] != classes[previous] || classes[middle] != classes[middlePrev]) {
                newClasses[current] = newClasses[previous] + 1;
            } else {
                newClasses[current] = newClasses[previous];
            }
        }
        return newClasses;
    }

    private static int getCharIndex(char character) {
        switch (character) {
            case '$': return 0;
            case 'A': return 1;
            case 'C': return 2;
            case 'G': return 3;
            case 'T': return 4;
            default: return -1;
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in); PrintWriter writer = new PrintWriter(System.out)) {
            IntStream.of(buildSuffixArray(scanner.next())).forEach(index -> writer.write(index + " "));
        }
    }

}
