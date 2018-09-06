package eastin.Survive.objects;

import eastin.Survive.World;
import eastin.Survive.utils.Color;
import eastin.Survive.utils.Coordinate;
import eastin.Survive.utils.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ebricco on 8/17/18.
 */
public class Projectile extends MovingRectangle {

    private final static int LENGTH = 15;
    private final static int WIDTH = 5;
    private final static Color COLOR = new Color(0,0,1);
    private final static int SPEED = 7000;

    private Direction direction;
    private long lastMovement;
    public boolean toDespawn;

    private Projectile(Direction d, Rectangle r) {
        super(r.getLeftBound(), r.getRightBound(), r.getUpperBound(), r.getLowerBound(), COLOR);

        this.direction = d;
        lastMovement = -1;
        toDespawn = false;
    }

    public void update() {
        if(lastMovement == -1) {
            lastMovement = System.currentTimeMillis();
            return;
        }

        int movementDistance = (int)((System.currentTimeMillis() - lastMovement)*SPEED)/1000;
        lastMovement = System.currentTimeMillis();

        List<Rectangle> interactables = new ArrayList<>();

        World.world.barriers.getObjects().forEach(b -> interactables.add(b));
        World.world.enemies.getObjects().forEach(e -> interactables.add(e));
        World.world.items.getObjects().forEach(i -> interactables.add(i));


        handleCollision(move(direction, movementDistance, interactables));

    }

    public static Projectile newProjectileFromLaunchPoint(Direction d, Coordinate launchPoint) {
        return new Projectile(d, Rectangle.buildRectangleFromPointInDirection(LENGTH, WIDTH, d, launchPoint));
    }

    private void handleCollision(Rectangle object) {
        if(object instanceof Enemy) {
            ((Enemy)object).takeDamage(1);
            toDespawn = true;
        } else if(object instanceof Barrier) {
            toDespawn = true;
        } else if(object instanceof Item) {
            toDespawn = true;
        }
    }

    public void setLastMovement(long lastMovement) {
        this.lastMovement = lastMovement;
    }

    public long getLastMovement() {
        return lastMovement;
    }
}
