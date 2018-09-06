package eastin.Survive.objects;

import eastin.Survive.World;
import eastin.Survive.utils.Color;
import eastin.Survive.utils.Coordinate;

/**
 * Created by ebricco on 8/23/18.
 */
public class Item extends RenderableRectangle {
    public static final int SIZE = 30;
    public static final Color color = new Color(0,0,1);
    public boolean toDespawn;


    public Item(int leftBound, int rightBound, int upperBound, int lowerBound) {
        super(leftBound, rightBound, upperBound, lowerBound, color);
        toDespawn = false;
    }

    public Item(Coordinate coord) {
        super(coord, SIZE, SIZE, color);
        this.toDespawn = false;
    }

    public void despawn() {
        toDespawn = true;
    }
}
