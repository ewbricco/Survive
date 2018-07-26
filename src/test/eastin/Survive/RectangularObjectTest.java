package eastin.Survive;

import eastin.Survive.Utils.Color;
import org.junit.Test;

/**
 * Created by ebricco on 7/26/18.
 */
public class RectangularObjectTest {

    @Test
    public void testOverlapsWithNonOverlapping() {
        RectangularObject rect1 = new RectangularObject(727,1073,149,-197);
        RectangularObject rect2 = new RectangularObject(1254,1336,841,759);

        assert(!rect1.overlapsWith(rect2));
        assert(!rect2.overlapsWith(rect1));
    }

    @Test
    public void testOverlapsWithNonOverlappingClose() {
        RectangularObject rect1 = new RectangularObject(0,10,10,0);
        RectangularObject rect2 = new RectangularObject(11,15,10,0);

        assert(!rect1.overlapsWith(rect2));
        assert(!rect2.overlapsWith(rect1));
    }

    @Test
    public void testOverlapsWithOverlappingTotalEnclosure() {
        RectangularObject rect1 = new RectangularObject(0,1000,1000,0);
        RectangularObject rect2 = new RectangularObject(10,50,50,10);

        assert(rect1.overlapsWith(rect2));
        assert(rect2.overlapsWith(rect1));
    }


    @Test
    public void testOverlapsWithOverlappingPartial() {
        RectangularObject rect1 = new RectangularObject(0,1000,1000,0);
        RectangularObject rect2 = new RectangularObject(500,1010,1010,500);

        assert(rect1.overlapsWith(rect2));
        assert(rect2.overlapsWith(rect1));
    }

    @Test
    public void testOverlapsWithSameTriangle() {
        RectangularObject rect1 = new RectangularObject(0,1000,1000,0);
        RectangularObject rect2 = new RectangularObject(0,1000,1000,0);

        assert(rect1.overlapsWith(rect2));
        assert(rect2.overlapsWith(rect1));
    }
}
