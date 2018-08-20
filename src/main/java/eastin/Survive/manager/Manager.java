package eastin.Survive.manager;

/**
 * Created by ebricco on 8/17/18.
 */
public interface Manager {
    void pause(long time);
    void unpause(long time);
    void render();
    void update();
}
