import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Day04 {
    private static Path input;
    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day04sorted").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static Shift processEvents (List<Event> events) {
        boolean awake = true;
        int[] minutes = new int[60];

        for (int i = 0; i<60;i++){
            for(Event event : events){
                if(event.time == i){
                    awake = event.content.equals("Awake");
                }
            }
            minutes[i] = awake ? 0:1;
        }

        return new Shift(minutes);
    }

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(input);

        List<Event> currentEvents = new ArrayList<>();
        HashMap<String, List<Shift>> guardMap = new HashMap<>();

        String currentGuardId = null;
        for(String line : lines) {
            int currentMinute = Integer.parseInt(line.substring(15, 17));
            if(line.substring(19).startsWith("Guard")){
                if(currentGuardId != null){

                    Shift shift = processEvents(currentEvents);
                    if(!guardMap.containsKey(currentGuardId)) {
                        guardMap.put(currentGuardId, new ArrayList<>());
                    }

                    guardMap.get(currentGuardId).add(shift);
                }

                currentGuardId = line.substring(25, line.indexOf(" ", 25));
                currentEvents = new ArrayList<>();
            } else if (line.substring(19).startsWith("falls")) {
                // set asleep as of this minute
                currentEvents.add(new Event("Sleep", currentMinute));
            } else if (line.substring(19).startsWith("wakes")) {
                // set awake as of this minute
                currentEvents.add(new Event("Awake", currentMinute));
            } else {
                // say what now?
                throw new RuntimeException();
            }

        }

        part1(guardMap);
        part2(guardMap);

    }
    private static void part1(Map<String, List<Shift>> guardMap){
        HashMap<String, Integer> guardSleepTotals = new HashMap<>();
        for(String guard : guardMap.keySet()){
            int total = guardMap.get(guard).stream()
                    .flatMapToInt(shift->Arrays.stream(shift.minutes))
                    .sum();
            guardSleepTotals.put(guard, total);
        }
        Map.Entry<String, Integer> maxSleepGuard = guardSleepTotals
                .entrySet()
                .stream()
                .reduce((g1, g2)->g1.getValue()>g2.getValue()?g1:g2)
                .get();
        System.out.println(maxSleepGuard);

        String maxSleepGuardId = maxSleepGuard.getKey();

        List<Shift> maxSleepGuardShifts = guardMap.get(maxSleepGuardId);
        int maxIndex = getMaxSleptMinute(maxSleepGuardShifts).maxMinute;
//        System.out.println(Arrays.toString(minutes));
        System.out.println(maxIndex);

        System.out.println(Integer.parseInt(maxSleepGuardId.substring(1))*maxIndex);
    }

    private static GuardStats getMaxSleptMinute(List<Shift> shifts){
        int[] minutes = new int[60];
        for(Shift shift : shifts) {
            for(int i = 0; i<60; i++){
                minutes[i]+= shift.minutes[i];
            }
        }
        int maxMinute = 0;
        for(int i =0; i<60; i++){
            if(minutes[i]>minutes[maxMinute]) {
                maxMinute = i;
            }
        }
        return new GuardStats(maxMinute, minutes[maxMinute]);
    }
    private static void part2(Map<String, List<Shift>> guardMap){
        System.out.println("part2");

        Map<String, GuardStats> maxMap = new HashMap<>();
        for(String guard : guardMap.keySet()){
            maxMap.put(guard, getMaxSleptMinute(guardMap.get(guard)));
        }

        Map.Entry<String, GuardStats> max = maxMap.entrySet().stream().reduce((g1, g2)->g1.getValue().maxMinuteCount>g2.getValue().maxMinuteCount?g1:g2).get();
        System.out.println(max.getKey());
        System.out.println(max.getValue().maxMinute);
        System.out.println(Integer.parseInt(max.getKey().substring(1))*max.getValue().maxMinute);
    }
}

class GuardStats{
    int maxMinute;
    int maxMinuteCount;

    GuardStats(int maxMinute, int maxMinuteCount) {
        this.maxMinute = maxMinute;
        this.maxMinuteCount = maxMinuteCount;
    }

    @Override
    public String toString() {
        return "GuardStats{" +
                "maxMinute=" + maxMinute +
                ", maxMinuteCount=" + maxMinuteCount +
                '}';
    }
}
class Shift {
    int[] minutes;

    Shift(int[] minutes){
        this.minutes = minutes;
    }
}

class Event {
    String content;
    int time;

    Event(String content, int time) {
        this.content = content;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Event{" +
                "content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}
