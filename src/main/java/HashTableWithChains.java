import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class HashTableWithChains {
    private static class FastScanner {
        private BufferedReader br;
        private StringTokenizer st;

        FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    private class Element {
        String value;
        Element next;
        Element previous;

        Element(String value) {
            this.value = value;
        }
    }

    private class List {
        private Element head;

        void add(String value) {
            Element element = new Element(value);
            if (head == null) {
                head = element;
            } else {
                element.next = head;
                head.previous = element;
                head = element;
            }
        }

        void remove(Element element) {
            if (element.next != null) {
                element.next.previous = element.previous;
            }
            if (element.previous != null) {
                element.previous.next = element.next;
            }
            if (element == head) {
                head = element.next;
            }
        }

        Element find(String value) {
            for (Element current = head; current != null; current = current.next) {
                if (current.value.equals(value)) {
                    return current;
                }
            }
            return null;
        }
    }

    private class HashFunction {
        static final int PRIME = 1_000_000_007;
        static final int X_FACTOR = 263;

        int cardinality;

        HashFunction(int cardinality) {
            this.cardinality = cardinality;
        }

        int calculateHash(String value) {
            double hash = 0;
            for (int index = 0; index < value.length(); index++) {
                hash = (hash + (((long) value.charAt(index)) * Math.pow(X_FACTOR, index))) % PRIME;
            }
            return (int) ((hash) % cardinality);
        }
    }

    private List[] table;

    private HashFunction hashFunction;

    private HashTableWithChains(int buckets) {
        table = new List[buckets];
        hashFunction = new HashFunction(buckets);
    }

    private void add(String value) {
        int hash = hashFunction.calculateHash(value);
        if (table[hash] == null) {
            table[hash] = new List();
        }

        if (table[hash].find(value) == null) {
            table[hash].add(value);
        }
    }

    private void del(String value) {
        int hash = hashFunction.calculateHash(value);
        if (table[hash] == null) {
            return;
        }

        Element element = table[hash].find(value);
        if (element == null) {
            return;
        }

        table[hash].remove(element);
    }

    private boolean contains(String value) {
        int hash = hashFunction.calculateHash(value);
        return table[hash] != null && table[hash].find(value) != null;
    }

    public static void main(String[] args) {
        FastScanner in = new FastScanner();
        HashTableWithChains hashTable = new HashTableWithChains(in.nextInt());
        int queryCount = in.nextInt();

        for (int index = 0; index < queryCount; index++) {
            switch (in.next()) {
                case "add":
                    hashTable.add(in.next());
                    break;
                case "find":
                    System.out.println(hashTable.contains(in.next()) ? "yes" : "no");
                    break;
                case "del":
                    hashTable.del(in.next());
                    break;
                case "check":
                    int listIndex = in.nextInt();
                    if (hashTable.table[listIndex] != null) {
                        for (Element current = hashTable.table[listIndex].head; current != null;
                            current = current.next) {
                            System.out.print(current.next == null ? current.value : current.value + " ");
                        }
                    }
                    System.out.println();
                    break;
            }
        }
    }
}
