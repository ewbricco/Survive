package main.eastin.Survive;

import main.eastin.Survive.Utils.AreaQuad;
import main.eastin.Survive.Utils.Color;
import main.eastin.Survive.Utils.Coordinate;
import main.eastin.Survive.Utils.RenderUtils;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 * Created by ebricco on 12/2/16.
 */
public class Enemies {
    private ArrayList<Integer> posX;
    private ArrayList<Integer> posY;
    private ArrayList<Integer> toRender;
    private long lastMovement;

    final long TIMEBETWEENMOVEMENTS = 100;
    final int MOVEMENTDISTANCE = 5;
    private final Color COLOR = new Color(1,0,0);
    private final int SIZE = 30;

    public Enemies(){
        posX = new ArrayList();
        posY = new ArrayList();
        toRender = new ArrayList();
        lastMovement = System.nanoTime() / 1000000;
        init();
    }

    public void render(){
        checkPosition();
        for (int j = 0; j < toRender.size(); j++) {
            int i = toRender.get(j);
            AreaQuad screen = Runner.mc.getAreaQuad();
            RenderUtils.renderQuad(Coordinate.Difference(new Coordinate(posX.get(j), posY.get(j)), screen.getBottomLeft()), SIZE, SIZE, COLOR);
        }
    }

    //computes which enemies to render
    private void checkPosition(){
        toRender = new ArrayList();
        AreaQuad screen = Runner.mc.getAreaQuad();
        for(int i=0; i<posX.size(); i++){
            if(posX.get(i)+SIZE < screen.getWesternFrontier() || posX.get(i)-SIZE > screen.getEasternFrontier()){
                continue;
            }
            if(posY.get(i)+SIZE < screen.getSouthernFrontier() || posY.get(i)-SIZE > screen.getNorthernFrontier()){
                continue;
            }
            toRender.add(i);
        }
    }

    private void init(){
        posX.add(300);
        posY.add(500);
    }

    //seeks target
    public void move(Coordinate target) {
        for (int i = 0; i < posX.size(); i++) {
            if (target.getX() < posX.get(i)) {
                posX.set(i, posX.get(i) - MOVEMENTDISTANCE);
            } else {
                posX.set(i, posX.get(i) + MOVEMENTDISTANCE);
            }
            if (target.getY() < posY.get(i)) {
                posY.set(i, posY.get(i) - MOVEMENTDISTANCE);
            } else {
                posY.set(i, posY.get(i) + MOVEMENTDISTANCE);
            }
        }
    }

    //attempts to reach target
    public void update(Coordinate target){
        if(System.nanoTime() / 1000000 - lastMovement > TIMEBETWEENMOVEMENTS){
            move(target);
            lastMovement = System.nanoTime() / 1000000;
        }
    }
}
