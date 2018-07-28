package eastin.Survive.objects;

import eastin.Survive.utils.Color;
import eastin.Survive.utils.Coordinate;

import java.util.List;

/**
 * Created by ebricco on 7/27/18.
 */
public class Enemy extends MovingRectangle {

    private final int MOVEMENTDISTANCE = 10;

    public boolean toDespawn;

    public Enemy(int leftBound, int rightBound, int upperBound, int lowerBound, Color color) {
        super(leftBound, rightBound, upperBound, lowerBound, color);
    }

    public void update(Coordinate target, List<RectangularObject> interactables) {
        handleCollision(seek(target, MOVEMENTDISTANCE, interactables));
    }

    private void handleCollision(RectangularObject object) {
        if(object instanceof MainCharacter) {
            ((MainCharacter)object).takeDamage(1);
            toDespawn = true;
        }
    }
}
