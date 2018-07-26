package eastin.Survive;

import eastin.Survive.Utils.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by ebricco on 7/25/18.
 *
 * note: need to account for center pixel
 */
public class RectangularObject {
    protected GameCoordinate coord;
    protected int width; //right now only represented the radius
    protected int height;
    protected Color color;

    //private int layer;

    public RectangularObject(GameCoordinate coord, int size, Color color) {
        this.coord = coord;
        width = size;
        height = size;
        this.color = color;
    }

    //real width is given width*2 + 1
    public RectangularObject(GameCoordinate coord, int width, int height, Color color) {
        this.coord = coord;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    //create from bounds
    public RectangularObject(int leftBound, int rightBound, int upperBound, int lowerBound, Color color) {
        this(new GameCoordinate(((rightBound - leftBound)/2) + leftBound, ((upperBound - lowerBound)/2) + lowerBound), (rightBound - leftBound)/2, (upperBound - lowerBound)/2, color);
        Coordinate c = new GameCoordinate(((rightBound - leftBound)/2) + leftBound, ((upperBound - lowerBound)/2) + lowerBound);
        System.out.println("creating rect with " + c.toString() + " width: " + (rightBound - leftBound)/2 + " height: " + (upperBound - lowerBound)/2);

        if(rightBound <= leftBound || upperBound <= lowerBound) {
            throw new IllegalArgumentException("nonsense bounds");
        }
    }


    //if any portion of the object is in the screen, render
    public void checkPositionAndRender() {
        AreaQuad screen = Runner.mc.getAreaQuad();

        //if no portion is in given quad, don't render
        if(coord.getX()+width < screen.getWesternFrontier() || coord.getX()-width > screen.getEasternFrontier()){
            return;
        }
        if(coord.getY()+height < screen.getSouthernFrontier() || coord.getY()-height > screen.getNorthernFrontier()){
            return;
        }

        render(Coordinate.Difference(coord, screen.getBottomLeft()));
    }

    //object will be of width width*2 + 1, same for height
    public void render(Coordinate renderCoord) {
        color.setColor();
        glBegin(GL_QUADS);
        {
            glVertex2f(renderCoord.getX()-width,renderCoord.getY()-height);
            glVertex2f(renderCoord.getX()+width,renderCoord.getY()-height);
            glVertex2f(renderCoord.getX()+width,renderCoord.getY()+height);
            glVertex2f(renderCoord.getX()-width,renderCoord.getY()+height);
        }
        glEnd();
    }

    public GameCoordinate getCoord() {
        return coord;
    }

    public int getSize() {
        return Math.max(width, height);
    }

    public int getLeftBound() {
        return coord.getX() - width;
    }

    public int getRightBound() {
        return coord.getX() + width;
    }

    public int getUpperBound() {
        return coord.getY() + height;
    }

    public int getLowerBound() {
        return coord.getY() - height;
    }

    public boolean overlapsWith(RectangularObject rect) {

        //if upper bound is in rect or lower bound is in rect or upper and lower are in rect
        /*boolean axisAligned1 = (rect.getUpperBound() > (rect.getCoord().getY() - rect.getSize()) && rect.getUpperBound() < (rect.getCoord().getY() + rect.getSize()))
                || (rect.getLowerBound() > (rect.getCoord().getY() - rect.getSize()) && rect.getLowerBound() < (rect.getCoord().getY() + rect.getSize()))
                || (rect.getUpperBound() > (rect.getCoord().getY() - rect.getSize()) && rect.getLowerBound() < (rect.getCoord().getY() + rect.getSize()));

        boolean axisAligned2 = (rect.getRightBound() > (rect.getCoord().getX() - rect.getSize()) && rect.getRightBound() < (rect.getCoord().getX() + rect.getSize()))
                || (rect.getLeftBound() > (rect.getCoord().getX() - rect.getSize()) && rect.getLeftBound() < (rect.getCoord().getX() + rect.getSize()))
                || (rect.getRightBound() > (rect.getCoord().getX() - rect.getSize()) && rect.getLeftBound() < (rect.getCoord().getX() + rect.getSize()));*/

        boolean axisAlignedWestEast = this.getLeftBound() > rect.getLeftBound() && this.getLeftBound() < rect.getRightBound() ||
                this.getRightBound() > rect.getLeftBound() && this.getRightBound() < rect.getRightBound() ||
                this.getLeftBound() <= rect.getLeftBound() && this.getRightBound() >= rect.getRightBound();

        boolean axisAlignedNorthSouth = this.getLowerBound() > rect.getLowerBound() && this.getLowerBound() < rect.getUpperBound() ||
                this.getUpperBound() > rect.getLowerBound() && this.getUpperBound() < rect.getUpperBound() ||
                this.getLowerBound() <= rect.getLowerBound() && this.getUpperBound() >= rect.getUpperBound();

        return axisAlignedWestEast && axisAlignedNorthSouth;
    }

    //creates minimum rect that encloses this and given rect
    public RectangularObject createEnclosingRect(RectangularObject rect) {
        int left = Math.min(this.getLeftBound(), rect.getLeftBound());
        int right = Math.max(this.getRightBound(), rect.getRightBound());
        int up = Math.max(this.getUpperBound(), rect.getUpperBound());
        int down = Math.min(this.getLowerBound(), rect.getLowerBound());

        System.out.println("creating enclosing triangle left: " + left + " right: " + right + " up: " + up + " down: " + down);

        return new RectangularObject(left, right, up, down, this.color);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void transformLeftBound(int newLeftBound) {
        coord.setX(newLeftBound + width);
    }

    public void transformRightBound(int newRightBound) {
        coord.setX(newRightBound - width);
    }

    public void transformUpperBound(int newUpperBound) {
        coord.setY(newUpperBound - width);
    }

    public void transformLowerBound(int newLowerBound) {
        coord.setY(newLowerBound + width);
    }

    public String toString() {
        return "Rectangle with left: " + getLeftBound() + " right: " + getRightBound() + " up: " + getUpperBound() + " low: " + getLowerBound();
    }
}
