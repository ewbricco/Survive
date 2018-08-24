package eastin.Survive.sound;

import eastin.Survive.World;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ebricco on 8/23/18.
 */
public class SoundManager {
    private ExecutorService executors;
    List<Sound> sounds;
    int trimInterval = 1000;
    int cycleCount;

    public SoundManager() {
        executors = Executors.newFixedThreadPool(1);
        sounds = new ArrayList<>();
        cycleCount = 0;
    }

    public void playSound(String fileName) {
        if(World.world.sound) {
            Sound toPlay = new Sound(fileName);
            sounds.add(toPlay);
            executors.submit(new Sound(fileName));
        }
    }

    public void update() {
        sounds.forEach(s -> s.checkIfDone());
        if(cycleCount % trimInterval == 0) {
            trim();
        }
    }

    public void trim() {
        sounds.removeIf(s -> s.toRemove);
    }

    public void destroy() {
        sounds.forEach(s -> s.delete());
        executors.shutdownNow();
    }
}
