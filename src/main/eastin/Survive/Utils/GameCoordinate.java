package eastin.Survive.Utils;

/**
 * Created by ebricco on 9/30/17.
 */
public class GameCoordinate extends Coordinate {
    public GameCoordinate(int x, int y) {
        super(x,y);
    }

    @Override
    public GameCoordinate clone() {
        return new GameCoordinate(x, y);
    }
}
