package eastin.Survive;

import java.util.Random;

/**
 * Created by ebricco on 11/27/16.
 */
public class GameState {
    public static final int WIDTH = 2560;
    public static final int HEIGHT = 1600;
    public static final int FRAMES = 60;
    public static final int MILLISPERFRAME = Math.round(1000 / FRAMES);
    public static final Random RAND = new Random();
    public static final String NAME = "survive";
}
