import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class TSPTest {

    public static final int POINT_COUNT = 10;
    public static final int GRID_SIZE = 10;
    public static final int AMOUNT = 10000;

    public static final Random RANDOM = ThreadLocalRandom.current();

    public static double globalDistance;
    public static long globalTime;

    public static List<Vector> route;

    public static void main(String[] args) {
        List<List<Vector>> sets = new ArrayList<>();
        for (int i = 0; i < AMOUNT; i++) {
            sets.add(generatePoints());
        }

        double avgDistance = 0;
        long avgTime = 0;
        double minDistance = Double.MAX_VALUE;
        double maxDistance = 0;
        for (int i = 0; i < AMOUNT; i++) {
            runSamples(new ArrayList<>(sets.get(i)), true);
            avgDistance += globalDistance;

            if (globalDistance > maxDistance) {
                maxDistance = globalDistance;
            }
            if (globalDistance < minDistance) {
                minDistance = globalDistance;
            }

            globalDistance = 0;
            avgTime += globalTime;
            //System.out.println(globalTime);
        }

        System.out.println("NN");
        System.out.println("average: " + Math.round((avgDistance / (double) AMOUNT) * 10.0) / 10.0);
        System.out.println("min: " + Math.round((minDistance) * 10.0) / 10.0);
        System.out.println("max: " + Math.round((maxDistance) * 10.0) / 10.0);
        System.out.println("avg time: " + Math.round((avgTime / (double) AMOUNT) * 10.0) / 10.0);

        avgDistance = 0;
        avgTime = 0;
        minDistance = Double.MAX_VALUE;
        maxDistance = 0;
        globalDistance = 0;
        globalTime = 0;
        for (int i = 0; i < AMOUNT; i++) {
            runSamples(new ArrayList<>(sets.get(i)), false);
            avgDistance += globalDistance;

            if (globalDistance > maxDistance) {
                maxDistance = globalDistance;
            }
            if (globalDistance < minDistance) {
                minDistance = globalDistance;
            }

            globalDistance = 0;
            avgTime += globalTime;
            //System.out.println(globalTime);
        }

        System.out.println();
        System.out.println("N2N");
        System.out.println("average: " + Math.round((avgDistance / (double) AMOUNT) * 10.0) / 10.0);
        System.out.println("min: " + Math.round((minDistance) * 10.0) / 10.0);
        System.out.println("max: " + Math.round((maxDistance) * 10.0) / 10.0);
        System.out.println("avg time: " + Math.round((avgTime / (double) AMOUNT) * 10.0) / 10.0);
    }

    public static void runSamples(List<Vector> points, boolean nn) {
        route = new ArrayList<>();

        //System.out.println(points);
        //globalDistance = 0;

        long time = System.currentTimeMillis();
        Vector point = points.get(0);
        route.add(point);
        points.remove(point);
        do {
            //System.out.println("point: " + point);
            if (nn) {
                point = nextPointNN(point, points);
            } else {
                point = nextPointN2N(point, points);
            }
            route.add(point);
        } while (point != null);

        globalTime = System.currentTimeMillis() - time;
        //System.out.println("distance: " + Math.round(globalDistance * 10.0) / 10.0);
    }

    public static Vector nextPointNN(Vector currentPoint, List<Vector> points) {
        Result result = getClosestPoint(currentPoint, points);
        if (result.isValid()) {
            points.remove(result.destinations[0]);
            if (result.distance < Math.pow(2, 10)) {
                globalDistance += result.distance;
            }
            //System.out.println("shortest: " + result.distance);
        }
        return result.destinations[0];
    }

    public static Vector nextPointN2N(Vector currentPoint, List<Vector> points) {
        Result result = getClosestPoints(currentPoint, points);
        if (result.isValid()) {
            if (result.destinations.length == 1) {
                points.remove(result.destinations[0]);
                globalDistance += calculateDistance(currentPoint, result.destinations[0]);
                return result.destinations[0];
            }

            double shortest = Double.MAX_VALUE;
            Vector next = null;
            Vector next2 = null;
            for (Vector destination : result.destinations) {
                Result result2 = getClosestPoint(destination, points);
                if (result2.isValid()) {
                    double distance = calculateDistance(currentPoint, destination) + result2.distance;
                    if (distance < shortest && !result2.destinations[0].equals(destination)) {
                        shortest = distance;
                        next = destination;
                        next2 = result2.destinations[0];
                    }
                } else {
                    points.remove(next);
                    globalDistance += calculateDistance(currentPoint, next);
                    return next;
                }
            }
            if (next != null) {
                points.remove(next);
                route.add(next);
                points.remove(next2);
                globalDistance += shortest;
            }
            return next2;
        }
        return null;
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

    public static Result getClosestPoints(Vector point, List<Vector> points) {
        List<Vector> tmp = new ArrayList<>(points);
        Result result = getClosestPoint(point, tmp);
        if (result.isValid()) {
            tmp.remove(result.destinations[0]);
            Result result2 = getClosestPoint(point, tmp);
            if (result2.isValid()) {
                return new Result(result.distance, result.destinations[0], result2.destinations[0]);
            }
            return result;
        }
        return result;
    }

    public static List<Vector> generatePoints() {
        List<Vector> points = new ArrayList<>();
        points.add(new Vector(0, 0));
        for (int i = 1; i < POINT_COUNT; i++) {
            Vector point;
            do {
                point = new Vector(RANDOM.nextInt(GRID_SIZE), RANDOM.nextInt(GRID_SIZE));
            } while (points.contains(point));
            points.add(point);
        }
        return points;
    }

    public static double calculateDistance(Vector p1, Vector p2) {
        return (p1 != null && p2 != null) ? Math.sqrt(Math.pow(Math.abs(p2.x - p1.x), 2) + Math.pow(Math.abs(p2.y - p1.y), 2)) : Double.MAX_VALUE;
    }
}
