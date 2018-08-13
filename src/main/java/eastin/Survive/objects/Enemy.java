package eastin.Survive.objects;

import eastin.Survive.utils.Color;
import eastin.Survive.utils.Coordinate;

import java.util.List;

/**
 * Created by ebricco on 7/27/18.
 */
public class Enemy extends MovingRectangle {

    private final int SPEED = 400;

    private long lastMovement;

    public boolean toDespawn;

    public Enemy(int leftBound, int rightBound, int upperBound, int lowerBound, Color color) {
        super(leftBound, rightBound, upperBound, lowerBound, color);
        lastMovement = System.nanoTime() / 1000000;
    }

    public void update(Coordinate target, List<RectangularObject> interactables) {
        int movementDistance = (int)(SPEED * ((double)(System.nanoTime()/1000000 - lastMovement) / 1000d));
        lastMovement = System.nanoTime() / 1000000;

        handleCollision(seek(target, movementDistance, interactables));
    }

    private void handleCollision(RectangularObject object) {
        if(object instanceof MainCharacter) {
            ((MainCharacter)object).takeDamage(1);
            toDespawn = true;
        }
    }
}
