import java.util.Scanner;
import java.util.stream.LongStream;

class Processor {

    private static class QueueFullException extends RuntimeException {
        QueueFullException() {
            super("Queue is full!");
        }
    }

    private static class Queue {

        private static class Element {
            Request value;
            Element next;

            Element(Request value) {
                this.value = value;
            }
        }

        Element head;
        Element tail;
        int queueSize;
        int usedSlots;

        Queue(int queueSize) {
            this.queueSize = queueSize;
            head = null;
            tail = null;
            usedSlots = 0;
        }

        void enqueue(Request request) {
            if (isFull()) {
                throw new QueueFullException();
            }

            if (!isEmpty()) {
                tail.next = new Element(request);
                tail = tail.next;
            } else {
                head = new Element(request);
                tail = head;
            }

            usedSlots++;
        }

        Request dequeue() {
            Request request = null;
            if (!isEmpty()) {
                usedSlots--;
                request = head.value;
                if (head.next == null) {
                    head = null;
                    tail = null;
                } else {
                    head = head.next;
                }
            }
            return request;
        }

        boolean isEmpty() {
            return usedSlots == 0;
        }

        boolean isFull() {
            return usedSlots == queueSize;
        }
    }

    private Queue queue;
    private boolean processing;

    Processor(int queueSize) {
        queue = new Queue(queueSize);
        processing = false;
    }

    long[] processRequests(Request[] requests) {
        long[] responses = new long[requests.length];

        int index = 0;
        int finished = 0;

        for (long nowMs = 0; finished != requests.length; nowMs++) {
            if (!queue.isEmpty() && queue.head.value.isFinished(nowMs)) {
                Request request = queue.dequeue();
                responses[request.getArrayIndex()] = request.getStartMs();
                finished++;
                processing = false;
            }

            while (index < requests.length && requests[index].getArrivalMs() == nowMs) {
                if (queue.isEmpty() && requests[index].getDurationMs() == 0 && !processing) {
                    responses[index] = nowMs;
                    finished++;
                } else {
                    try {
                        queue.enqueue(requests[index]);
                    } catch (QueueFullException e) {
                        responses[index] = -1;
                        finished++;
                    }
                }
                index++;
            }

            if (!queue.isEmpty() && !processing) {
                queue.head.value.setStartMs(nowMs);
                processing = true;
            }
        }
        return responses;
    }
}

class Request {
    private int arrayIndex;
    private long arrivalMs;
    private long startMs;
    private long durationMs;

    Request(int arrayIndex, long arrivalMs, long durationMs) {
        this.arrayIndex = arrayIndex;
        this.arrivalMs = arrivalMs;
        this.durationMs = durationMs;
    }

    boolean isFinished(long nowMs) {
        return startMs + durationMs <= nowMs;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Request) {
            Request request = (Request) other;
            return arrivalMs == request.arrivalMs && durationMs == request.durationMs;
        }
        return false;
    }

    long getStartMs() {
        return startMs;
    }

    void setStartMs(long startMs) {
        this.startMs = startMs;
    }

    long getDurationMs() {
        return durationMs;
    }

    long getArrivalMs() {
        return arrivalMs;
    }

    int getArrayIndex() {
        return arrayIndex;
    }
}

class ProcessPackages {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Processor processor = new Processor(scanner.nextInt());
        Request[] requests = new Request[scanner.nextInt()];

        for (int index = 0; index < requests.length; index++) {
            scanner.nextLine();
            requests[index] = new Request(index, scanner.nextInt(), scanner.nextInt());
        }

        LongStream.of(processor.processRequests(requests)).forEach(System.out::println);
    }

}
