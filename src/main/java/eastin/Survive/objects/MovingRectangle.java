package eastin.Survive.objects;

import eastin.Survive.World;
import eastin.Survive.utils.Color;
import eastin.Survive.utils.Coordinate;
import eastin.Survive.utils.Direction;

import java.util.List;

/**
 * Created by ebricco on 7/25/18.
 */
public class MovingRectangle extends RenderableRectangle {

    public MovingRectangle(MovingRectangle m) {
        super(m.getLeftBound(), m.getRightBound(), m.getUpperBound(), m.getLowerBound(), m.color);
    }

    public MovingRectangle(int leftBound, int rightBound, int upperBound, int lowerBound, Color color) {
        super(leftBound, rightBound, upperBound, lowerBound, color);
    }

    //moves rectangle in direction d by distance, unless it hits an obstacle. Returns impacted obstacle
    public Rectangle move(Direction direction, int distance, List<? extends Rectangle> obstacles) {

        //System.out.println("moving mc. current quad: " + toString());
        //System.out.println(direction);

        Rectangle sim = simulateUnhinderedMovement(direction, distance);

        //System.out.println("sim: " + sim.toString());

        Rectangle affectedArea = createEnclosingRect(sim);

        //System.out.println("affected area: "  + affectedArea.toString());

        Rectangle collisionObject = null;

        //note need to go through all objects even if find one that overlaps; could overlap with multiple and need "first" object collision
        for(Rectangle object:obstacles){
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

    public Rectangle move(Coordinate diff, List<? extends Rectangle> interactables) {

        if(World.world.printFloat) {
            System.out.println("movement vector " + diff.toString());
        }

        Rectangle collisionObject = null;
        Rectangle next;

        if(yMovementCollides(diff, interactables)) {
            if(World.world.printFloat) {
                System.out.println("y collides");
            }
            next = moveX(diff, interactables);
            if(next != null) {
                collisionObject = next;
            }
            next = moveY(diff, interactables);
            if(collisionObject == null && next != null) {
                collisionObject = next;
            }
        } else {
            if(World.world.printFloat) {
                System.out.println("y first");
                World.world.printFloat = false;
            }
            next = moveY(diff, interactables);
            if(next != null) {
                collisionObject = next;
            }
            next = moveX(diff, interactables);
            if(collisionObject == null && next != null) {
                collisionObject = next;
            }
        }

        return collisionObject;
    }

    protected Rectangle moveY(Coordinate diff, List<? extends Rectangle> interactables) {

        Rectangle collisionObject = null;

        if(diff.getY() > 0) {
            collisionObject = move(Direction.NORTH, diff.getY(), interactables);
        } else if(diff.getY() < 0) {
            collisionObject = move(Direction.SOUTH, -diff.getY(), interactables);
        }

        return collisionObject;
    }

    protected Rectangle moveX(Coordinate diff, List<? extends Rectangle> interactables) {

        Rectangle collisionObject = null;

        if(diff.getX() > 0) {
            collisionObject = move(Direction.EAST, diff.getX(), interactables);
        } else if(diff.getX() < 0) {
            collisionObject = move(Direction.WEST, -diff.getX(), interactables);
        }

        return collisionObject;
    }

    protected boolean yMovementCollides(Coordinate diff, List<? extends Rectangle> interactables) {
        Rectangle next = null;
        MovingRectangle simulation = new MovingRectangle(this);

        if(diff.getY() > 0) {
            next = simulation.move(Direction.NORTH, diff.getY(), interactables);
            if(next != null) {
                return true;
            }
        } else if(diff.getY() < 0) {
            next = simulation.move(Direction.SOUTH, -diff.getY(), interactables);
            if(next != null) {
                return true;
            }
        }

        return false;
    }

    public Rectangle simulateUnhinderedMovement(Direction direction, int distance) {

        if(direction == Direction.NORTH){
            return new Rectangle(leftBound, rightBound, upperBound + distance, lowerBound + distance);
        }
        else if(direction == Direction.SOUTH){
            return new Rectangle(leftBound, rightBound, upperBound - distance, lowerBound - distance);
        }
        else if(direction == Direction.WEST){
            return new Rectangle(leftBound - distance, rightBound - distance, upperBound, lowerBound);
        }
        else if(direction == Direction.EAST){
            return new Rectangle(leftBound + distance, rightBound + distance, upperBound, lowerBound);
        }

        return null;
    }

    public Rectangle seek(Coordinate target, double distance, List<? extends Rectangle> interactables) {
        //System.out.println("target: " + target.toString());
        //System.out.println("center: " + getCenter());
        Coordinate diff = Coordinate.difference(target, getBottomLeft());
        diff.pythagoreanScale(distance);
        //System.out.println(diff.toString());
        return move(diff, interactables);
    }

}
