import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class Day05 {
    private static Path input;

    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day05").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private static ReactionResult react(String s) {
        int reactionCount = 0;

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < s.length(); i++) {
            char a = s.charAt(i - 1);
            char b = s.charAt(i);

            if (b >= 97) {
                if (a == (b - 32)) {
                    i++;
                    reactionCount++;
                } else {
                    builder.append(a);
                }
            } else {
                if (a == (b + 32)) {
                    i++;
                    reactionCount++;
                } else {
                    builder.append(a);
                }
            }
        }
        builder.append(s.charAt(s.length() - 1));
        return new ReactionResult(builder.toString(), reactionCount);
    }

    private static int part1(String polymer) {
        ReactionResult r = react(polymer);
        while (r.reactionCount > 0) {
            r = react(r.polymer);
        }
        return r.polymer.length();
    }

    private static int part2(String s) {
        Set<Integer> set = new HashSet<>();
        for (int i = 65; i < 65 + 26; i++) {
            String letter = Character.toString((char) i);
            set.add(part1(s.replace(letter.toLowerCase(), "").replace(letter.toUpperCase(), "")));
        }
        return set.stream().mapToInt(Integer::intValue).min().getAsInt();
    }

    public static void main(String[] args) throws IOException {
        String s = Files.readAllLines(input).get(0);

        System.out.println("part 1: " + part1(s));
        System.out.println("part 2: " + part2(s));
    }

}

class ReactionResult {
    String polymer;
    int reactionCount;

    public ReactionResult(String polymer, int reactionCount) {
        this.polymer = polymer;
        this.reactionCount = reactionCount;
    }
}