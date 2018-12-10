import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {
    private static Path input;
    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day10").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        List<Flare> list = Files.lines(input).map(s-> new Flare(
                Integer.parseInt(s.substring(10, 16).trim()),
                Integer.parseInt(s.substring(18, 24).trim()),
                Integer.parseInt(s.substring(36, 38).trim()),
                Integer.parseInt(s.substring(40, 42).trim())
        )).collect(Collectors.toList());

        for(int i = 1; i<10620; i++){
            list = list.stream().map(Day10::processFlare).collect(Collectors.toList());
            if(i>10600){
                System.out.println(i);
                printFlares(list);
            }
        }
    }

    private static void printFlares(List<Flare> flares){

        int widthX = 200;
        int widthY = 200;
        int[][] points = new int[widthY][widthX];

        int centerX = (int) flares.stream().map(Flare::getPositionX).mapToInt(Integer::intValue).average().getAsDouble();
        int centerY = (int) flares.stream().map(Flare::getPositionY).mapToInt(Integer::intValue).average().getAsDouble();

        int minX = centerX - widthX / 2;
        int maxX = centerX + widthX / 2;

        int minY = centerY - widthY / 2;
        int maxY = centerY + widthY / 2;

        for (Flare f : flares) {
            if(f.getPositionX() < maxX && f.getPositionX() > minX){
                if(f.getPositionY() < maxY && f.getPositionY() > minY){
                    points[f.getPositionY() - minY][f.getPositionX() - minX]++;
                }
            }
        }
        int maxPointsInLine = Arrays.stream(points).map(inner->Arrays.stream(inner).sum()).max(Integer::compareTo).get();
        System.out.println("score: "+maxPointsInLine);
        System.out.println(Arrays.deepToString(points)
                .replace("],","]\n")
                .replace(", ", "")
                .replace("0", " ")
                .replace("1", "#"));

    }

    private static Flare processFlare(Flare flare){
        return new Flare(
                flare.getPositionX() + flare.getVelocityX(),
                flare.getPositionY() + flare.getVelocityY(),
                flare.getVelocityX(),
                flare.getVelocityY());
    }
}
class Flare {
    private final int positionX;
    private final int positionY;
    private final int velocityX;
    private final int velocityY;

    int getPositionX() {
        return positionX;
    }

    int getPositionY() {
        return positionY;
    }

    int getVelocityX() {
        return velocityX;
    }

    int getVelocityY() {
        return velocityY;
    }

    Flare(int positionX, int positionY, int velocityX, int velocityY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
}
