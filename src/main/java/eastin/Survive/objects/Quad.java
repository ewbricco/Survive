package eastin.Survive.objects;

import eastin.Survive.utils.Coordinate;

import java.io.Serializable;

/**
 * Created by ebricco on 8/9/18.
 *
 * rotatable quadrilateral
 */
public class Quad extends Entity implements Serializable {

    Line left;
    Line right;
    Line front;
    Line back;

    public Quad(Coordinate topLeft, Coordinate topRight, Coordinate bottomRight, Coordinate bottomLeft) {
        left = new Line(bottomLeft, topLeft);
        right = new Line(bottomRight, topRight);
        front = new Line(topLeft, topRight);
        back = new Line(bottomLeft, bottomRight);
    }

    //aligns line through bottom and top to cartesian angle while keeping rectangle as centered as possible
    //note an aligned rectangle has constant angle of 90 degrees
    public void alignToAngle(double angle) {
        angle %= 360;
        if(angle <0 ) {
            angle *= -1d;
        }
    }

    @Override
    public boolean overlapsWith(Entity e) {
        return false;
    }
}
