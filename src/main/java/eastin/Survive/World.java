package eastin.Survive;

import eastin.Survive.manager.*;
import eastin.Survive.manager.MainCharacter;
import eastin.Survive.worldprovider.WorldProvider;
import eastin.Survive.worldprovider.WorldProviderFactory;

import java.io.*;
import java.util.*;

import static java.lang.Thread.sleep;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by ebricco on 8/13/18.
 *
 * represents all ingame objects
 *
 * TODO resolve unification of concerns, world shouldn't be a world provider. Maybe there should be a WorldManager?
 */
public class World implements Serializable, WorldProvider {
    static final long serialVersionUID=1L;

    public MainCharacter mc;
    public Barriers barriers;
    public Enemies enemies;
    public Projectiles projectiles;

    private List<Manager> managers;

    public boolean paused = false;
    public boolean spawning = false;

    private static String fileName = null;

    public World() {
        WorldProviderFactory.getWorldProvider().setWorldProvider(this);

        mc = new MainCharacter();
        barriers = new Barriers();
        enemies = new Enemies();
        projectiles = new Projectiles();

        managers = Arrays.asList(barriers, enemies, mc, projectiles);
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
            spawning = !spawning;
        }

        else if(key == GLFW_KEY_1 && action == GLFW_PRESS) {
            enemies.getObjects().forEach(e -> {
                System.out.println("PATH:");
                e.path.forEach(c -> System.out.println(c));
                System.out.println("*****");
            });
        }

        else if(key == GLFW_KEY_2 && action == GLFW_PRESS) {
            System.out.println("mc at: " + mc.toString());
        }

        else if(key == GLFW_KEY_3 && action == GLFW_PRESS) {
            System.out.println("closest barrier: " + barriers.getClosest(mc.getCenter()));
        }

        else if(key == GLFW_KEY_4) {
            enemies.getObjects().forEach(e -> System.out.println("enemy: " + e.toString()));
        }

        if(!paused) {
            mc.checkInput(key, action);
        }
    }


    public void update() {
        managers.forEach(m -> m.update());
    }

    public void render() {
        managers.forEach(m -> m.render());
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

        WorldProviderFactory.getWorldProvider().setWorldProvider(world);

        return world;
    }

    public void simulateGameLoop(List<DoIf> doifs, EndIf endCondition) {
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

    public void simulateGameLoop() {
        simulateGameLoop(new ArrayList<>(), () -> false);
    }

    public static class RunEveryNth implements DoIf {
        private int n;
        private int i;
        private Runnable action;

        public RunEveryNth(int n, Runnable action) {
            this.n = n;
            this.action = action;
            i=1;
        }

        public void attempt() {
            if(i%n == 0) {
                action.run();
            }
            i++;
        }
    }

    public interface DoIf {
        void attempt();
    }

    public interface EndIf {
        boolean test();
    }
}
