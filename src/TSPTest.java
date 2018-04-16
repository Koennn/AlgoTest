import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class TSPTest {

    public static final int POINT_COUNT = 20;
    public static final int GRID_SIZE = 10;
    public static final int AMOUNT = 1000;

    public static final Random RANDOM = ThreadLocalRandom.current();

    public static double globalDistance;
    public static long globalTime;

    public static List<Vector> route;

    public static void main(String[] args) {
        double avgDistance = 0;
        long avgTime = 0;
        double minDistance = Double.MAX_VALUE;
        double maxDistance = 0;
        for (int i = 0; i < AMOUNT; i++) {
            runSamples();
            avgDistance += globalDistance;

            if (globalDistance > maxDistance) {
                maxDistance = globalDistance;
            }
            if (globalDistance < minDistance) {
                minDistance = globalDistance;
            }

            globalDistance = 0;
            avgTime += globalTime;
            System.out.println(globalTime);
        }

        System.out.println("average: " + Math.round((avgDistance / (double) AMOUNT) * 10.0) / 10.0);
        System.out.println("min: " + Math.round((minDistance) * 10.0) / 10.0);
        System.out.println("max: " + Math.round((maxDistance) * 10.0) / 10.0);
        System.out.println("avg time: " + Math.round((avgTime / (double) AMOUNT) * 10.0) / 10.0);
    }

    public static void runSamples() {
        route = new ArrayList<>();

        List<Vector> points = generatePoints();
        System.out.println(points);

        long time = System.currentTimeMillis();
        Vector point = points.get(0);
        do {
            System.out.println("point: " + point);
            point = nextPoint(point, points);
            route.add(point);
        } while (point != null);

        globalTime = System.currentTimeMillis() - time;
        System.out.println(Math.round(globalDistance * 10.0) / 10.0);
    }

    public static Vector nextPoint(Vector currentPoint, List<Vector> points) {
        Result result = getClosestPoint(currentPoint, points);
        if (result.isValid()) {
            points.remove(result.destination);
            globalDistance += result.distance;
            System.out.println("shortest: " + result.distance);
        }
        return result.destination;
    }

    public static Result getClosestPoint(Vector point, List<Vector> points) {
        double shortest = Double.MAX_VALUE;
        Vector next = null;
        for (Vector p : points) {
            double distance = calculateDistance(point, p);
            if (distance < shortest && !p.equals(point)) {
                shortest = distance;
                next = p;
            }
        }
        return new Result(shortest, next);
    }

    public static List<Vector> generatePoints() {
        List<Vector> points = new ArrayList<>();
        for (int i = 0; i < POINT_COUNT; i++) {
            Vector point;
            do {
                point = new Vector(RANDOM.nextInt(GRID_SIZE), RANDOM.nextInt(GRID_SIZE));
            } while (points.contains(point));
            points.add(point);
        }
        return points;
    }

    public static double calculateDistance(Vector p1, Vector p2) {
        return Math.sqrt(Math.pow(Math.abs(p2.x - p1.x), 2) + Math.pow(Math.abs(p2.y - p1.y), 2));
    }
}
