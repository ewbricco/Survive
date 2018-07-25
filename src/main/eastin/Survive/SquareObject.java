package main.eastin.Survive;

import main.eastin.Survive.Utils.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by ebricco on 7/25/18.
 */
public class SquareObject {
    private GameCoordinate coord;
    private int size; //square radius (also account for center pixel)
    private Color color;

    public SquareObject(GameCoordinate coord, int size, Color color) {
        this.coord = coord;
        this.size = size;
        this.color = color;
    }


    //if any portion of the object is in the screen, render
    public void checkPositionAndRender() {
        AreaQuad screen = Runner.mc.getAreaQuad();

        //if no portion is in given quad, don't render
        if(coord.getX()+size < screen.getWesternFrontier() || coord.getX()-size > screen.getEasternFrontier()){
            return;
        }
        if(coord.getY()+size < screen.getSouthernFrontier() || coord.getY()-size > screen.getNorthernFrontier()){
            return;
        }

        render(Coordinate.Difference(coord, screen.getBottomLeft()), size, size);
    }

    //object will be of width width*2 + 1, same for height
    public void render(Coordinate renderCoord, int width, int height) {
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
        return size;
    }
}
