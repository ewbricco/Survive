package eastin.Survive.objects;

import eastin.Survive.manager.MainCharacter;
import eastin.Survive.utils.Color;
import eastin.Survive.utils.Coordinate;
import eastin.Survive.utils.NavMesh;
import eastin.Survive.utils.Node;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ebricco on 7/27/18.
 */
public class Enemy extends MovingRectangle {

    private final int SPEED = 300;
    private final int INITIALHEALTH = 1;

    private long lastMovement;
    public List<Coordinate> path;
    private long lastPathUpdate;
    private final long TIMEBETWEENPATHUPDATE = 100;
    private int health;

    public boolean toDespawn;

    public Enemy(int leftBound, int rightBound, int upperBound, int lowerBound, Color color) {
        super(leftBound, rightBound, upperBound, lowerBound, color);
        lastMovement = System.nanoTime() / 1000000;
        lastPathUpdate=0;
        health = INITIALHEALTH;
    }

    public void update(Coordinate target, List<RectangularObject> interactables, NavMesh nav) {
        int movementDistance = (int)(SPEED * ((double)(System.nanoTime()/1000000 - lastMovement) / 1000d));
        lastMovement = System.nanoTime() / 1000000;

        if(System.currentTimeMillis() - lastPathUpdate > TIMEBETWEENPATHUPDATE) {
            path = nav.getPath(new Node(getBottomLeft()), new Node(target));
        }

        for(int i=0; i<path.size()-1; i++) {
            double stepDistance = path.get(i).distanceTo(path.get(i+1));
            if(stepDistance < movementDistance) {
                handleCollision(seek(path.get(i+1), (int)stepDistance, interactables));
                movementDistance -= stepDistance;
            } else {
                handleCollision(seek(path.get(i+1), movementDistance, interactables));
                break;
            }
        }
    }

    private void handleCollision(RectangularObject object) {
        if(object instanceof MainCharacter) {
            ((MainCharacter)object).takeDamage(1);
            toDespawn = true;
        } else if(object instanceof Projectile) {
            takeDamage(1);
            ((Projectile)object).toDespawn = true;
        }
    }

    public void takeDamage(int damage) {
        health -= damage;
        if(health <= 0) {
            toDespawn = true;
        }
    }

    public void setLastMovement(long time) {
        lastMovement = time;
    }

    public void setLastPathUpdate(long time) {
        lastPathUpdate = time;
    }

    public long getLastPathUpdate() {
        return lastPathUpdate;
    }

    public long getLastMovement() {
        return lastMovement;
    }
}
