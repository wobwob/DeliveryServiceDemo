package deliveryService.utils;

import java.util.Random;

public class TestUtils {
    private static final Random random = new Random();
    private static final double RANGE_MIN = 1;
    private static final double RANGE_MAX = 200;

    public static double generateRandomDoubleValue(double rangeMin, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }

    public static double generateRandomDoubleValue() {
        return generateRandomDoubleValue(RANGE_MIN, RANGE_MAX);
    }

    public static boolean generateRandomBooleanValue() {
        return random.nextBoolean();
    }

    public static int generateRandomIntegerValue(int rangeMax) {
        return random.nextInt(rangeMax);
    }

    public static int generateRandomIntegerValue() {
        return generateRandomIntegerValue((int) RANGE_MAX);
    }
}
