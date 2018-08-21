package eastin.Survive.manager;

import eastin.Survive.GameState;
import eastin.Survive.World;
import eastin.Survive.objects.Barrier;
import eastin.Survive.objects.RectangularObject;
import eastin.Survive.objects.RenderableRectangle;
import eastin.Survive.utils.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ebricco on 11/27/16.
 * barrier locations are maintained in a 3760 x 2800 grid (x between -600 and 3160, y between -600 and 2200)
 * barriers are of diameter between 40 and 400 (size should be between 20 and 200)
 * when a barrier is in the screen range (x: 0-2560, y: 0-1600)
 */

public class Barriers implements Manager, Serializable {

    public List<Barrier> objects;

    private RectangularObject frontier;

    final double PERCENTCOVERAGE = .05d;

    static int renderCount = 0;

    public Barriers() {
        objects = new ArrayList<>();
        Coordinate screenCenter = new Coordinate(MainCharacter.STARTPOINTX, MainCharacter.STARTPOINTY);
        frontier = new RectangularObject(-GameState.WIDTH/4 + screenCenter.getX(), GameState.WIDTH/4 + screenCenter.getX(), GameState.HEIGHT/4 + screenCenter.getY(), -GameState.HEIGHT/4 + screenCenter.getY());
    }

    //TODO: note that if barriers ever become permanent, there is a performance concern with how the spawn area is expanded
    //as the frontiers expand, more and more needs to be calculated to expand it further
    public void update() {

        //could check if center changed
        //System.out.println(center.toString());

        //check which frontiers are too close to the game center

        if(World.spawningBarriers) {
            modifyFrontiers(frontier.getDistancesToPoint(World.world.mc.getCenter()));
        }
    }

    public void render() {
        //int count = 0;
        for(RenderableRectangle object:objects) {
            if(object.checkPositionAndRender()) {
                //count++;
            }
        }

        /*if(renderCount %30 ==0) {
            System.out.println("number of barriers: " + objects.size());
            System.out.println("number of barriers rendered: " + count);
        }*/

        renderCount++;
    }


    //calculates new barriers if an approaching area hasn't been calculated
    private void modifyFrontiers(Map<Direction, Integer> distances) {

        for (Direction d : distances.keySet()) {

            //System.out.println(d + ": " + Integer.toString(distances.get(d)));

            //if a frontier is too close, expand it
            if (distances.get(d) < 3000) {
                //System.out.println("expanding frontier in direction "  + d);
                expandFrontier(d, 5000 - distances.get(d));
            }

            //if a frontier is too far away, shrink it
            else if (distances.get(d) > 10000) {
                shrinkFrontier(d, distances.get(d) - 5000);
            }
        }
    }

    //expand frontier by expansionSize in direction d
    private void expandFrontier(Direction d, int expansionSize) {
        int num;
        double chance;
        int roundedChance;
        double raw;

        //System.out.println("expanding frontier");

        //calculate number of barriers to create (area of expansion * percent coverage / average area)
        //System.out.println(d);
        //System.out.println(expansionSize);
        //System.out.println(frontier.getPerpendicularRange(d));
        raw = (frontier.getPerpendicularRange(d) * expansionSize) * PERCENTCOVERAGE/ 20000;
        //System.out.println(raw);
        num = (int) raw;
        chance = raw - (double) num;
        roundedChance = (int) (chance * 100);

        num += (roundedChance >= GameState.RAND.nextInt(100)) ? 1 : 0;

        RectangularObject creationArea = frontier.getAdjacentQuad(d, expansionSize);

        //System.out.println("creating " + num + " barriers in direction " + d);
        //create barriers
        createBarriers(creationArea, num);

        //update area tracker
        frontier.expand(d, expansionSize);

        //System.out.println(frontier.toString());
    }

    private void shrinkFrontier(Direction d, int distance) {

        //System.out.println("there are currently " + objects.size() + " barriers. Shrinking barries in direction: " + d + " in the amount of " + distance);
        //System.out.println(frontier.toString());
        //System.out.println(Runner.mc.getScreen());
        RectangularObject toDespawn = frontier.getAdjacentQuad(d, distance);
        //System.out.println("toDespawn: " + toDespawn);

        int startingSize = objects.size();

        objects.removeIf(object -> object.overlapsWith(toDespawn));

        //System.out.println((startingSize - objects.size()) + " barriers removed");



        frontier.expand(d, -distance);
        //System.out.println("trimmed to " + objects.size());
    }


    //create num barriers in area
    private void createBarriers(RectangularObject area, int num) {

        //create new barriers
        for (int i = 0; i < num; i++) {

            //note: barriers can be created on top of each other
            int s = GameState.RAND.nextInt(200 - 20) + 20;

            //System.out.println("Shape of radius " + s + " created.");

            Coordinate coord = new Coordinate(GameState.RAND.nextInt(area.getWidth()) + area.getLeftBound(), GameState.RAND.nextInt(area.getHeight()) + area.getLowerBound());

            objects.add(new Barrier(coord, s, s, new Color()));
        }
    }

    public List<RenderableRectangle> getObjects(RectangularObject r) {
        List<RenderableRectangle> inArea = new ArrayList<>();
        objects.forEach(b -> {
            if(b.overlapsWith(r)) {
                inArea.add(b);
            }
        });

        return inArea;
    }

    public List<Barrier> getObjects() {
        return objects;
    }

    public void pause(long time) {

    }

    public void unpause(long time) {

    }

    public Barrier getClosest(Coordinate c) {
        double minDistance = 100000;
        double distance;
        Barrier closestBarrier = null;

        for(Barrier b:objects) {
            distance = b.getCenter().distanceTo(c);
            if (distance < minDistance) {
                minDistance = distance;
                closestBarrier = b;
            }
        }

        return closestBarrier;
    }

    public List<Barrier> getBarriersInRect(RectangularObject r) {
        List<Barrier> barriers = new ArrayList<>();

        objects.forEach(b -> {
            if(b.overlapsWith(r)) {
                barriers.add(b);
            }
        });
        return barriers;
    }
}