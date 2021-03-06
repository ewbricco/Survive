package eastin.Survive;

import eastin.Survive.manager.*;
import eastin.Survive.manager.MainCharacter;
import eastin.Survive.objects.Enemy;
import eastin.Survive.sound.SoundManager;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.stb_easy_font_print;

/**
 * Created by ebricco on 8/13/18.
 *
 * represents all ingame objects
 *
 * TODO resolve unification of concerns, world shouldn't be a world provider. Maybe there should be a WorldManager?
 */
public class World implements Serializable {
    static final long serialVersionUID=1L;

    public static World world;

    public MainCharacter mc;
    public Barriers barriers;
    public Enemies enemies;
    public Projectiles projectiles;
    public static SoundManager sounds;
    public Items items;

    private List<Manager> managers;

    public boolean paused = false;
    public final boolean sound = true;
    public static boolean spawningEnemies = false;
    public static boolean spawningBarriers = true;
    public static boolean renderNav = false;
    public static boolean printFloat = false;

    private static String fileName = null;

    public World() {
        world = this;

        mc = new MainCharacter();
        barriers = new Barriers();
        enemies = new Enemies();
        projectiles = new Projectiles();
        items = new Items();

        managers = Arrays.asList(mc, barriers, enemies, projectiles, items);

        sounds = new SoundManager();
    }

    public static World createWorld() {
        if(fileName != null) {
            return World.recreateFromFile(fileName);
        } else {
            return new World();
        }
    }

    public World getWorld() {
        return this;
    }

    public void handleInput(int key, int action) {
        if(key == GLFW_KEY_P && action == GLFW_RELEASE) {
            if(paused) {
                unpause();
            } else {
                pause();
            }
        } else if(key == GLFW_KEY_F && action == GLFW_PRESS) {
            saveToFile();
        } else if(key == GLFW_KEY_E && action == GLFW_PRESS) {
            spawningEnemies = !spawningEnemies;
        } else if(key == GLFW_KEY_N && action == GLFW_RELEASE) {
            renderNav = !renderNav;
        }

        else if(key == GLFW_KEY_1 && action == GLFW_PRESS) {
            enemies.getObjects().forEach(e -> {
                System.out.println("PATH:");
                e.path.forEach(c -> System.out.println(c));
                System.out.println("*****");
            });
        } else if(key == GLFW_KEY_2 && action == GLFW_PRESS) {
            System.out.println("mc at: " + mc.toString());
        } else if(key == GLFW_KEY_3 && action == GLFW_PRESS) {
            System.out.println("closest barrier: " + barriers.getClosest(mc.getCenter()));
        } else if(key == GLFW_KEY_4 && action == GLFW_RELEASE) {
            enemies.getObjects().forEach(e -> System.out.println("enemy: " + e.toString()));
        } else if(key == GLFW_KEY_Z && action == GLFW_RELEASE) {
            printFloat = !printFloat;
        }

        if(!paused) {
            mc.checkInput(key, action);
        }
    }


    public void update() {
        managers.forEach(m -> m.update());
        sounds.update();
    }

    /*
    Note: this thing uses coordinates with flipped y
 */
    public void renderText() {
        String text = "Health: " + mc.health + "      Score: " +  Enemy.killCount;
        ByteBuffer charBuffer = BufferUtils.createByteBuffer(text.length() * 270);

        int quads = stb_easy_font_print(10, -GameState.HEIGHT / 3.5f, text, null, charBuffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glColor3f(0f, 0f, 0f);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);

        float scaleFactor = 1.0f + 10f * 0.25f;

        glPushMatrix();
        // Zoom
        glScalef(scaleFactor, -scaleFactor, 1f);
        // Scroll
        glTranslatef(4.0f, 4.0f, 0f);

        glDrawArrays(GL_QUADS, 0, quads * 4);

        glPopMatrix();

        glDisableClientState(GL_VERTEX_ARRAY);
    }

    public void render() {
        managers.forEach(m -> m.render());
        renderText();
    }

    public void pause() {
        paused = true;

        long pausedAt = System.currentTimeMillis();

        managers.forEach(m -> m.pause(pausedAt));
    }

    public void unpause() {
        paused = false;

        long unpausedAt = System.currentTimeMillis() + 100;

        managers.forEach(m -> m.unpause(unpausedAt));
        while(System.currentTimeMillis() < unpausedAt) {
            //spin
        }
    }


    protected String saveToFile() {

        Date date = new Date();

        Calendar cal = Calendar.getInstance();

        String month = Integer.toString((cal.get(Calendar.MONTH) + 1)).length()==1?("0" + Integer.toString((cal.get(Calendar.MONTH) + 1))):Integer.toString((cal.get(Calendar.MONTH) + 1));
        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH)).length()==1? "0" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)):Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY)).length()==1? "0" + Integer.toString(cal.get(Calendar.HOUR_OF_DAY)):Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String minute = Integer.toString(cal.get(Calendar.MINUTE)).length()==1? "0" + Integer.toString(cal.get(Calendar.MINUTE)):Integer.toString(cal.get(Calendar.MINUTE));

        String fileName = Integer.toString(cal.get(Calendar.YEAR)) + month + day + hour + Integer.toString(cal.get(Calendar.MINUTE)) + ".world";

        try
        {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(this);

            out.close();
            file.close();

        }

        catch(IOException ex)
        {
            System.out.println("problem saving world: " + ex.toString());
        }

        return fileName;
    }

    protected static World recreateFromFile(String filename) {

        World world = null;

        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            world = (World)in.readObject();

            in.close();
            file.close();
        } catch(IOException ex) {
            System.out.println(ex.toString());
        } catch(ClassNotFoundException ex) {
            System.out.println(ex.toString());
        }

        World.world = world;

        return world;
    }


    //for testing
    public void simulateGameLoop(List<Runner.DoIf> doifs, Runner.EndIf endCondition) {
        long start;
        while(!endCondition.test()) {
            start = System.currentTimeMillis();
            update();
            long toSleep = 17 - (System.currentTimeMillis() - start);
            toSleep = Math.max(0, toSleep);
            doifs.forEach(doIf -> doIf.attempt());
            try {
                sleep(toSleep);
            } catch(InterruptedException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void destroy() {
        sounds.destroy();
    }

    public void simulateGameLoop() {
        simulateGameLoop(new ArrayList<>(), () -> false);
    }
}
