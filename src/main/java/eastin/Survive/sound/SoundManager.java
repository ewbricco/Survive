package eastin.Survive.sound;

import eastin.Survive.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.lwjgl.openal.AL10.alGenSources;

/**
 * Created by ebricco on 8/23/18.
 */
public class SoundManager {
    private ExecutorService executors;
    List<Sound> sounds;
    int trimInterval = 1000;
    int cycleCount;
    HashMap<String, Sound> soundMap;
    public static List<Integer> sources;
    int fireCounter;
    final int NUMBERFIRESOURCES = 100;


    public SoundManager() {
        executors = Executors.newFixedThreadPool(1);
        sounds = new ArrayList<>();
        cycleCount = 0;
        soundMap = new HashMap<>();

        sources = new ArrayList<Integer>();

        Sound.initializeAbilityToPlaySound();

        for(int i=0; i<NUMBERFIRESOURCES; i++) {
            //Request a source
            sources.add(alGenSources());
        }

        fireCounter = 0;
    }

    public int getNextFireSource() {
        return sources.get(fireCounter++%100);
    }

    public void playSound(String fileName) {
        if(World.world.sound) {
            Sound toPlay;
            if(soundMap.get(fileName) == null) {
                System.out.println("new sound: " + soundMap.get(fileName));
                toPlay = new Sound(fileName);
                soundMap.put(fileName, toPlay);
            } else {
                toPlay = soundMap.get(fileName);
            }

            sounds.add(toPlay);
            executors.submit(toPlay);
        }
    }

    public void update() {
        //sounds.forEach(s -> s.checkIfDone());
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
