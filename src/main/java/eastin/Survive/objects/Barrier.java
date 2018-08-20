package eastin.Survive.objects;

import eastin.Survive.utils.Color;
import eastin.Survive.utils.Coordinate;

import java.io.Serializable;

/**
 * Created by ebricco on 8/17/18.
 */
public class Barrier extends RenderableRectangle {

    public Barrier(Coordinate coord, int width, int height, Color color) {
        super(coord, width, height, color);
    }

    public Barrier(int left, int right, int up, int down, Color color) {
        super(left, right, up, down, color);
    }
}
