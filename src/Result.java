public class Result {

    public double distance;
    public Vector destination;

    public Result(double distance, Vector destination) {
        this.distance = distance;
        this.destination = destination;
    }

    public boolean isValid() {
        return this.destination != null;
    }
}
