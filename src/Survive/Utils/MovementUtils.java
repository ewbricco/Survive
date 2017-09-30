package Survive.Utils;

import Survive.Barriers;

import java.util.ArrayList;

/**
 * Created by ebricco on 9/30/17.
 */
public class MovementUtils {

    public static Coordinate move(Direction direction, Coordinate mc, int mcSize, Barriers barriers, int MOVEMENTDISTANCE) {

        Coordinate coordDelta;

        ArrayList<Integer> posX = new ArrayList();
        ArrayList<Integer> posY = new ArrayList();

        barriers.coords.forEach(coordinate -> {
            posX.add(coordinate.getX());
            posY.add(coordinate.getY());
        });

        if(direction == Direction.NORTH){
            int adj = collisionAdjustment(Direction.NORTH, mc.getX(), mc.getY(), mcSize, posX, posY, barriers.size, MOVEMENTDISTANCE);
            coordDelta = new Coordinate(0, MOVEMENTDISTANCE - adj);
            barriers.uncalculatedNorth += MOVEMENTDISTANCE;
        }
        else if(direction == Direction.SOUTH){
            int adj = collisionAdjustment(Direction.SOUTH, mc.getX(), mc.getY(), mcSize, posX, posY, barriers.size, MOVEMENTDISTANCE);
            coordDelta = new Coordinate(0, adj - MOVEMENTDISTANCE);
            barriers.uncalculatedSouth += MOVEMENTDISTANCE;
        }
        else if(direction == Direction.WEST){
            int adj = collisionAdjustment(Direction.WEST, mc.getX(), mc.getY(), mcSize, posX, posY, barriers.size, MOVEMENTDISTANCE);
            coordDelta = new Coordinate(adj - MOVEMENTDISTANCE, 0);
            barriers.uncalculatedWest += MOVEMENTDISTANCE;
        }
        else if(direction == Direction.EAST){
            int adj = collisionAdjustment(Direction.EAST, mc.getX(), mc.getY(), mcSize, posX, posY, barriers.size, MOVEMENTDISTANCE);
            coordDelta = new Coordinate(MOVEMENTDISTANCE - adj, 0);
            barriers.uncalculatedEast += MOVEMENTDISTANCE;
        } else {
            coordDelta = new Coordinate(0,0);
        }

        return coordDelta;
    }

    /*check if a barrier overlaps with given quad, if it does calculate adjustment
    * note: assumes that object can only collide with one shape
    */
    public static int collisionAdjustment(Direction d, int x, int y, int s, ArrayList<Integer> posX, ArrayList<Integer> posY, ArrayList<Integer> size, int MOVEMENTDISTANCE){
        //boolean diffInRange;
        //boolean otherAxisAligned;
        boolean axisAligned1;
        boolean axisAligned2;

        int diff = 0, top, bottom, left, right;

        if(d == Direction.NORTH){
            top = y + s + MOVEMENTDISTANCE;
            bottom = y - s + MOVEMENTDISTANCE;
            left = x - s;
            right = x + s;
        }
        else if(d == Direction.SOUTH){
            top = y + s - MOVEMENTDISTANCE;
            bottom = y - s - MOVEMENTDISTANCE;
            left = x - s;
            right = x + s;
        }
        else if(d == Direction.WEST){
            top = y + s;
            bottom = y - s;
            left = x - s - MOVEMENTDISTANCE;
            right = x + s - MOVEMENTDISTANCE;
        }
        else if(d == Direction.EAST){
            top = y + s;
            bottom = y - s;
            left = x - s + MOVEMENTDISTANCE;
            right = x + s + MOVEMENTDISTANCE;
        }
        else{
            return 0;
        }

        for(int i=0; i<posX.size(); i++){
            axisAligned1 = (top > (posY.get(i) - size.get(i)) && top < (posY.get(i) + size.get(i)))
                    || (bottom > (posY.get(i) - size.get(i)) && bottom < (posY.get(i) + size.get(i)))
                    || (top > (posY.get(i) - size.get(i)) && bottom < (posY.get(i) + size.get(i)));

            axisAligned2 = (right > (posX.get(i) - size.get(i)) && right < (posX.get(i) + size.get(i)))
                    || (left > (posX.get(i) - size.get(i)) && left < (posX.get(i) + size.get(i)))
                    || (right > (posX.get(i) - size.get(i)) && left < (posX.get(i) + size.get(i)));
            if(axisAligned1 && axisAligned2){
                if(d == Direction.NORTH) {
                    diff = top - (posY.get(i) - size.get(i));
                }
                else if(d == Direction.SOUTH){
                    diff =  (posY.get(i) + size.get(i)) - bottom;
                }
                else if(d == Direction.WEST){
                    diff =  (posX.get(i) + size.get(i)) - left;
                }
                else if(d == Direction.EAST){
                    diff =  right - (posX.get(i) - size.get(i));
                }
                return diff;
            }
        }

        return 0;
    }
}
