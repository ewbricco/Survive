package eastin.Survive.manager;

import eastin.Survive.GameState;
import eastin.Survive.World;
import eastin.Survive.objects.Item;
import eastin.Survive.objects.Rectangle;
import eastin.Survive.objects.RenderableRectangle;
import eastin.Survive.utils.Coordinate;
import eastin.Survive.utils.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ebricco on 8/23/18.
 */
public class Items implements Manager {

    public List<Item> objects;
    private Rectangle frontier;
    final double PERCENTCOVERAGE = .01d;

    public Items() {
        objects = new ArrayList<>();
        Coordinate screenCenter = new Coordinate(MainCharacter.STARTPOINTX, MainCharacter.STARTPOINTY);
        frontier = new Rectangle(-GameState.WIDTH/4 + screenCenter.getX(), GameState.WIDTH/4 + screenCenter.getX(), GameState.HEIGHT/4 + screenCenter.getY(), -GameState.HEIGHT/4 + screenCenter.getY());
    }

    public void render() {
        for(RenderableRectangle object:objects) {
            object.checkPositionAndRender();
        }
    }

    public void update() {
        int initialSize = objects.size();
        objects.removeIf(i -> i.toDespawn);
        if(objects.size() != initialSize) {
            World.world.enemies.lastNavRefresh = 0;
        }
        modifyFrontiers(frontier.getDistancesToPoint(World.world.mc.getCenter()));
    }

    private void modifyFrontiers(Map<Direction, Integer> distances) {

        for (Direction d : distances.keySet()) {

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

        raw = (frontier.getPerpendicularRange(d) * expansionSize) * PERCENTCOVERAGE/ 20000;
        num = (int) raw;
        chance = raw - (double) num;
        roundedChance = (int) (chance * 100);

        num += (roundedChance >= GameState.RAND.nextInt(100)) ? 1 : 0;

        Rectangle creationArea = frontier.getAdjacentQuad(d, expansionSize);

        createItems(creationArea, num);

        frontier.expand(d, expansionSize);
    }

    private void shrinkFrontier(Direction d, int distance) {

        Rectangle toDespawn = frontier.getAdjacentQuad(d, distance);

        objects.removeIf(object -> object.overlapsWith(toDespawn));

        frontier.expand(d, -distance);
    }

    //create num items in area
    private void createItems(Rectangle area, int num) {
        for (int i = 0; i < num; i++) {
            newItem(area);
        }
    }

    public void newItem(Rectangle area) {

        int retryAttempts = 5;

        while(retryAttempts > 0) {
            Coordinate coord = new Coordinate(GameState.RAND.nextInt(area.getWidth()) + area.getLeftBound(), GameState.RAND.nextInt(area.getHeight()) + area.getLowerBound());

            Item item = new Item(coord);

            if(World.world.barriers.getBarriersInRect(item).size() > 0) {
                retryAttempts--;
            } else {
                objects.add(item);
                break;
            }

            if(retryAttempts == 0) {
                objects.add(item);
            }
        }
    }

    public List<Item> getItemsInRect(Rectangle r) {
        List<Item> items = new ArrayList<>();

        objects.forEach(i -> {
            if(i.overlapsWith(r)) {
                items.add(i);
            }
        });

        return items;
    }

    public void pause(long time) {

    }

    public void unpause(long time) {

    }

    public List<Item> getObjects() {
        return objects;
    }
}
