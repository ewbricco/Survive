package eastin.Survive.utils;

import eastin.Survive.Runner;
import eastin.Survive.World;
import eastin.Survive.objects.Barrier;
import eastin.Survive.objects.Enemy;
import eastin.Survive.objects.RectangularObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

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

        System.out.println("took: " + (System.currentTimeMillis() - start) + " ms to generate NavMeshes");

        Node node1 = new Node(90, 0);
        Node node2 = new Node(-1000, -1000);

        long start2 = System.currentTimeMillis();
        for(int i=0; i<100; i++) {
            NavMesh mesh = new NavMesh(mover, obstacles);
        }

        System.out.println("took: " + (System.currentTimeMillis() - start2) + " ms to calculate paths");


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
    public void testPathBasic2() {

        World world = new World();
        world.barriers.getObjects().add(new Barrier(6314, 6494, 575, 534, new Color()));

        world.enemies.addEnemy(new Coordinate(5430, 688));

        world.mc.setBounds(new RectangularObject(6272, 6313, 575, 534));

        Runner.DoIf doif = new Runner.RunEveryNth(30, () -> {
            if(world.enemies.getObjects().size() > 0) {
                System.out.println(world.enemies.getObjects().get(0).toString());
                world.enemies.getObjects().get(0).path.forEach(c -> System.out.println(c));
                System.out.println(world.enemies.getObjects().get(0).positionInPath);
            }
        });

        world.simulateGameLoop(Arrays.asList(doif), () -> world.enemies.getObjects().size() == 0);
    }

    @Test
    public void testPathBasic3() {

        World world = new World();
        World.spawningBarriers = false;
        World.spawningEnemies = false;
        Enemy.TIMEBETWEENPATHUPDATE = 10000;

        world.barriers.getObjects().add(new Barrier(1055, 1151, 326, 230, new Color(0,0,1)));

        world.enemies.addEnemy(new Coordinate(2000, 160));

        world.mc.setBounds(new RectangularObject(1013, 1054, 309, 268));

        Runner.DoIf doif = new Runner.RunEveryNth(30, () -> {
            /*if(world.enemies.getObjects().size() > 0) {
                System.out.println(world.enemies.getObjects().get(0).toString());
                c
                System.out.println(world.enemies.getObjects().get(0).positionInPath);
            }*/
        });

        //middle steps should include bottom left of barrier (1055, 230) - enemy size 60 -1 (994, 169)

        long start = System.currentTimeMillis();

        Runner.run(world, Arrays.asList(doif), () -> world.enemies.getObjects().size() == 0 || System.currentTimeMillis() - start > 10000);

        assert(world.barriers.getObjects().size() == 1);
        assert(world.enemies.getObjects().size() == 0);
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
