package eastin.Survive.utils;

import eastin.Survive.objects.RectangularObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ebricco on 8/7/18.
 */
public class NavMeshTest {

    @Test
    public void timeTest() {
        RectangularObject r1 = new RectangularObject(0, 10, 10, 0);
        RectangularObject r2 = new RectangularObject(5, 15, 30, 20);
        RectangularObject r3 = new RectangularObject(30, 40, 25, 15);
        RectangularObject r4 = new RectangularObject(100, 110, 10, -10);
        RectangularObject r5 = new RectangularObject(50, 60, 100, 90);
        RectangularObject r6 = new RectangularObject(-500, -400, -500, -600);
        RectangularObject r7 = new RectangularObject(-200, -50, 15, -67);

        ArrayList<RectangularObject> obstacles = new ArrayList<>();
        obstacles.addAll(Arrays.asList(r1, r2, r3, r4, r5, r6, r7));

        RectangularObject mover = new RectangularObject(100, 105, 105, 100);

        long start = System.currentTimeMillis();
        for(int i=0; i<100; i++) {
            NavMesh mesh = new NavMesh(mover, obstacles);
        }

        System.out.println("took: " + (System.currentTimeMillis() - start) + " ms.");

    }

    @Test
    public void testNumberOfEdges2() {
        RectangularObject r1 = new RectangularObject(0, 100, 100, 0);
        RectangularObject r2 = new RectangularObject(1000, 2000, 2000, 1000);

        ArrayList<RectangularObject> obstacles = new ArrayList<>();
        obstacles.addAll(Arrays.asList(r1, r2));

        RectangularObject mover = new RectangularObject(100, 105, 105, 100);


        NavMesh mesh = new NavMesh(mover, obstacles);

        assert(mesh.computeNumberOfEdges() == 34);
    }

    @Test
    public void testPathBasic() {
        RectangularObject r1 = new RectangularObject(0, 100, 100, 0);
        RectangularObject r2 = new RectangularObject(200, 300, 300, 200);

        RectangularObject mover = new RectangularObject(-100, -95, -100, -105);

        Node s = new Node(-100, -100);
        Node t = new Node(500, 500);

        ArrayList<RectangularObject> obstacles = new ArrayList<>();
        obstacles.addAll(Arrays.asList(r1, r2));

        NavMesh mesh = new NavMesh(mover, obstacles);

        List<Coordinate> path = mesh.getPath(s, t);

        System.out.println("path:");
        for(Coordinate c:path) {
            System.out.println(c.toString());
        }
    }


    @Test
    public void testPathForUnnecessaryClingingToBarriers() {
        RectangularObject r1 = new RectangularObject(0, 100, 100, 0);

        RectangularObject mover = new RectangularObject(0, 10, -10, -20);

        Node s = new Node(mover.getBottomLeft());
        Node t = new Node(500, -20);

        ArrayList<RectangularObject> obstacles = new ArrayList<>();
        obstacles.addAll(Arrays.asList(r1));

        NavMesh mesh = new NavMesh(mover, obstacles);

        List<Coordinate> path = mesh.getPath(s, t);

        System.out.println("path:");
        for(Coordinate c:path) {
            System.out.println(c.toString());
        }

        assert(path.size() == 2);
    }
}
