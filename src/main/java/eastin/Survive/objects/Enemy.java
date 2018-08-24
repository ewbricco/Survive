package eastin.Survive.objects;

import eastin.Survive.manager.MainCharacter;
import eastin.Survive.utils.Color;
import eastin.Survive.utils.Coordinate;
import eastin.Survive.utils.NavMesh;
import eastin.Survive.utils.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by ebricco on 7/27/18.
 */
public class Enemy extends MovingRectangle {

    public static final Color DEADCOLOR = new Color(1,0,0);

    private final int SPEED = 350;
    private final int INITIALHEALTH = 1;

    private long lastMovement;
    public List<Coordinate> path;
    public int positionInPath; //enemy is at or past this position in path
    private long lastPathUpdate;
    public static long TIMEBETWEENPATHUPDATE = 10;
    private int health;
    private long diedAt;
    public static long DEADTIME = 75;

    public boolean toDespawn; //if enemy should be removed from map
    public boolean dead; //if dead, but not necessarily ready to be removed

    public Enemy(int leftBound, int rightBound, int upperBound, int lowerBound, Color color) {
        super(leftBound, rightBound, upperBound, lowerBound, color);
        lastMovement = System.nanoTime() / 1000000;
        lastPathUpdate=0;
        health = INITIALHEALTH;
        dead = false;
    }

    public void update(RectangularObject target, List<? extends RectangularObject> interactables, NavMesh nav) {
        if(dead) {
            deadUpdate();
            return;
        }
        interactables = new ArrayList<RectangularObject>(interactables);
        interactables.remove(this);

        int movementDistance = (int)(SPEED * ((double)(System.nanoTime()/1000000 - lastMovement) / 1000d));
        lastMovement = System.nanoTime() / 1000000;

        if(System.currentTimeMillis() - lastPathUpdate > TIMEBETWEENPATHUPDATE) {
            path = nav.getPath(this, target);
            positionInPath = 0;
            lastPathUpdate = System.currentTimeMillis();
        }

        for(int i=positionInPath; i<path.size()-1; i++) {
            double stepDistance = getBottomLeft().distanceTo(path.get(i+1));
            if(stepDistance <= movementDistance) {
                handleCollision(seek(path.get(i+1), stepDistance, interactables));
                movementDistance -= stepDistance;
                positionInPath++;

                Coordinate diff = Coordinate.difference(getBottomLeft(), path.get(i+1));
                if(diff.getX() != 0 || diff.getY() != 0) {
                    System.out.println("enemy not moved to step when it was supposed to");
                    move(diff, interactables);
                }

            //make sure enemy is at position
            } else {
                handleCollision(seek(path.get(i+1), movementDistance, interactables));
                break;
            }
        }
    }

    private void deadUpdate() {
        if(System.currentTimeMillis() - diedAt > DEADTIME) {
            toDespawn = true;
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
            kill();
        }
    }

    public void kill() {
        dead = true;
        diedAt = System.currentTimeMillis();
        color = DEADCOLOR;
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
