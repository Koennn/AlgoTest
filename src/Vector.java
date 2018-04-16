public class Vector {

    public int x;
    public int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("{%s, %s}", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }
        return ((Vector) obj).x == this.x && ((Vector) obj).y == this.y;
    }
}
