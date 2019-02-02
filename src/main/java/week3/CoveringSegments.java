package week3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CoveringSegments {

    private static List<Long> optimalPoints(List<Segment> segments) {
        Segment segmentForPoint = new Segment(Long.MIN_VALUE, Long.MAX_VALUE);
        List<Long> points = new ArrayList<>();

        while (segments.size() > 0) {
            Segment current = segments.stream().min(Comparator.comparingLong(segment -> segment.start)).get();

            if (isPointInsideSegment(current.start, segmentForPoint)) {
                segmentForPoint.start = current.start;
                if (isPointInsideSegment(current.end, segmentForPoint)) {
                    segmentForPoint.end = current.end;
                }
            } else {
                points.add(segmentForPoint.end);
                segmentForPoint = new Segment(current.start, current.end);
            }

            if (segments.size() == 1) {
                points.add(segmentForPoint.end);
            }
            segments.remove(current);
        }
        return points;
    }

    private static boolean isPointInsideSegment(long point, Segment segment) {
        return point >= segment.start && point <= segment.end;
    }

    private static class Segment {
        long start, end;

        Segment(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Segment> segments = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int start, end;
            start = scanner.nextInt();
            end = scanner.nextInt();
            segments.add(i, new Segment(start, end));
        }
        List<Long> points = optimalPoints(segments);
        System.out.println(points.size());
        for (Long point : points) {
            System.out.print(point + " ");
        }
    }
}
 
