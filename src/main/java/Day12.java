import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

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
        String initialConditions = lines.get(0).substring(15);

        int offset = 10;
        ArrayDeque<Boolean> plantState = createInitialConditions(initialConditions, offset);
        Map<List<Boolean>, Boolean> generationRules = createGenerationRules(lines.subList(2, lines.size()));
        State state = new State(plantState, offset);

        System.out.println("Part 1");
        part1(state, generationRules);
        System.out.println("------");
        System.out.println("Part 2");
        part2(state, generationRules);
    }

    private static Map<List<Boolean>, Boolean> createGenerationRules(List<String> lines) {
        Map<List<Boolean>, Boolean> generationRules = new HashMap<>();

        for (String rule : lines) {
            List<Boolean> state = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                state.add(rule.charAt(i) == '#');
            }
            boolean result = rule.charAt(9) == '#';

            generationRules.put(state, result);
        }
        return generationRules;
    }

    private static ArrayDeque<Boolean> createInitialConditions(String initialConditions, int offset) {
        ArrayDeque<Boolean> plantState = new ArrayDeque<>(Collections.nCopies(offset, Boolean.FALSE));

        for (int i = 0; i < initialConditions.length(); i++) {
            plantState.add(initialConditions.charAt(i) == '#');
        }
        plantState.addAll(Collections.nCopies(offset, Boolean.FALSE));
        return plantState;
    }

    private static void part1(State state, Map<List<Boolean>, Boolean> generationRules) {
        State currentState = state;

        for (int i = 1; i <= 20; i++) {
            currentState = nextGeneration(currentState, generationRules);
        }

        System.out.println(currentState.toString() + getSum(currentState));
    }

    private static void part2(State state, Map<List<Boolean>, Boolean> generationRules) {

        State currentState = state;
        State previousState;
        long generation = 0L;
        do {
            previousState = currentState;
            currentState = nextGeneration(currentState, generationRules);
            generation++;
            System.out.println(String.format("%1$3s", generation) + currentState.toString() + getSum(currentState));

        } while (!new ArrayList<>(previousState.getPlantPots()).equals(new ArrayList<>(currentState.getPlantPots())));

        int previousSum = getSum(previousState);
        int currentSum = getSum(currentState);

        int sumIncrement = currentSum - previousSum;

        BigInteger result = BigInteger.valueOf(50_000_000_000L - generation).multiply(BigInteger.valueOf(sumIncrement)).add(BigInteger.valueOf(currentSum));
        System.out.println(result);
    }

    private static State nextGeneration(State state, Map<List<Boolean>, Boolean> generationRules) {
        ArrayDeque<Boolean> nextState = new ArrayDeque<>(state.getPlantPots().size());
        int currentOffset = state.getOffset();

        nextState.addFirst(false);
        nextState.addFirst(false);

        Iterator<Boolean> iterator = state.getPlantPots().iterator();

        ArrayDeque<Boolean> rollingState = new ArrayDeque<>();
        IntStream.range(0, 5).forEach(n -> rollingState.add(iterator.next()));

        nextState.add(generationRules.get(new ArrayList<>(rollingState)));
        while (iterator.hasNext()) {
            rollingState.removeFirst();
            rollingState.add(iterator.next());
            nextState.add(generationRules.get(new ArrayList<>(rollingState)));
        }


        Iterator<Boolean> leftIterator = nextState.iterator();
        int leftEmptyPots = 0;
        while (!leftIterator.next()) {
            leftEmptyPots++;
        }

        if(leftEmptyPots < 10){
            IntStream.range(0, 10 - leftEmptyPots).forEach(n -> nextState.addFirst(false));
            currentOffset += 10 - leftEmptyPots;
        } else if(leftEmptyPots > 10){
            IntStream.range(0, leftEmptyPots - 10).forEach(n -> nextState.removeFirst());
            currentOffset -= leftEmptyPots - 10;
        }

        Iterator<Boolean> rightIterator = nextState.descendingIterator();
        int rightEmptyPots = 0;
        while (!rightIterator.next()) {
            rightEmptyPots++;
        }

        if(rightEmptyPots < 10){
            IntStream.range(0, 10 - rightEmptyPots).forEach(n -> nextState.addLast(false));
        } else if(rightEmptyPots > 10){
            IntStream.range(0, rightEmptyPots - 10).forEach(n -> nextState.removeLast());
        }

        return new State(nextState, currentOffset);
    }

    private static int getSum(State state) {
        Iterator<Boolean> iterator = state.getPlantPots().iterator();
        int sum = 0;
        for (int i = 0; i < state.getPlantPots().size(); i++) {
            boolean value = iterator.next();
            if (value) {
                sum += (i - state.getOffset());
            }
        }
        return sum;
    }
}

class State {
    private final ArrayDeque<Boolean> plantPots;
    private final int offset;

    State(ArrayDeque<Boolean> plantPots, int offset) {
        this.plantPots = plantPots;
        this.offset = offset;
    }

    ArrayDeque<Boolean> getPlantPots() {
        return plantPots;
    }

    int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return this.getPlantPots()
                .toString()
                .replaceAll("false", ".")
                .replaceAll("true", "#")
                .replaceAll("[, ]", "");
    }
}