package eastin.Survive.manager;

import eastin.Survive.World;
import eastin.Survive.objects.Projectile;
import eastin.Survive.utils.Coordinate;
import eastin.Survive.utils.Direction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ebricco on 8/17/18.
 */
public class Projectiles implements Manager, Serializable {
    public List<Projectile> objects;

    private final long TIMEBETWEENTRIMS = 6000;
    private long lastTrim;
    private long pausedAt;

    public Projectiles() {
        objects = new ArrayList<>();
        lastTrim = 0;
    }

    public void update() {
        objects.forEach(p -> p.update());
        if(System.currentTimeMillis() - lastTrim > TIMEBETWEENTRIMS) {
            trim();
        }
    }

    public void render() {
        objects.removeIf(p -> p.toDespawn);
        objects.forEach(p -> p.checkPositionAndRender());
    }

    public void trim() {
        objects.forEach(p -> {
            if(!p.overlapsWith(World.world.mc.getScreen())) {
                p.toDespawn = true;
            }
        });

        lastTrim = System.currentTimeMillis();
    }

    public void addNewProjectile(Direction d, Coordinate launchPoint) {
        objects.add(Projectile.newProjectileFromLaunchPoint(d, launchPoint));
    }

    public void pause(long time) {
        pausedAt = time;
    }

    public void unpause(long time) {
        lastTrim = time - (pausedAt - lastTrim);
        objects.forEach(p -> p.setLastMovement(time - (pausedAt - p.getLastMovement())));
    }
}
