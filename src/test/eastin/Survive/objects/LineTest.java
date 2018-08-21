package eastin.Survive.objects;

import eastin.Survive.utils.Coordinate;
import eastin.Survive.utils.Node;
import org.junit.Test;

/**
 * Created by ebricco on 8/7/18.
 */
public class LineTest {

    @Test
    public void ensureSlopeInitializedTestSimple() {
        Line l1 = new Line(new Node(0,0), new Node(10,10));
        assert(l1.getSlope() == 1.0d);
        assert(l1.getIntercept() == 0.0d);
    }

    @Test
    public void ensureSlopeInitializedTestPostive() {
        Line l1 = new Line(new Node(-2,5), new Node(-10,-10));
        assert(l1.getSlope() == 1.875d);
        assert(l1.getIntercept() == 8.75d);
    }

    @Test
    public void ensureSlopeInitializedTestNegative() {
        Line l1 = new Line(new Node(-2,5), new Node(10,-10));
        assert(l1.getSlope() == -1.25);
        assert(l1.getIntercept() == 2.5);
    }

    @Test
    public void evaluateTest1() {
        Line l1 = new Line(new Node(0,0), new Node(10,10));

        assert(l1.getYAt(5) == 5);
    }


    @Test
    public void testOverlapsWithRectangleNotOverlapping() {
        Line l1 = new Line(new Node(0,0), new Node(10,10));
        RectangularObject r1 = new RectangularObject(5, 10, 2, -2);

        assert(!l1.overlapsWith(r1));
        assert(!r1.overlapsWith(l1));
    }

    @Test
    public void testLineThroughMiddle() {
        Line l1 = new Line(new Node(0,0), new Node(0,10));
        RectangularObject r1 = new RectangularObject(-10, 100, 55, -55);

        assert(l1.overlapsWith(r1));
        assert(r1.overlapsWith(l1));
    }

    @Test
    public void continuedLineRunsThroughRectangle() {
        Line l1 = new Line(new Node(0,0), new Node(10,10));
        RectangularObject r1 = new RectangularObject(11, 100, 100, -100);

        assert(!l1.overlapsWith(r1));
        assert(!r1.overlapsWith(l1));
    }

    @Test
    public void overlappingClose() {
        Line l1 = new Line(new Node(0,0), new Node(10,10));
        RectangularObject r1 = new RectangularObject(10, 100, 100, -100);

        assert(l1.overlapsWith(r1));
        assert(r1.overlapsWith(l1));
    }

    @Test
    public void overlappingDiagonal() {
        Line l1 = new Line(new Node(101,101), new Node(-5,-5));
        RectangularObject r1 = new RectangularObject(0, 100, 100, 0);

        assert(l1.overlapsWith(r1));
        assert(r1.overlapsWith(l1));
    }

    @Test
    public void overlappingRealCase() {
        Line l1 = new Line(new Coordinate(994,249), new Coordinate(1152,169));
        RectangularObject r1 = new RectangularObject(996, 1151, 326, 171);

        assert(l1.overlapsWith(r1));
        assert(r1.overlapsWith(l1));
    }

}
