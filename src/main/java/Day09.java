import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Arrays;

public class Day09 {
    private static Path input;

    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day09").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String line = Files.readAllLines(input).get(0);
        int numberOfPlayers = Integer.parseInt(line.substring(0, line.indexOf(" ")));
        int lastMarble = Integer.parseInt(line.substring(line.indexOf("worth") + 6, line.lastIndexOf(" ")));

        part1(numberOfPlayers, lastMarble);
        part1(numberOfPlayers, lastMarble * 100);
    }

    private static void part1(int numberOfPlayers, int lastMarble) {
        long[] playerScores = new long[numberOfPlayers];
        int playerIndex = 0;
        ArrayDeque<Integer> marbles = new ArrayDeque<>(lastMarble + 1);

        marbles.add(0);

        for (int marble = 1; marble <= lastMarble; marble++, playerIndex++, playerIndex %= numberOfPlayers) {
            if (marble % 23 == 0) {
                playerScores[playerIndex] += marble;
                marbles.addFirst(marbles.removeLast());
                marbles.addFirst(marbles.removeLast());
                marbles.addFirst(marbles.removeLast());
                marbles.addFirst(marbles.removeLast());
                marbles.addFirst(marbles.removeLast());
                marbles.addFirst(marbles.removeLast());
                marbles.addFirst(marbles.removeLast());
                playerScores[playerIndex] += marbles.removeLast();
                marbles.addLast(marbles.removeFirst());
            } else {
                marbles.addLast(marbles.removeFirst());
                marbles.addLast(marble);
            }
            if (marble % 100000 == 0) {
                System.out.println(Instant.now() + " " + marble);
            }
        }
        System.out.println(Arrays.stream(playerScores).max());
    }
}
