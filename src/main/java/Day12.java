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

        part1(state, generationRules);
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

        for (long i = 1; i <= 200L; i++) {
            currentState = nextGeneration(currentState, generationRules);
            System.out.println(i + currentState.toString() + getSum(currentState));
        }

        // the plant pots reach steady state by generation 128
        // the value at generation 1000 is 80212
        // each generation increases the total sum by 78

        BigInteger a = new BigInteger("49999999000").multiply(BigInteger.valueOf(78)).add(BigInteger.valueOf(80212));
        System.out.println(a);
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