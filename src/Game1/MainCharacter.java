package Game1;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 * Created by ebricco on 11/27/16.
 */
public class MainCharacter {
    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getSize() {
        return WIDTH;
    }

    private int xPos;
    private int yPos;
    private final int WIDTH = 41;
    private final int HEIGHT = 41;
    private final int STARTPOINTX = GameState.WIDTH/2;
    private final int STARTPOINTY = GameState.HEIGHT/2;

    public MainCharacter(){
        xPos = STARTPOINTX;
        yPos = STARTPOINTY;
    }

    public void render(){
        glColor3f(0, 0, 0);
        glBegin(GL_QUADS);
        {
            glVertex2f(xPos-WIDTH,yPos-HEIGHT);
            glVertex2f(xPos+WIDTH,yPos-HEIGHT);
            glVertex2f(xPos+WIDTH,yPos+HEIGHT);
            glVertex2f(xPos-WIDTH,yPos+HEIGHT);
        }
        glEnd();
    }


}
