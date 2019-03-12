package coursera.datastructures.week1;

import java.util.Scanner;
import java.util.stream.IntStream;

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

    private static class State {
        private int finished = 0;
        private int currentIndex = 0;
        private boolean processing = false;
    }

    private Queue queue;

    Processor(int queueSize) {
        queue = new Queue(queueSize);
    }

    int[] processRequests(Request[] requests) {
        int[] responses = new int[requests.length];
        State state = new State();

        for (int nowMs = 0; state.finished != requests.length; nowMs++) {
            dequeueFinished(state, nowMs, responses);
            enqueueArriving(state, nowMs, requests, responses);

            while (!queue.isEmpty() && !state.processing) {
                queue.head.value.setStartMs(nowMs);
                if (queue.head.value.getDurationMs() == 0) {
                    Request request = queue.dequeue();
                    responses[request.getArrayIndex()] = request.getStartMs();
                    state.finished++;
                } else {
                    state.processing = true;
                }
            }
        }
        return responses;
    }

    private void dequeueFinished(State state, int nowMs, int[] responses) {
        if (!queue.isEmpty() && queue.head.value.isFinished(nowMs)) {
            Request request = queue.dequeue();
            responses[request.getArrayIndex()] = request.getStartMs();
            state.finished++;
            state.processing = false;
        }
    }

    private void enqueueArriving(State state, int nowMs, Request[] requests, int[] responses) {
        while (state.currentIndex < requests.length && requests[state.currentIndex].getArrivalMs() == nowMs) {
            if (queue.isEmpty() && requests[state.currentIndex].getDurationMs() == 0) {
                responses[state.currentIndex] = nowMs;
                state.finished++;
            } else {
                try {
                    queue.enqueue(requests[state.currentIndex]);
                } catch (QueueFullException e) {
                    responses[state.currentIndex] = -1;
                    state.finished++;
                }
            }
            state.currentIndex++;
        }
    }
}

class Request {
    private int arrayIndex;
    private int arrivalMs;
    private int startMs;
    private int durationMs;

    Request(int arrayIndex, int arrivalMs, int durationMs) {
        this.arrayIndex = arrayIndex;
        this.arrivalMs = arrivalMs;
        this.durationMs = durationMs;
    }

    boolean isFinished(long nowMs) {
        return startMs + durationMs <= nowMs;
    }

    int getStartMs() {
        return startMs;
    }

    void setStartMs(int startMs) {
        this.startMs = startMs;
    }

    int getDurationMs() {
        return durationMs;
    }

    int getArrivalMs() {
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

        IntStream.of(processor.processRequests(requests)).forEach(System.out::println);
    }

}
