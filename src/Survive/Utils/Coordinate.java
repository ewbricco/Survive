package Survive.Utils;

/**
 * Created by ebricco on 9/30/17.
 */
public class Coordinate {
    private int x;
    private int y;

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
        xdiff *= xdiff;

        int ydiff = (x - c.getY());
        ydiff *= ydiff;

        return Math.pow((double)(xdiff+ydiff), .5d);
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

    public void print() {
        System.out.println("coordinate located at (" + x + "," + y + ")");
    }

    public static Coordinate Difference(Coordinate c1, Coordinate c2) {
        return new Coordinate(c1.getX() - c2.getX(), c1.getY() - c2.getY());
    }
}

