package eastin.Survive.Utils;

import org.junit.Test;

/**
 * Created by ebricco on 7/26/18.
 */
public class CoordinateTest {

    @Test
    public void testPythagoreanScale() {
        Coordinate c = new Coordinate(4, 10);
        c.pythagoreanScale(20000);
        assert(c.getHypotenuse() > 20000 - 5 && c.getHypotenuse() < 20000 + 5);
    }
}
