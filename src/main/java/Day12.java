import java.io.IOException;
import java.math.BigInteger;
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

        int offset = 10;

        String initialConditions = lines.get(0).substring(15);
        ArrayDeque<Boolean> plantState = new ArrayDeque<>(Collections.nCopies(offset, Boolean.FALSE));
        List<Boolean> plantState2 = new ArrayList<>(Collections.nCopies(150, Boolean.FALSE));

        for (int i = 0; i < initialConditions.length(); i++) {
            plantState.add(initialConditions.charAt(i) == '#');
            plantState2.set(i+offset, initialConditions.charAt(i)=='#');
        }

        plantState.addLast(false);
        plantState.addLast(false);
        plantState.addLast(false);
        plantState.addLast(false);
        plantState.addLast(false);

        Map<List<Boolean>, Boolean> generationRules = new HashMap<>();
        for (String rule : lines.subList(2, lines.size())) {
            List<Boolean> state = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                state.add(rule.charAt(i) == '#');
            }
            boolean result = rule.charAt(9) == '#';

            generationRules.put(state, result);
        }

//        part1(plantState2, generationRules, offset);
        part2(plantState, generationRules, offset);
    }

    private static void part1(List<Boolean> initialState, Map<List<Boolean>, Boolean> generationRules, int offset){
        for (int i = 1; i <= 20; i++) {
            initialState = nextGeneration(initialState, generationRules);
        }

        printState(initialState, offset, 20);
        System.out.println(offset);
        System.out.println(getSum(initialState, offset));
    }

    private static void part2(ArrayDeque<Boolean> initialState, Map<List<Boolean>, Boolean> generationRules, int initialOffset){

        ArrayDeque<Boolean> currentState = initialState;
        int currentOffset = initialOffset;

        for (long i = 1; i <= 1000L; i++) {
            currentState = nextGeneration(currentState, generationRules);

            int emptyPotCount = 0;
            Iterator<Boolean> potIterator = currentState.iterator();
            while(!potIterator.next()){
                emptyPotCount++;
            }

            while(emptyPotCount > 10){
                currentState.removeFirst();
                emptyPotCount--;
                currentOffset--;
            }

            while (emptyPotCount < 10) {
                currentState.addFirst(false);
                emptyPotCount++;
                currentOffset++;
            }

            emptyPotCount = 0;
            potIterator = currentState.descendingIterator();
            while(!potIterator.next()){
                emptyPotCount++;
            }

            while(emptyPotCount < 10){
                currentState.addLast(false);
                emptyPotCount++;
            }
            printState(currentState, currentOffset, i);
        }
        // the plant pots reach steady state by generation 128
        // the value at generation 1000 is 80212
        // each generation increases the total sum by 78

        BigInteger a = new BigInteger("49999999000").multiply(BigInteger.valueOf(78)).add(BigInteger.valueOf(80212));
        System.out.println(a);
    }

    private static void printState(Collection<Boolean> plantState, int offset, long generation) {
        System.out.println(generation+plantState
                .toString()
                .replaceAll("false", ".")
                .replaceAll("true", "#")
                .replaceAll("[, ]", "") + getSum(plantState, offset)
                + ", offset "+offset
        );
    }

    private static List<Boolean> nextGeneration(List<Boolean> plantState, Map<List<Boolean>, Boolean> generationRules) {
        List<Boolean> nextState = new ArrayList<>(Collections.nCopies(plantState.size()+2, Boolean.FALSE));
        for (int i = 2; i < plantState.size() - 2; i++) {
            nextState.set(i, generationRules.get(plantState.subList(i - 2, i + 3)));
        }

        return nextState;
    }

    private static ArrayDeque<Boolean> nextGeneration(ArrayDeque<Boolean> plantState, Map<List<Boolean>, Boolean> generationRules) {
//        List<Boolean> nextState = new ArrayList<>(Collections.nCopies(plantState.size()+2, Boolean.FALSE));
        ArrayDeque<Boolean> nextState = new ArrayDeque<>(plantState.size() + 2);

        ArrayDeque<Boolean> rollingState = new ArrayDeque<>();
        rollingState.add(plantState.removeFirst());
        rollingState.add(plantState.removeFirst());
        rollingState.add(plantState.removeFirst());
        rollingState.add(plantState.removeFirst());
        rollingState.add(plantState.removeFirst());

        nextState.add(generationRules.get(new ArrayList<>(rollingState)));

        while(plantState.size() > 0){
            rollingState.removeFirst();
            rollingState.add(plantState.removeFirst());
            nextState.add(generationRules.get(new ArrayList<>(rollingState)));
        }

        nextState.addFirst(false);
        nextState.addFirst(false);
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

    private static int getSum(Collection<Boolean> plantState, int offset) {
        Iterator<Boolean> iterator= plantState.iterator();
        int sum = 0;
        for (int i = 0; i < plantState.size(); i++) {
            boolean value = iterator.next();
            if (value) {
                sum += (i - offset);
            }
        }
        return sum;
    }
}
