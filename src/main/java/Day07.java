import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day07 {
    private static Path input;
    private static List<String> steps = IntStream.range(65, 91).mapToObj(c->String.valueOf((char) c)).collect(Collectors.toList());

    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day07").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, List<Dependency>> dependencyMap = Files.lines(input)
                .map(s -> new Dependency(s.substring(36, 37), s.substring(5, 6)))
                .collect(Collectors.groupingBy(Dependency::getStep));

        part1(dependencyMap);
        part2(dependencyMap);
    }
    private static void part1(Map<String, List<Dependency>> dependencyMap) {
        List<String> order = new ArrayList<>();

        // while there are unfinished steps
        while(order.size() != steps.size()){
            // get steps that have no unsatisfied dependencies, that haven't been added yet
            List<String> availableSteps = getAvailableSteps(dependencyMap, order);

            // add the first one alphabetically
            order.add(availableSteps.get(0));
        }
        System.out.println(String.join("", order));
    }

    private static void part2(Map<String, List<Dependency>> dependencyMap) {
        List<String> order = new ArrayList<>();

        int currentTime = 0;
        Set<Worker> workers = IntStream.range(0,5).mapToObj((i)->new Worker()).collect(Collectors.toSet());

        while (order.size() != steps.size()) {

            Set<Worker> busyWorkers = workers.stream()
                    .filter(worker -> !worker.isFree())
                    .collect(Collectors.toSet());
            OptionalInt nextFinishTime = busyWorkers.stream().mapToInt(Worker::getFinishesAt).min();
            if(nextFinishTime.isPresent()){
                currentTime = nextFinishTime.getAsInt();

                Set<Worker> workersFinishingAtNextTime = busyWorkers.stream()
                        .filter(w->w.getFinishesAt() == nextFinishTime.getAsInt())
                        .collect(Collectors.toSet());

                workersFinishingAtNextTime.forEach(worker -> worker.setFree(true));

                List<String> finishedSteps = workersFinishingAtNextTime.stream().map(Worker::getWorkingOnStep).sorted().collect(Collectors.toList());
                order.addAll(finishedSteps);
            }

            List<String> stepsInProgress = busyWorkers.stream().map(Worker::getWorkingOnStep).collect(Collectors.toList());
            List<String> availableSteps = getAvailableSteps(dependencyMap, order);
            availableSteps.removeAll(stepsInProgress);

            Set<Worker> availableWorkers = workers.stream().filter(Worker::isFree).collect(Collectors.toSet());

            if (availableSteps.size() > 0) {
                Iterator<Worker> workerIterator = availableWorkers.iterator();
                Iterator<String> stepIterator = availableSteps.iterator();
                while(workerIterator.hasNext() && stepIterator.hasNext()){
                    Worker w = workerIterator.next();
                    String step = stepIterator.next();
                    w.setFree(false);
                    w.setWorkingOnStep(step);
                    w.setFinishesAt(currentTime + stepTime(step));
                }
            }
        }
        System.out.println(currentTime);
    }

    private static List<String> getAvailableSteps(Map<String, List<Dependency>> dependencyMap, List<String> completedSteps){
        List<String> availableSteps = new ArrayList<>();
        // add all steps with no dependencies at all
        availableSteps.addAll(steps.stream()
                .filter(step -> !dependencyMap.containsKey(step))
                .filter(step -> !completedSteps.contains(step))
                .collect(Collectors.toList()));

        availableSteps.addAll(steps.stream()
                .filter(dependencyMap::containsKey) // add steps which have dependencies
                .filter(step -> hasSatisfiedDependencies(step, completedSteps, dependencyMap))
                .filter(step -> !completedSteps.contains(step))
                .collect(Collectors.toList()));

        Collections.sort(availableSteps);
        return availableSteps;
    }

    private static int stepTime(String step) {
        return step.charAt(0) - 64 + 60;
    }

    private static boolean hasSatisfiedDependencies(String step, List<String> completedSteps, Map<String, List<Dependency>> dependencyMap){
        return completedSteps.containsAll(dependencyMap.get(step).stream().map(Dependency::getDependsOn).collect(Collectors.toList()));
    }
}

class Worker {
    private boolean isFree = true;
    private String workingOnStep;
    private int finishesAt;

    boolean isFree() {
        return isFree;
    }

    void setFree(boolean free) {
        isFree = free;
    }

    String getWorkingOnStep() {
        return workingOnStep;
    }

    void setWorkingOnStep(String workingOnStep) {
        this.workingOnStep = workingOnStep;
    }

    int getFinishesAt() {
        return finishesAt;
    }

    void setFinishesAt(int finishesAt) {
        this.finishesAt = finishesAt;
    }
}

class Dependency {
    private final String step;
    private final String dependsOn;

    String getStep() {
        return step;
    }

    String getDependsOn() {
        return dependsOn;
    }

    Dependency(String step, String dependsOn) {
        this.step = step;
        this.dependsOn = dependsOn;
    }
}