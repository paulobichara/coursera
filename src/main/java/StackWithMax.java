import java.util.*;
import java.io.*;

class FastScanner {
    private StringTokenizer tok = new StringTokenizer("");
    private BufferedReader in;

    FastScanner() {
        in = new BufferedReader(new InputStreamReader(System.in));
    }

    String next() throws IOException {
        while (!tok.hasMoreElements())
            tok = new StringTokenizer(in.readLine());
        return tok.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(next());
    }
}

class StackWithMax extends Stack<Integer> {

    private Stack<Integer> maxStack = new Stack<>();

    @Override
    public Integer push(Integer item) {
        maxStack.push(maxStack.empty() ? item : Math.max(maxStack.peek(), item));
        return super.push(item);
    }

    @Override
    public synchronized Integer pop() {
        maxStack.pop();
        return super.pop();
    }

    private Integer max() {
        return maxStack.peek();
    }

    static public void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();
        int queries = scanner.nextInt();
        StackWithMax stack = new StackWithMax();
        for (int qi = 0; qi < queries; ++qi) {
            String operation = scanner.next();
            if ("push".equals(operation)) {
                int value = scanner.nextInt();
                stack.push(value);
            } else if ("pop".equals(operation)) {
                stack.pop();
            } else if ("max".equals(operation)) {
                System.out.println(stack.max());
            }
        }
    }
}
