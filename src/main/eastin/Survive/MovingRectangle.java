package eastin.Survive;

import eastin.Survive.Utils.Color;
import eastin.Survive.Utils.Coordinate;
import eastin.Survive.Utils.Direction;

import java.util.List;

/**
 * Created by ebricco on 7/25/18.
 */
public class MovingRectangle extends RenderableRectangle {

    public MovingRectangle(int leftBound, int rightBound, int upperBound, int lowerBound, Color color) {
        super(leftBound, rightBound, upperBound, lowerBound, color);
    }

    //moves rectangle in direction d by distance, unless it hits an obstacle. Returns impacted obstacle
    public RectangularObject move(Direction direction, int distance, List<? extends RectangularObject> obstacles) {

        //System.out.println(direction);

        RectangularObject sim = simulateUnhinderedMovement(direction, distance);

        //System.out.println("sim: " + sim.toString());

        RectangularObject affectedArea = createEnclosingRect(sim);

        //System.out.println("affected area: "  + affectedArea.toString());

        RectangularObject collisionObject = null;

        //note need to go through all objects even if find one that overlaps; could overlap with multiple and need "first" object collision
        for(RectangularObject object:obstacles){
            if(affectedArea.overlapsWith(object)){

                //System.out.println("collision detected: " + object.toString());

                collisionObject = object;

                if(direction == Direction.NORTH) {
                    sim.transformUpperBound(collisionObject.getLowerBound()-1);
                }
                else if(direction == Direction.SOUTH){
                    sim.transformLowerBound(collisionObject.getUpperBound()+1);
                }
                else if(direction == Direction.WEST){
                    sim.transformLeftBound(collisionObject.getRightBound()+1);
                }
                else if(direction == Direction.EAST){
                    sim.transformRightBound(collisionObject.getLeftBound()-1);
                }

                affectedArea = createEnclosingRect(sim);
            }
        }

        //set new bounds
        setBounds(sim);

        return collisionObject;
    }

    public void move(Coordinate diff) {
        leftBound += diff.getX();
        rightBound += diff.getX();
        upperBound += diff.getY();
        lowerBound += diff.getY();
    }

    public RectangularObject simulateUnhinderedMovement(Direction direction, int distance) {

        if(direction == Direction.NORTH){
            return new RectangularObject(leftBound, rightBound, upperBound + distance, lowerBound + distance);
        }
        else if(direction == Direction.SOUTH){
            return new RectangularObject(leftBound, rightBound, upperBound - distance, lowerBound - distance);
        }
        else if(direction == Direction.WEST){
            return new RectangularObject(leftBound - distance, rightBound - distance, upperBound, lowerBound);
        }
        else if(direction == Direction.EAST){
            return new RectangularObject(leftBound + distance, rightBound + distance, upperBound, lowerBound);
        }

        return null;
    }

    public void seek(Coordinate target, int distance) {
        //System.out.println("target: " + target.toString());
        //System.out.println("center: " + getCenter());
        Coordinate diff = Coordinate.Difference(target, getCenter());
        diff.pythagoreanScale(distance);
        //System.out.println(diff.toString());
        move(diff);
    }

}
