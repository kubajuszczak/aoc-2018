import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day01 {

    private static Path input;
    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day01").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() throws IOException {
        int result = Files.lines(input)
                .mapToInt(Integer::parseInt)
                .sum();
        System.out.println(result);
    }

    private static void part2() throws IOException {
        List<Integer> lines = Files.lines(input).map(Integer::parseInt).collect(Collectors.toList());

        int current = 0;
        int index = 0;

        HashSet<Integer> output = new HashSet<>(130000);
        output.add(current);

        while(true){
            int value = lines.get(index);
            current += value;

            if(output.contains(current)){
                System.out.println(current);
                break;
            }

            output.add(current);

            index++;
            index %= lines.size();
        }
    }
}
