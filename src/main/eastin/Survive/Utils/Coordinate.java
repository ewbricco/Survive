package eastin.Survive.utils;

/**
 * Created by ebricco on 9/30/17.
 */
public class Coordinate {
    protected int x;
    protected int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Coordinate c) {
        x += c.getX();
        y += c.getY();
    }

    public void subtract(Coordinate c) {
        x -= c.getX();
        x -= c.getY();
    }

    public double distanceTo(Coordinate c) {
        int xdiff = (x - c.getX());

        int ydiff = (y - c.getY());

        return Math.pow(Math.pow(xdiff, 2)+Math.pow(ydiff, 2), .5d);
    }

    public double getHypotenuse() {
        return distanceTo(new Coordinate(0,0));
    }

    //updates coordinate to be closer to the target in both x and y by distance
    public void seek(Coordinate target, int distance) {
        if (target.getX() < x) {
            x -= distance;
        } else {
            x += distance;
        }
        if (target.getY() < y) {
            y -= distance;
        } else {
            y += distance;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addX(int inc) {
        x += inc;
    }

    public void addY(int inc) {
        y += inc;
    }

    public void print() {
        System.out.println("coordinate located at (" + x + "," + y + ")");
    }

    public String toString() {
        return "coordinate located at (" + x + "," + y + ")";
    }

    public static Coordinate Difference(Coordinate c1, Coordinate c2) {
        return new Coordinate(c1.getX() - c2.getX(), c1.getY() - c2.getY());
    }

    //maintains x:y ratio, but scales so hypotenuse is equal given value
    public void pythagoreanScale(int hypo) {

        double scaler = Math.pow(Math.pow(hypo, 2) / (Math.pow(x, 2) + Math.pow(y, 2)), .5d);

        x = (int) Math.round((double)x  * scaler);
        y = (int) Math.round((double)y  * scaler);
    }
}

