import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day12 {

    private static Path input;

    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day12").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(input);

        int size = 150;
        int offset = 10;

        String initialConditions = lines.get(0).substring(15);
        List<Boolean> plantState = new ArrayList<>(Collections.nCopies(size, Boolean.FALSE));

        for (int i = 0; i < initialConditions.length(); i++) {
            plantState.set(offset + i, initialConditions.charAt(i) == '#');
        }

        Map<List<Boolean>, Boolean> generationRules = new HashMap<>();
        for (String rule : lines.subList(2, lines.size())) {
            List<Boolean> state = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                state.add(rule.charAt(i) == '#');
            }
            boolean result = rule.charAt(9) == '#';

            generationRules.put(state, result);
        }

        part1(plantState, generationRules, offset);
    }

    private static void part1(List<Boolean> initialState, Map<List<Boolean>, Boolean> generationRules, int offset){
        printState(initialState);
        for (int i = 0; i < 20; i++) {
            initialState = nextGeneration(initialState, generationRules);
            printState(initialState);
        }

        System.out.println(getSum(initialState, offset));
    }

    private static void printState(List<Boolean> plantState) {
        System.out.println(plantState
                .toString()
                .replaceAll("false", ".")
                .replaceAll("true", "#")
                .replaceAll("[, ]", "")
        );
    }

    private static List<Boolean> nextGeneration(List<Boolean> plantState, Map<List<Boolean>, Boolean> generationRules) {
        List<Boolean> nextState = new ArrayList<>(Collections.nCopies(plantState.size(), Boolean.FALSE));
        for (int i = 2; i < plantState.size() - 2; i++) {
            nextState.set(i, generationRules.get(plantState.subList(i - 2, i + 3)));
        }

        return nextState;
    }

    private static int getSum(List<Boolean> plantState, int offset) {
        int sum = 0;
        for (int i = 0; i < plantState.size(); i++) {
            if (plantState.get(i)) {
                sum += (i - offset);
            }
        }
        return sum;
    }
}
