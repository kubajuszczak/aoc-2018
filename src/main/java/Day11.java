import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;

public class Day11 {
    public static void main(String[] args) {
        int gridSerial = 1723;

        int[][] power = new int[300][300];
        for (int x = 1; x <= 300; x++) {
            for (int y = 1; y <= 300; y++) {
                power[x - 1][y - 1] = calculatePower(x, y, gridSerial);
            }
        }

        part1(power);
        part2(power);
    }

    private static void part1(int[][] power) {
        Optional<SquarePower> bestSquare = IntStream
                .range(1, 299)
                .boxed()
                .flatMap(x -> IntStream
                        .range(1, 299)
                        .mapToObj(y -> findSquarePower(power, x, y, 3)
                        )
                )
                .max(Comparator.comparing(SquarePower::getPower));


        if (bestSquare.isPresent()) {
            SquarePower s = bestSquare.get();
            System.out.println("Best 3x3 square is " + s.getX() + "," + s.getY() + " with a total power of " + s.getPower());
        }
    }

    private static void part2(int[][] power) {
        Optional<SquarePower> bestSquare = IntStream
                .range(1, 301)
                .boxed()
                .flatMap(size -> IntStream
                        .range(1, 302 - size)
                        .boxed()
                        .flatMap(x -> IntStream
                                .range(1, 302 - size)
                                .mapToObj(y -> findSquarePower(power, x, y, size))
                        )
                )
                .max(Comparator.comparing(SquarePower::getPower));


        if (bestSquare.isPresent()) {
            SquarePower s = bestSquare.get();
            System.out.println("Best square is " + s.getX() + "," + s.getY() + " of size " + s.getSize() + "x" + s.getSize() + " with a total power of " + s.getPower());
        }

    }

    private static SquarePower findSquarePower(int[][] powerLevels, int x, int y, int size) {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sum += powerLevels[x + i - 1][y + j - 1];
            }
        }
        return new SquarePower(x, y, sum, size);
    }

    private static int calculatePower(int x, int y, int gridSerial) {
        int rackId = x + 10;
        int powerLevel = rackId * y;
        powerLevel += gridSerial;
        powerLevel *= rackId;

        powerLevel %= 1000;
        powerLevel = (powerLevel - (powerLevel % 100)) / 100;

        powerLevel -= 5;
        return powerLevel;
    }

}

class SquarePower {
    private int x;
    private int y;
    private int power;
    private int size;

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getPower() {
        return power;
    }

    int getSize() {
        return size;
    }

    SquarePower(int x, int y, int power, int size) {
        this.x = x;
        this.y = y;
        this.power = power;
        this.size = size;
    }
}
