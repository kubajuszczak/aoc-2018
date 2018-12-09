import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day06 {
    private static Path input;

    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day06").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        List<Point> list = Files.lines(input)
                .map(s -> new Point(Integer.parseInt(s.substring(0, s.indexOf(","))), Integer.parseInt(s.substring(s.indexOf(" ") + 1))))
                .collect(Collectors.toList());

        HashSet<Integer> areas = new HashSet<>(part1(list, -1000, 1001));
        // do it again with a larger grid to exclude infinite areas
        areas.retainAll(part1(list, -2000, 2001));
        System.out.println("part 1:");
        System.out.println(areas.stream().max(Integer::compareTo));


        System.out.println("part 2:");
        System.out.println(part2(list));

    }

    private static Set<Integer> part1(List<Point> input, int minValue, int maxValue) {
        // initialize a suitably large grid of points
        Map<Point, List<Distance>> map = IntStream
                .range(minValue, maxValue)
                .boxed()
                .flatMap(x -> IntStream.range(minValue, maxValue)
                        .mapToObj(y -> new Point(x, y)))

                // calculate distance to each of the points in the list
                .map(gridPoint ->
                        input.stream()
                                .map(gridPoint::taxicabDistance)
                                .reduce(Distance.IDENTITY, (distance, distance2) -> {
                                    if (distance.magnitude == distance2.magnitude) {
                                        return new Distance(null, distance.magnitude);
                                    } else if (distance.magnitude < distance2.magnitude) {
                                        return distance;
                                    } else {
                                        return distance2;
                                    }
                                }))
                .filter(d -> d.p != null)
                .collect(Collectors.groupingBy(Distance::getPoint));
        // count the number of grid points closest to each list point
        return map.values().stream().map(List::size).collect(Collectors.toSet());
    }

    private static long part2(List<Point> input) {
        return IntStream
                .range(-2000, 2001)
                .boxed()
                .flatMap(x -> IntStream.range(-2000, 2001)
                        .mapToObj(y -> new Point(x, y)))
                // calculate distance to each of the points in the list
                .map(gridPoint ->
                        input.stream()
                                .map(gridPoint::taxicabDistance).mapToInt(d -> d.magnitude).sum())
                .filter(area -> area < 10000)
                .count();
    }
}

class Point {
    private final int x;
    private final int y;

    Distance taxicabDistance(Point p) {
        return new Distance(p, Math.abs(this.x - p.x) + Math.abs(this.y - p.y));
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

class Distance {
    static Distance IDENTITY = new Distance(null, 9999);
    final Point p;
    final int magnitude;

    Point getPoint() {
        return p;
    }

    Distance(Point p, int magnitude) {
        this.p = p;
        this.magnitude = magnitude;
    }
}
