package coursera.datastructures.week2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.StringTokenizer;

public class MergingTables {
    private final InputReader reader;
    private final OutputWriter writer;

    private MergingTables(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new MergingTables(reader, writer).run();
        writer.writer.flush();
    }

    class Table {
        Table parent;
        int rank;
        int numberOfRows;

        Table(int numberOfRows) {
            this.numberOfRows = numberOfRows;
            rank = 0;
            parent = this;
        }
        Table getParent() {
            // find super parent and compress path
            if (parent != this) {
                parent = parent.getParent();
            }
            return parent;
        }
    }

    private int maximumNumberOfRows = -1;

    private void merge(Table destination, Table source) {
        Table realDestination = destination.getParent();
        Table realSource = source.getParent();
        if (realDestination == realSource) {
            return;
        }
        // merge two components here
        // use rank heuristic
        // update maximumNumberOfRows
        if (realDestination.rank > realSource.rank) {
            realSource.parent = realDestination;
            realDestination.numberOfRows += realSource.numberOfRows;
            maximumNumberOfRows = Math.max(realDestination.numberOfRows, maximumNumberOfRows);
            realSource.numberOfRows = 0;
        } else {
            realDestination.parent = realSource;
            realSource.numberOfRows += realDestination.numberOfRows;
            maximumNumberOfRows = Math.max(realSource.numberOfRows, maximumNumberOfRows);
            realDestination.numberOfRows = 0;
            if (realSource.rank == realDestination.rank) {
                realSource.rank++;
            }
        }
    }

    private void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();
        Table[] tables = new Table[n];
        for (int i = 0; i < n; i++) {
            int numberOfRows = reader.nextInt();
            tables[i] = new Table(numberOfRows);
            maximumNumberOfRows = Math.max(maximumNumberOfRows, numberOfRows);
        }
        for (int i = 0; i < m; i++) {
            int destination = reader.nextInt() - 1;
            int source = reader.nextInt() - 1;
            merge(tables[destination], tables[source]);
            writer.printf("%d\n", maximumNumberOfRows);
        }
    }


    static class InputReader {
        BufferedReader reader;
        StringTokenizer tokenizer;

        InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class OutputWriter {
        PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}
