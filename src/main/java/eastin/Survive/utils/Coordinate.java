package eastin.Survive.utils;

import eastin.Survive.objects.Entity;
import eastin.Survive.objects.Rectangle;

import java.io.Serializable;

/**
 * Created by ebricco on 9/30/17.
 */
public class Coordinate extends Entity implements Serializable {
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

    public Coordinate addX(int inc) {
        x += inc;
        return this;
    }

    public Coordinate addY(int inc) {
        y += inc;
        return this;
    }

    public void print() {
        System.out.println("coordinate located at (" + x + "," + y + ")");
    }

    public String toString() {
        return "coordinate located at (" + x + "," + y + ")";
    }

    public static Coordinate difference(Coordinate c1, Coordinate c2) {
        return new Coordinate(c1.getX() - c2.getX(), c1.getY() - c2.getY());
    }

    //maintains x:y ratio, but scales so hypotenuse is equal given value
    public void pythagoreanScale(double hypo) {

        double scaler = Math.pow(Math.pow(hypo, 2) / (Math.pow(x, 2) + Math.pow(y, 2)), .5d);

        x = (int) Math.round((double)x  * scaler);
        y = (int) Math.round((double)y  * scaler);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        return y == that.y;

    }

    //sets coordinate to point at angle a from point c
    public void rotateAboutPoint(Coordinate c, double angle) {

        //get distance from point c
        double distance = distanceTo(c);

        //get and set x and y components at new angle
        double xdiff = distance * Math.cos(Math.toDegrees(angle));
        double ydiff = distance * Math.sin(Math.toDegrees(angle));

        x = (int)Math.round(xdiff) + c.getX();
        y = (int)Math.round(ydiff) + c.getY();
    }

    //finds cartesian angle for line between origin and coordinate
    public double findAngle() {
        //TODO
        return 0d;
    }

    public boolean overlapsWith(Rectangle r) {
        if(x >= r.getLeftBound() && x <= r.getRightBound() && y <= r.getUpperBound() && y >= r.getLowerBound()) {
            return true;
        }

        return false;
    }

    public boolean overlapsWith(Entity e) {
        if(e instanceof Coordinate) {
            return equals(e);
        } else if(e instanceof Rectangle) {
            return overlapsWith((Rectangle)e);
        }

        return false;
    }
}

