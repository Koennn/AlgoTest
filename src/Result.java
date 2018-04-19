import java.util.Arrays;

public class Result {

    public double distance;
    public Vector[] destinations;

    public Result(double distance, Vector... destination) {
        this.distance = distance;
        this.destinations = destination;
    }

    public boolean isValid() {
        return this.destinations != null && this.destinations.length > 0 && this.destinations[0] != null;
    }

    @Override
    public String toString() {
        return String.format("{distance=%s, destinations=%S}", this.distance, Arrays.toString(this.destinations));
    }
}
