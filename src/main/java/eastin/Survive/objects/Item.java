package eastin.Survive.objects;

import eastin.Survive.utils.Color;

/**
 * Created by ebricco on 8/23/18.
 */
public class Item extends RenderableRectangle {
    public static final int SIZE = 30;
    public static final Color color = new Color(0,0,1);


    public Item(int leftBound, int rightBound, int upperBound, int lowerBound, Color color) {
        super(leftBound, rightBound, upperBound, lowerBound, color);
    }
}
