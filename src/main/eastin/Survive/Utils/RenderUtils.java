package eastin.Survive.Utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 * Created by ebricco on 9/30/17.
 */
public class RenderUtils {

    //object will be of width width*2 + 1, same for height
    public static void renderQuad(Coordinate coords, int width, int height, Color color) {
        color.setColor();
        glBegin(GL_QUADS);
        {
            glVertex2f(coords.getX()-width,coords.getY()-height);
            glVertex2f(coords.getX()+width,coords.getY()-height);
            glVertex2f(coords.getX()+width,coords.getY()+height);
            glVertex2f(coords.getX()-width,coords.getY()+height);
        }
        glEnd();
    }
}
