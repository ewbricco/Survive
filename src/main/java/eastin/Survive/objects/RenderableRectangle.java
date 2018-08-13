package eastin.Survive.objects;

import eastin.Survive.Runner;
import eastin.Survive.objects.RectangularObject;
import eastin.Survive.utils.Color;
import eastin.Survive.utils.Coordinate;
import eastin.Survive.utils.GameCoordinate;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by ebricco on 7/26/18.
 */
public class RenderableRectangle extends RectangularObject {
    protected Color color;

    public RenderableRectangle(int leftBound, int rightBound, int upperBound, int lowerBound, Color color) {
        super(leftBound, rightBound, upperBound, lowerBound);
        this.color = color;
    }

    public RenderableRectangle(GameCoordinate coord, int width, int height, Color color) {
        super(coord, width, height);
        this.color = color;
    }

    //if any portion of the object is in the screen, render and return true
    public boolean checkPositionAndRender() {
        RectangularObject screen = Runner.world.mc.getScreen();

        if(!this.overlapsWith(screen)) {
            return false;
        }

        render(screen.getBottomLeft());

        return true;
    }

    public void render(Coordinate coord) {
        color.setColor();
        glBegin(GL_QUADS);
        {
            glVertex2f(getLeftBound()-coord.getX(),getLowerBound()-coord.getY());
            glVertex2f(getRightBound()-coord.getX(),getLowerBound()-coord.getY());
            glVertex2f(getRightBound()-coord.getX(),getUpperBound()-coord.getY());
            glVertex2f(getLeftBound()-coord.getX(),getUpperBound()-coord.getY());
        }
        glEnd();
    }
}
