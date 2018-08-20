package eastin.Survive.objects;

import eastin.Survive.utils.Coordinate;

/**
 * Created by ebricco on 8/7/18.
 *
 * represents a line from one coordinate to another
 *
 */
public class Line extends Entity {
    Coordinate s;
    Coordinate t;

    private Double distance;
    private Double slope;
    private Double intercept;

    public Line(Coordinate s, Coordinate t) {
        this.s = s;
        this.t = t;
    }

    public double getDistance() {
        if(distance == null) {
            distance = s.distanceTo(t);
        }

        return distance;
    }

    public void ensureSlopeAndInterceptInitialized() {
        if(slope == null) {
            Coordinate diff = Coordinate.difference(t, s);

            slope = ((double)diff.getY()) / ((double)diff.getX());

            if(Double.isInfinite(slope)) {
                intercept = Double.NaN;
            } else {
                intercept = s.getY() - (slope * (double) s.getX());
            }
        }
    }


    public boolean overlapsWith(RectangularObject r) {

        if(!r.overlapsWith(getEnclosingRect())) {
            return false;
        }

        if(Double.isInfinite(getSlope())) {

            //System.out.println(s.getX() >= r.getLeftBound());
            //System.out.println(s.getX() <= r.getRightBound());

            if(s.getX() >= r.getLeftBound() && s.getX() <= r.getRightBound()) {
                return true;
            } else {
                return false;
            }
        }

        int leftY = (int)getYAt(r.getLeftBound());
        int rightY = (int)getYAt(r.getRightBound());


        //if crosses upper bound, crosses lower bound, or stays between OR line is vertical and slope is finite and the x value is between left and right return true
        //crosses bound if starts higher and goes lower or starts lower and goes higher
        if(leftY >= r.getUpperBound() && rightY <= r.getUpperBound() || leftY <= r.getUpperBound() && rightY >= r.getUpperBound()
                || leftY <= r.getUpperBound() && leftY >= r.getLowerBound() && rightY <= r.getUpperBound() && rightY >= r.getLowerBound()
                || leftY >= r.getUpperBound() && rightY <= r.getLowerBound() || leftY <= r.getLowerBound() && rightY >= r.getLowerBound()) {
            return true;
        }

        return false;
    }

    public double getYAt(int x) {

        if(Double.isInfinite(getSlope())) {
            throw new RuntimeException("line with infinite slope cannot be evalulated as a function");
        }
        return (getSlope() * (double)x) + getIntercept();
    }

    @Override
    public boolean overlapsWith(Entity e) {
        if(e instanceof RectangularObject) {
            return overlapsWith((RectangularObject) e);
        } else {
            return false;
        }
    }

    public Double getSlope() {
        ensureSlopeAndInterceptInitialized();
        return slope;
    }

    public Double getIntercept() {
        ensureSlopeAndInterceptInitialized();
        return intercept;
    }

    public RectangularObject getEnclosingRect() {
        return new RectangularObject(Math.min(s.getX(), t.getX()), Math.max(s.getX(), t.getX()), Math.max(s.getY(), t.getY()), Math.min(s.getY(), t.getY()));
    }

    public void rotateAboutPoint(Coordinate c, double angle) {

    }

    //cartesian angle if lower coordinate was origin, result 0-180
    public double findAngle() {
        return 0d;
    }

    public Coordinate getBottomLeft() {
        if(s.getY() < t.getY() || s.getX() < t.getX()) {
            return s;
        }

        return t;
    }

    public Coordinate getTopRight() {
        if(s.getY() < t.getY() || s.getX() < t.getX()) {
            return t;
        }

        return s;
    }

    @Override
    public String toString() {
        return "line with " + s.toString() + " and " + t.toString();
    }
}
