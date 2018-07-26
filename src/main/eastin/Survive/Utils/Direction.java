package eastin.Survive.Utils;

/**
 * Created by ebricco on 11/29/16.
 */
public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public Direction getOpposite() {
        if(this.name().equals("NORTH")) {
            return SOUTH;
        }
        else if(this.name().equals("SOUTH")) {
            return NORTH;
        }
        else if(this.name().equals("WEST")) {
            return EAST;
        }
        else if(this.name().equals("EAST")) {
            return WEST;
        }

        return null;
    }
}
