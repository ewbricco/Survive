package eastin.Survive.objects;

import eastin.Survive.utils.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ebricco on 7/25/18.
 *
 * aligned with axes
 */
public class RectangularObject extends Entity {

    protected int leftBound;
    protected int rightBound;
    protected int upperBound;
    protected int lowerBound;
    protected int width;
    protected int height;

    public RectangularObject(RectangularObject r) {
        this(r.getLeftBound(), r.getRightBound(), r.getUpperBound(), r.getLowerBound());
    }

    public RectangularObject(int leftBound, int rightBound, int upperBound, int lowerBound) {
        if(rightBound < leftBound || upperBound < lowerBound) {
            throw new IllegalArgumentException("nonsense bounds");
        }

        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        width = rightBound - leftBound + 1;
        height = upperBound - lowerBound + 1;
    }

    //create rectangle with lower left corner coordinate coord
    public RectangularObject(Coordinate coord, int width, int height)
    {
        leftBound = coord.getX();
        rightBound = coord.getX() + width;
        upperBound = coord.getY() + height;
        lowerBound = coord.getY();
        this.width = rightBound - leftBound + 1;
        this.height = upperBound - lowerBound + 1;
    }

    public Coordinate getBottomLeft() {
        return new Coordinate(leftBound, lowerBound);
    }

    public Coordinate getTopLeft() {
        return new Coordinate(leftBound, upperBound);
    }

    public Coordinate getBottomRight() {
        return new Coordinate(rightBound, lowerBound);
    }

    public Coordinate getTopRight() {
        return new Coordinate(rightBound, upperBound);
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

    public boolean overlapsWith(Entity e) {
        if(e instanceof RectangularObject) {
            return overlapsWith((RectangularObject)e);
        } else if(e instanceof Line) {
            return ((Line)e).overlapsWith(this);
        } else if(e instanceof Coordinate) {
            return ((Coordinate)e).overlapsWith(this);
        }

        return false;
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
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void transformLeftBound(int newLeftBound) {
        leftBound = newLeftBound;
        rightBound = leftBound + width - 1;
    }

    public void transformRightBound(int newRightBound) {
        rightBound = newRightBound;
        leftBound = rightBound - width + 1;
    }

    public void transformUpperBound(int newUpperBound) {
        upperBound = newUpperBound;
        lowerBound = upperBound - height + 1;
    }

    public void transformLowerBound(int newLowerBound) {
        lowerBound = newLowerBound;
        upperBound = lowerBound + height - 1;
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
            return new RectangularObject(leftBound, rightBound, upperBound + amount, upperBound);
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

    public boolean equals(RectangularObject r) {
        return this.leftBound == r.getLeftBound() && this.rightBound == r.getRightBound() && this.upperBound == r.getUpperBound() && this.lowerBound == r.getLowerBound();
    }

    //creates a rectangle with width centered at coordinate with length going in direction d
    public static RectangularObject buildRectangleFromPointInDirection(int length, int width, Direction d, Coordinate c) {
        if(d == Direction.NORTH) {
            return new RectangularObject(c.getX() - width/2, c.getX() + width/2, c.getY() + length, c.getY());
        } else if(d == Direction.SOUTH) {
            return new RectangularObject(c.getX() - width/2, c.getX() + width/2, c.getY(), c.getY() - length);
        } else if(d == Direction.WEST) {
            return new RectangularObject(c.getX() - length, c.getX(), c.getY() + width/2, c.getY() - width/2);
        } else if(d == Direction.EAST) {
            return new RectangularObject(c.getX(), c.getX() + length, c.getY() + width/2, c.getY() - width/2);
        }

        return null;
    }

    public Coordinate getMiddleOfFace(Direction d) {
        if(d == Direction.NORTH) {
            return new Coordinate(getLeftBound() + width/2, getUpperBound());
        } else if(d == Direction.SOUTH) {
            return new Coordinate(getLeftBound() + width/2, getLowerBound());
        } else if(d == Direction.WEST) {
            return new Coordinate(getLeftBound(), getLowerBound() + height/2);
        } else if(d == Direction.EAST) {
            return new Coordinate(getRightBound(), getLowerBound() + height/2);
        }

        return null;
    }

    public Coordinate getClosestCorner(Coordinate c) {
        double min;
        Coordinate closestCorner;

        Coordinate i;
        double cur;

        i = getBottomLeft();
        min = i.distanceTo(c);
        closestCorner = i;

        //go through each corner
        i = getTopLeft();
        cur = i.distanceTo(c);
        if(cur < min) {
            closestCorner = i;
        }

        //go through each corner
        i = getTopRight();
        cur = i.distanceTo(c);
        if(cur < min) {
            closestCorner = i;
        }

        //go through each corner
        i = getBottomLeft();
        cur = i.distanceTo(c);
        if(cur < min) {
            closestCorner = i;
        }

        return closestCorner;
    }

    public void expandInAllDirections(int amount) {
        expand(Direction.NORTH, amount);
        expand(Direction.SOUTH, amount);
        expand(Direction.WEST, amount);
        expand(Direction.EAST, amount);
    }
}
