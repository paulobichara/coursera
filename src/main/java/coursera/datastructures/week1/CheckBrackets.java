package coursera.datastructures.week1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Stack;

public class CheckBrackets {

    private static class Bracket {
        char type;
        int position;

        Bracket(char type, int position) {
            this.type = type;
            this.position = position;
        }

        boolean match(char c) {
            return (type == '[' && c == ']') || (type == '{' && c == '}') || (type == '(' && c == ')');
        }
    }

    public static void main(String[] args) throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        String text = reader.readLine();

        Stack<Bracket> opening_brackets_stack = new Stack<>();
        for (int position = 0; position < text.length(); ++position) {
            char next = text.charAt(position);

            if (next == '(' || next == '[' || next == '{') {
                opening_brackets_stack.push(new Bracket(next, position));
            }

            if (next == ')' || next == ']' || next == '}') {
                if (!opening_brackets_stack.empty() && opening_brackets_stack.peek().match(next)) {
                    opening_brackets_stack.pop();
                } else {
                    System.out.print(position + 1);
                    return;
                }
            }
        }

        if (opening_brackets_stack.empty()) {
            System.out.print("Success");
        } else {
            System.out.print(opening_brackets_stack.peek().position + 1);
        }
    }
}
