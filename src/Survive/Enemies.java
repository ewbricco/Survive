package Survive;

import Survive.Utils.Color;

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
        for(int j=0; j<toRender.size(); j++) {
            int i = toRender.get(j);
            Color c = COLOR;
            glColor3f(c.getRed(), c.getGreen(), c.getBlue());
            glBegin(GL_QUADS);
            {
                int x = posX.get(i);
                int y = posY.get(i);
                glVertex2f(x - SIZE, y - SIZE);
                glVertex2f(x + SIZE, y - SIZE);
                glVertex2f(x + SIZE, y + SIZE);
                glVertex2f(x - SIZE, y + SIZE);
            }
            glEnd();
        }
    }

    private void checkPosition(){
        toRender = new ArrayList();
        for(int i=0; i<posX.size(); i++){
            if(posX.get(i)+SIZE < 0 || posX.get(i)-SIZE > 2560){
                continue;
            }
            if(posY.get(i)+SIZE < 0 || posY.get(i)-SIZE > 1600){
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
    public void move(int targetX, int targetY) {
        for (int i = 0; i < posX.size(); i++) {
            if (targetX < posX.get(i)) {
                posX.set(i, posX.get(i) - MOVEMENTDISTANCE);
            } else {
                posX.set(i, posX.get(i) + MOVEMENTDISTANCE);
            }
            if (targetY < posY.get(i)) {
                posY.set(i, posY.get(i) - MOVEMENTDISTANCE);
            } else {
                posY.set(i, posY.get(i) + MOVEMENTDISTANCE);
            }
        }
    }

    //gets main charcter
    public void update(int x, int y){
        if(System.nanoTime() / 1000000 - lastMovement > TIMEBETWEENMOVEMENTS){
            move(x,y);
            lastMovement = System.nanoTime() / 1000000;
        }
    }
}
