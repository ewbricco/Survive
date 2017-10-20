package main.eastin.Survive.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ebricco on 9/30/17.
 *
 * represents an area of absolute coordinates
 */
public class AreaQuad {
    private int northernFrontier; //ymax
    private int southernFrontier; //ymin
    private int westernFrontier; //xmin
    private int easternFrontier; //xmax


    public AreaQuad(int n, int s, int w, int e) {
        northernFrontier = n;
        southernFrontier = s;
        westernFrontier = w;
        easternFrontier = e;
    }

    public AreaQuad() {
        northernFrontier = 0;
        southernFrontier = 0;
        westernFrontier = 0;
        easternFrontier = 0;
    }

    //returns array of distances to point north, south, west, then east
    public Map<Direction, Integer> getDistancesToPoint(Coordinate c) {
        HashMap<Direction, Integer> distances = new HashMap();

        distances.put(Direction.NORTH, northernFrontier - c.getY());
        distances.put(Direction.SOUTH, c.getY() - southernFrontier);
        distances.put(Direction.WEST, c.getX() - westernFrontier);
        distances.put(Direction.EAST, easternFrontier - c.getX());

        return distances;
    }

    public int getXRange() {
        return easternFrontier - westernFrontier;
    }

    public int getYRange() {
        return northernFrontier - southernFrontier;
    }

    public int getPerpendicularRange(Direction d) {
        if(d == Direction.NORTH || d==Direction.SOUTH) {
            return getXRange();
        }
        else {
            return getYRange();
        }
    }

    //returns an Areaquad representing a rectangle next to this quad in direction d and size size
    public AreaQuad getAdjacentQuad(Direction d, int size) {
        if(d == Direction.NORTH) {
            return new AreaQuad(northernFrontier + size, northernFrontier, westernFrontier, easternFrontier);
        } else if (d == Direction.SOUTH) {
            return new AreaQuad(southernFrontier, southernFrontier - size, westernFrontier, easternFrontier);
        } else if (d == Direction.WEST) {
            return new AreaQuad(northernFrontier, southernFrontier, westernFrontier - size, westernFrontier);
        } else if (d == Direction.EAST) {
            return new AreaQuad(northernFrontier, southernFrontier, easternFrontier, easternFrontier + size);
        }

        return null;
    }

    //changes given direction by amount
    public void updateBound(Direction d, int amount) {
        if(d == Direction.NORTH) {
            northernFrontier += amount;
        } else if(d == Direction.SOUTH) {
            southernFrontier -= amount;
        } else if(d == Direction.WEST) {
            westernFrontier -= amount;
        } else if(d == Direction.EAST) {
            easternFrontier += amount;
        }
    }

    public Coordinate getBottomLeft() {
        return new Coordinate(westernFrontier, southernFrontier);
    }

    public int getNorthernFrontier() {
        return northernFrontier;
    }

    public void setNorthernFrontier(int northernFrontier) {
        this.northernFrontier = northernFrontier;
    }

    public int getSouthernFrontier() {
        return southernFrontier;
    }

    public void setSouthernFrontier(int southernFrontier) {
        this.southernFrontier = southernFrontier;
    }

    public int getWesternFrontier() {
        return westernFrontier;
    }

    public void setWesternFrontier(int westernFrontier) {
        this.westernFrontier = westernFrontier;
    }

    public int getEasternFrontier() {
        return easternFrontier;
    }

    public void setEasternFrontier(int easternFrontier) {
        this.easternFrontier = easternFrontier;
    }
}
