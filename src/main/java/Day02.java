import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Day02 {

    private static Path input;
    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day02").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static boolean countRepeats(String word, int target) {
        int[] letterCounts = new int[26];
        for (char c: word.toCharArray()) {
            letterCounts[c - 97]++;
        }
        for(int count : letterCounts) {
            if (count == target){
                return true;
            }
        }
        return false;
    }

    private static void part1() throws IOException {
        long result2 = Files.lines(input)
            .filter(s -> countRepeats(s, 2))
                .count();

        long result3 = Files.lines(input)
                .filter(s -> countRepeats(s, 3))
                .count();
        System.out.println(result2 * result3);
    }

    private static int countDifferences(String input1, String input2){
        int count = 0;
        for(int i = 0; i < input1.length(); i++){
            if(input1.toCharArray()[i]!=input2.toCharArray()[i]){
                count++;
            }
        }
        return count;
    }

    private static void part2() throws IOException {
        List<String> lines = Files.readAllLines(input);
        for (int i = 0; i < lines.size(); i++){
            for(int j = i + 1; j < lines.size(); j++){
                if(countDifferences(lines.get(i), lines.get(j))==1){
                    System.out.println(lines.get(i));
                    System.out.println(lines.get(j));
                    return;
                }
            }
        }
    }

}
