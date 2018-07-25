package main.eastin.Survive;

import main.eastin.Survive.Utils.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ebricco on 11/27/16.
 * barrier locations are maintained in a 3760 x 2800 grid (x between -600 and 3160, y between -600 and 2200)
 * barriers are of diameter between 40 and 400 (size should be between 20 and 200)
 * when a barrier is in the screen range (x: 0-2560, y: 0-1600)
 */
public class Barriers {
    public ArrayList<Coordinate> coords;
    public ArrayList<Integer> size;
    private ArrayList<Color> color;
    private ArrayList<Integer> toRender;
    //public int uncalculatedNorth;
    //public int uncalculatedWest;
    //public int uncalculatedSouth;
    //public int uncalculatedEast;

    private AreaQuad frontier;

    final double PERCENTCOVERAGE = .05d;


    public Barriers() {
        coords = new ArrayList();
        size = new ArrayList();
        color = new ArrayList();
        toRender = new ArrayList();

        frontier = new AreaQuad(GameState.HEIGHT/4, -GameState.HEIGHT/4, -GameState.WIDTH/4, GameState.WIDTH/4);
    }

    public void render(AreaQuad screen) {
        checkPosition(screen);
        for (int j = 0; j < toRender.size(); j++) {
            int i = toRender.get(j);
            RenderUtils.renderQuad(Coordinate.Difference(coords.get(i), screen.getBottomLeft()), size.get(i), size.get(i), color.get(i));
        }
    }

    //find all barriers that are in the given coordinate area
    private void checkPosition(AreaQuad screen) {
        toRender = new ArrayList();
        for (int i = 0; i < coords.size(); i++) {
            if (coords.get(i).getX() + size.get(i) < screen.getWesternFrontier() || coords.get(i).getX() - size.get(i) > screen.getEasternFrontier()) {
                continue;
            }
            if (coords.get(i).getY() + size.get(i) < screen.getSouthernFrontier() || coords.get(i).getY() - size.get(i) > screen.getNorthernFrontier()) {
                continue;
            }
            toRender.add(i);
        }
    }

    //TODO: note that if barriers ever become permanent, there is a performance concern with how the spawn area is expanded
    //as the frontiers expand, more and more needs to be calculated to expand it further
    public void update(GameCoordinate center) {
        //check which frontiers are too close to the game center
        modifyFrontiers(center, frontier.getDistancesToPoint(center));
    }


    //calculates new barriers if an approaching area hasn't been calculated
    private void modifyFrontiers(GameCoordinate center, Map<Direction, Integer> distances) {

        for (Direction d : distances.keySet()) {

            //if a frontier is too close, expand it
            if (distances.get(d) < 3000) {
                System.out.println("expanding frontier in direction "  + d);
                expandFrontier(d, 4000 - distances.get(d));
            }

            //if a frontier is too far away, shrink it
            else if (distances.get(d) > 5000) {
                //TODO
            }
        }
    }

    //expand frontier by expansionSize in direction d
    private void expandFrontier(Direction d, int expansionSize) {
        int num;
        double chance;
        int roundedChance;
        double raw;

        //calculate number of barriers to create (area of expansion * percent coverage / average area)
        raw = (frontier.getPerpendicularRange(d) * expansionSize) * PERCENTCOVERAGE/ 20000;
        num = (int) raw;
        chance = raw - (double) num;
        roundedChance = (int) (chance * 100);

        num += (roundedChance >= GameState.RAND.nextInt(100)) ? 1 : 0;

        AreaQuad creationArea = frontier.getAdjacentQuad(d, expansionSize);

        System.out.println("creating " + num + " barriers in direction " + d);
        //create barriers
        createBarriers(creationArea, num);

        //update area tracker
        frontier.updateBound(d, expansionSize);
    }


    //create num barriers in area
    private void createBarriers(AreaQuad area, int num) {
        //create new barriers
        for (int i = 0; i < num; i++) {
            //note: barriers can be created on top of each other
            color.add(new Color());
            int s = GameState.RAND.nextInt(200 - 20) + 20;
            size.add(s);
            //System.out.println("Shape of radius " + s + " created.");

            GameCoordinate coord = new GameCoordinate(GameState.RAND.nextInt(area.getXRange()) + area.getWesternFrontier(), GameState.RAND.nextInt(area.getYRange()) + area.getSouthernFrontier());
            coord.print();
            coords.add(coord);
        }
    }
}