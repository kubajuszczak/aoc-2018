import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day03 {
    private static Path input;
    static {
        try {
            input = Paths.get(ClassLoader.getSystemResource("day03").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int[][] array = new int[1000][1000];

        for (String line : Files.readAllLines(input)){
            //#1 @ 483,830: 24x18
            int xIndex = Integer.parseInt(line.substring(line.indexOf("@") + 2, line.indexOf(",")));
            int yIndex = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(":")));
            int xSize = Integer.parseInt(line.substring(line.indexOf(":") + 2, line.indexOf("x")));
            int ySize = Integer.parseInt(line.substring(line.indexOf("x") + 1));

            for(int x = xSize -1; x >= 0; x--){
                for(int y = ySize - 1; y >= 0; y--){
                    array[xIndex + x][yIndex + y]++;
                }
            }
        }
        long result = Arrays.stream(array)
            .flatMapToInt(Arrays::stream)
            .filter(x->x>1)
            .count();

        System.out.println(result);

        mainloop: for (String line : Files.readAllLines(input)){
            //#1 @ 483,830: 24x18
            int xIndex = Integer.parseInt(line.substring(line.indexOf("@") + 2, line.indexOf(",")));
            int yIndex = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(":")));
            int xSize = Integer.parseInt(line.substring(line.indexOf(":") + 2, line.indexOf("x")));
            int ySize = Integer.parseInt(line.substring(line.indexOf("x") + 1));

            for(int x = xSize -1; x >= 0; x--){
                for(int y = ySize - 1; y >= 0; y--){
                    if(array[xIndex + x][yIndex + y]>1){
                        continue mainloop;
                    }
                }
            }
            System.out.println(line);
        }
    }

}
