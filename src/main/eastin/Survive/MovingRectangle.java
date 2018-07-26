package eastin.Survive;

import eastin.Survive.Utils.Color;
import eastin.Survive.Utils.Coordinate;
import eastin.Survive.Utils.Direction;
import eastin.Survive.Utils.GameCoordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ebricco on 7/25/18.
 */
public class MovingRectangle extends RectangularObject {
    public MovingRectangle(GameCoordinate coord, int width, int height, Color color) {
        super(coord, width, height, color);
    }

    //moves rectangle in direction d by distance, unless it hits an obstacle. Returns impacted obstacle
    public RectangularObject move(Direction direction, int distance, List<RectangularObject> obstacles) {

        System.out.println(direction);

        RectangularObject sim = simulateUnhinderedMovement(direction, distance);

        System.out.println("sim: " + sim.toString());

        RectangularObject affectedArea = createEnclosingRect(sim);

        System.out.println("affected area: "  + affectedArea.toString());

        RectangularObject collisionObject = null;

        //note need to go through all objects even if find one that overlaps; could overlap with multiple and need "first" object collision
        for(RectangularObject object:obstacles){
            if(affectedArea.overlapsWith(object)){

                System.out.println("collision detected: " + object.toString());

                collisionObject = object;

                if(direction == Direction.NORTH) {
                    sim.transformUpperBound(collisionObject.getLowerBound());
                }
                else if(direction == Direction.SOUTH){
                    sim.transformLowerBound(collisionObject.getUpperBound());
                }
                else if(direction == Direction.WEST){
                    sim.transformLeftBound(collisionObject.getRightBound());
                }
                else if(direction == Direction.EAST){
                    sim.transformRightBound(collisionObject.getLeftBound());
                }
            }
        }

        coord = sim.getCoord();

        return collisionObject;
    }

    public void unconditionalTeleport(GameCoordinate coordinate) {
        coord = coordinate;
    }

    public RectangularObject simulateUnhinderedMovement(Direction direction, int distance) {
        GameCoordinate simCoord = coord.clone();
        if(direction == Direction.NORTH){
            simCoord.addY(distance);
        }
        else if(direction == Direction.SOUTH){
            simCoord.addY(-1*distance);
        }
        else if(direction == Direction.WEST){
            simCoord.addX(-1*distance);
        }
        else if(direction == Direction.EAST){
            simCoord.addX(distance);
        }

        return new RectangularObject(simCoord, width, height, color);
    }

}
