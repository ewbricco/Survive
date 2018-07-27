package eastin.Survive;

import eastin.Survive.Utils.*;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by ebricco on 7/25/18.
 *
 */
public class RectangularObject {

    protected int leftBound;
    protected int rightBound;
    protected int upperBound;
    protected int lowerBound;
    protected int width;
    protected int height;

    public RectangularObject(int leftBound, int rightBound, int upperBound, int lowerBound) {
        if(rightBound <= leftBound || upperBound <= lowerBound) {
            throw new IllegalArgumentException("nonsense bounds");
        }

        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        width = rightBound - leftBound;
        height = upperBound - lowerBound;
    }

    //create rectangle with lower left corner coordinate coord
    public RectangularObject(GameCoordinate coord, int width, int height)
    {
        leftBound = coord.getX();
        rightBound = coord.getX() + width;
        upperBound = coord.getY() + height;
        lowerBound = coord.getY();
        width = rightBound - leftBound;
        height = upperBound - lowerBound;
    }

    public Coordinate getBottomLeft() {
        return new Coordinate(leftBound, lowerBound);
    }

    public int getSize() {
        return Math.max(getWidth(), getHeight());
    }

    public int getLeftBound() {
        return leftBound;
    }

    public int getRightBound() {
        return rightBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public boolean overlapsWith(RectangularObject rect) {

        boolean axisAlignedWestEast = this.getLeftBound() >= rect.getLeftBound() && this.getLeftBound() <= rect.getRightBound() ||
                this.getRightBound() >= rect.getLeftBound() && this.getRightBound() <= rect.getRightBound() ||
                this.getLeftBound() <= rect.getLeftBound() && this.getRightBound() >= rect.getRightBound();

        if (!axisAlignedWestEast) {
            return false;
        }

        boolean axisAlignedNorthSouth = this.getLowerBound() >= rect.getLowerBound() && this.getLowerBound() <= rect.getUpperBound() ||
                this.getUpperBound() >= rect.getLowerBound() && this.getUpperBound() <= rect.getUpperBound() ||
                this.getLowerBound() <= rect.getLowerBound() && this.getUpperBound() >= rect.getUpperBound();

        return axisAlignedNorthSouth;
    }

    //creates minimum rect that encloses this and given rect
    public RectangularObject createEnclosingRect(RectangularObject rect) {
        int left = Math.min(this.getLeftBound(), rect.getLeftBound());
        int right = Math.max(this.getRightBound(), rect.getRightBound());
        int up = Math.max(this.getUpperBound(), rect.getUpperBound());
        int down = Math.min(this.getLowerBound(), rect.getLowerBound());

        return new RectangularObject(left, right, up, down);
    }

    public int getWidth() {
        return rightBound - leftBound;
    }

    public int getHeight() {
        return upperBound - lowerBound;
    }

    public void transformLeftBound(int newLeftBound) {
        leftBound = newLeftBound;
        rightBound = leftBound + width;
    }

    public void transformRightBound(int newRightBound) {
        rightBound = newRightBound;
        leftBound = rightBound - width;
    }

    public void transformUpperBound(int newUpperBound) {
        upperBound = newUpperBound;
        lowerBound = upperBound - height;
    }

    public void transformLowerBound(int newLowerBound) {
        lowerBound = newLowerBound;
        upperBound = lowerBound + height;
    }

    public String toString() {
        return "Rectangle with left: " + getLeftBound() + " right: " + getRightBound() + " up: " + getUpperBound() + " low: " + getLowerBound();
    }

    //changes given direction by amount
    public void expand(Direction d, int amount) {
        if(d == Direction.NORTH) {
            upperBound += amount;
        } else if(d == Direction.SOUTH) {
            lowerBound -= amount;
        } else if(d == Direction.WEST) {
            leftBound -= amount;
        } else if(d == Direction.EAST) {
            rightBound += amount;
        }
    }

    //returns an Areaquad representing a rectangle next to this quad in direction d and size size
    public RectangularObject getAdjacentQuad(Direction d, int amount) {
        if(d == Direction.NORTH) {
            return new RectangularObject(leftBound, rightBound, upperBound + amount, lowerBound);
        } else if (d == Direction.SOUTH) {
            return new RectangularObject(leftBound, rightBound, lowerBound, lowerBound - amount);
        } else if (d == Direction.WEST) {
            return new RectangularObject(leftBound - amount, leftBound, upperBound, lowerBound);
        } else if (d == Direction.EAST) {
            return new RectangularObject(rightBound, rightBound + amount, upperBound, lowerBound);
        }

        return null;
    }

    public int getPerpendicularRange(Direction d) {
        if(d == Direction.NORTH || d==Direction.SOUTH) {
            return getWidth();
        }
        else {
            return getHeight();
        }
    }

    //returns array of distances to point north, south, west, then east
    public Map<Direction, Integer> getDistancesToPoint(Coordinate c) {
        HashMap<Direction, Integer> distances = new HashMap();

        distances.put(Direction.NORTH, Math.abs(upperBound - c.getY()));
        distances.put(Direction.SOUTH, Math.abs(c.getY() - lowerBound));
        distances.put(Direction.WEST, Math.abs(c.getX() - leftBound));
        distances.put(Direction.EAST, Math.abs(rightBound - c.getX()));

        return distances;
    }

    public void setBounds(RectangularObject rect) {
        leftBound = rect.getLeftBound();
        rightBound = rect.getRightBound();
        upperBound = rect.getUpperBound();
        lowerBound = rect.getLowerBound();
    }

    public Coordinate getCenter() {
        return new Coordinate(leftBound + (rightBound-leftBound)/2, lowerBound + (upperBound - lowerBound)/2);
    }
}
