package eastin.Survive.objects;

import eastin.Survive.Runner;
import eastin.Survive.World;
import eastin.Survive.managers.Barriers;
import eastin.Survive.managers.Enemies;
import eastin.Survive.utils.Color;
import eastin.Survive.utils.Direction;
import org.junit.Test;

import java.util.Arrays;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by ebricco on 8/13/18.
 */
public class EnemyTest {

    //test for jitter in enemy movement
    @Test
    public void jitterTest() throws Exception {

        World world = new World();

        boolean testCompleted = false;

        long begin;
        long toSleep;

        world.handleInput(GLFW_KEY_D, GLFW_PRESS);

        long startTest = System.currentTimeMillis();

        Integer previousDistance = null;

        while(!testCompleted) {
            begin = System.currentTimeMillis();

            world.mc.update();
            world.enemies.update(world.mc.getCenter());

            int enemyRight = world.enemies.objects.get(0).getRightBound();
            int mcRight = world.mc.getRightBound();

            int distance = mcRight - enemyRight;

            if(previousDistance == null) {
                previousDistance = distance;
            }

            assert(Math.abs(previousDistance - distance) < 15);

            previousDistance = distance;

            toSleep = 17 - (System.currentTimeMillis() - begin);
            sleep(toSleep < 0?0:toSleep);

            if(System.currentTimeMillis() - startTest > 3000) {
                testCompleted = true;
            }
        }

    }
}
