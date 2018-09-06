package eastin.Survive;

import eastin.Survive.objects.Rectangle;
import org.junit.Test;

import java.io.File;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

/**
 * Created by ebricco on 8/13/18.
 */
public class WorldTest {

    @Test
    public void testSave() {

        World world = new World();

        world.mc.checkInput(GLFW_KEY_D, GLFW_PRESS);

        Rectangle start = world.mc.getScreen();
        world.update();
        world.update();
        world.update();

        assert(!world.mc.getScreen().equals(start));

        String saveFile = world.saveToFile();

        World world2 = World.recreateFromFile(saveFile);

        assert(world.mc.getScreen().equals(world2.mc.getScreen()));

        File file = new File(saveFile);

        file.delete();
    }
}
