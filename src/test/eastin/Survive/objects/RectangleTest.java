package eastin.Survive.objects;

import eastin.Survive.utils.Coordinate;
import eastin.Survive.utils.Direction;
import org.junit.Test;

import java.util.Map;

/**
 * Created by ebricco on 7/26/18.
 */
public class RectangleTest {

    @Test
    public void testOverlapsWithNonOverlapping() {
        Rectangle rect1 = new Rectangle(727,1073,149,-197);
        Rectangle rect2 = new Rectangle(1254,1336,841,759);

        assert(!rect1.overlapsWith(rect2));
        assert(!rect2.overlapsWith(rect1));
    }

    @Test
    public void testOverlapsWithNonOverlappingClose() {
        Rectangle rect1 = new Rectangle(0,10,10,0);
        Rectangle rect2 = new Rectangle(11,15,10,0);

        assert(!rect1.overlapsWith(rect2));
        assert(!rect2.overlapsWith(rect1));
    }

    @Test
    public void testOverlapsWithOverlappingTotalEnclosure() {
        Rectangle rect1 = new Rectangle(0,1000,1000,0);
        Rectangle rect2 = new Rectangle(10,50,50,10);

        assert(rect1.overlapsWith(rect2));
        assert(rect2.overlapsWith(rect1));
    }


    @Test
    public void testOverlapsWithOverlappingPartial() {
        Rectangle rect1 = new Rectangle(0,1000,1000,0);
        Rectangle rect2 = new Rectangle(500,1010,1010,500);

        assert(rect1.overlapsWith(rect2));
        assert(rect2.overlapsWith(rect1));
    }

    @Test
    public void testOverlapsWithSameTriangle() {
        Rectangle rect1 = new Rectangle(0,1000,1000,0);
        Rectangle rect2 = new Rectangle(0,1000,1000,0);

        assert(rect1.overlapsWith(rect2));
        assert(rect2.overlapsWith(rect1));
    }

    @Test
    public void testOnePixelLineOverlap() {
        Rectangle rect1 = new Rectangle(0,1000,1000,0);
        Rectangle rect2 = new Rectangle(1000,1002,1000,0);

        assert(rect1.overlapsWith(rect2));
        assert(rect2.overlapsWith(rect1));
    }

    @Test
    public void testDistancesToPonint() {
        Rectangle rect1 = new Rectangle(-3700, 8870, 5820, -4180);

        Map<Direction, Integer> distances =  rect1.getDistancesToPoint(new Coordinate(2830, 715));

        assert(distances.get(Direction.WEST) == 6530);
        assert(distances.get(Direction.EAST) == 6040);
        assert(distances.get(Direction.NORTH) == 5105);
        assert(distances.get(Direction.SOUTH) == 4895);
    }
}
