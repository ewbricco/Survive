package Game1;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 * Created by ebricco on 11/27/16.
 * barrier locations are maintained in a 3760 x 2800 grid (x between -600 and 3160, y between -600 and 2200)
 * barriers are of diameter between 40 and 400 (size should be between 20 and 200)
 * when a barrier is in the screen range (x: 0-2560, y: 0-1600)
 */
public class Barriers {
    private ArrayList<Integer> posX;
    private ArrayList<Integer> posY;
    private ArrayList<Integer> size;
    private ArrayList<Color> color;
    private ArrayList<Integer> toRender;
    private int uncalculatedNorth;
    private int uncalculatedWest;
    private int uncalculatedSouth;
    private int uncalculatedEast;
    private long lastMovement;
    private Direction direction;


    final long TIMEBETWEENMOVEMENTS = 25;
    final int MOVEMENTDISTANCE = 15;


    public Barriers(){
        posX = new ArrayList();
        posY = new ArrayList();
        size = new ArrayList();
        color = new ArrayList();
        toRender = new ArrayList();
        uncalculatedNorth = 1000;
        uncalculatedWest = 1580;
        uncalculatedSouth = 1000;
        uncalculatedEast = 1580;
        lastMovement = 0;
    }

    public void render(){
        checkPosition();
        for(int j=0; j<toRender.size(); j++) {
            int i = toRender.get(j);
            Color c = color.get(i);
            glColor3f(c.getRed(), c.getGreen(), c.getBlue());
            glBegin(GL_QUADS);
            {
                int x = posX.get(i);
                int y = posY.get(i);
                int s = size.get(i);
                glVertex2f(x - s, y - s);
                glVertex2f(x + s, y - s);
                glVertex2f(x + s, y + s);
                glVertex2f(x - s, y + s);
            }
            glEnd();
        }
    }

    private void checkPosition(){
        toRender = new ArrayList();
        for(int i=0; i<posX.size(); i++){
            if(posX.get(i)+size.get(i) < 0 || posX.get(i)-size.get(i) > 2560){
                continue;
            }
            if(posY.get(i)+size.get(i) < 0 || posY.get(i)-size.get(i) > 1600){
                continue;
            }
            toRender.add(i);
        }
    }

    public void update(){
        if(uncalculatedNorth > 50){
            addNorthern();
        }
        else if(uncalculatedSouth > 50){
            addSouthern();
        }
        else if(uncalculatedEast > 50){
            addEastern();
        }
        else if(uncalculatedWest > 50){
            addWestern();
        }
    }

    //TODO ensure that "corners" are filled properly
    private void addNorthern(){
        int num;
        double chance;
        int roundedChance;
        double raw;

        //System.out.println("Calculating northern barriers in area with height " + uncalculatedNorth);

        //calculate number of barriers to create
        raw = (2560 * uncalculatedNorth * .05)/(14830 * 4);
        num = (int)raw;
        chance = raw - (double)num;
        roundedChance = (int)(chance * 100);

        //create certain
        for(int i=0; i<num; i++){
            //note: barriers can be created on top of each other
            color.add(new Color());
            int s = GameState.RAND.nextInt(200-20)+20;
            size.add(s);
            //System.out.println("Shape of radius " + s + " created.");
            posX.add(GameState.RAND.nextInt(3760));
            posY.add(GameState.RAND.nextInt(uncalculatedNorth) + 2200 - uncalculatedNorth);
        }

        //check for uncertain
        if(roundedChance >= GameState.RAND.nextInt(100)){
            color.add(new Color());
            int s = GameState.RAND.nextInt(200-20)+20;
            size.add(s);
            //System.out.println("Chance of " + roundedChance + "% succeeded. Radius " + s);
            posX.add(GameState.RAND.nextInt(3760) - 600);
            posY.add(GameState.RAND.nextInt(uncalculatedNorth) + 2200 - uncalculatedNorth);
        }

        //delete southern
        for(int i=0; i<posX.size(); i++){
            if(posY.get(i) < -600){
                //System.out.println("Removing barrier at y position: " + posY.get(i));
                posY.remove(i);
                posX.remove(i);
                color.remove(i);
                size.remove(i);
                //System.out.println("Number of barriers remaining: " + posY.size());
            }
        }


        uncalculatedNorth = 0;
    }

    private void addSouthern(){
        int num;
        double chance;
        int roundedChance;
        double raw;

        System.out.println("Calculating southern barriers in area with height " + uncalculatedSouth);

        //calculate number of barriers to create
        raw = (2560 * uncalculatedSouth * .05)/(14830 * 4);
        num = (int)raw;
        chance = raw - (double)num;
        roundedChance = (int)(chance * 100);

        //create certain
        for(int i=0; i<num; i++){
            //note: barriers can be created on top of each other
            color.add(new Color());
            int s = GameState.RAND.nextInt(200-20)+20;
            size.add(s);
            System.out.println("Shape of radius " + s + " created.");
            posX.add(GameState.RAND.nextInt(3760));
            posY.add(GameState.RAND.nextInt(uncalculatedSouth) - 600);
        }

        //check for uncertain
        if(roundedChance >= GameState.RAND.nextInt(100)){
            color.add(new Color());
            int s = GameState.RAND.nextInt(200-20)+20;
            size.add(s);
            System.out.println("Chance of " + roundedChance + "% succeeded. Radius " + s);
            posX.add(GameState.RAND.nextInt(3760));
            posY.add(GameState.RAND.nextInt(uncalculatedSouth) - 600);
        }

        //delete northern
        for(int i=0; i<posX.size(); i++){
            if(posY.get(i) > 2200){
                System.out.println("Removing barrier at y position: " + posY.get(i));
                posY.remove(i);
                posX.remove(i);
                color.remove(i);
                size.remove(i);
                System.out.println("Number of barriers remaining: " + posY.size());
            }
        }


        uncalculatedSouth = 0;
    }

    private void addEastern(){
        int num;
        double chance;
        int roundedChance;
        double raw;

        System.out.println("Calculating eastern barriers in area with height " + uncalculatedEast);

        //calculate number of barriers to create
        raw = (1600 * uncalculatedEast * .05)/(14830 * 4);
        num = (int)raw;
        chance = raw - (double)num;
        roundedChance = (int)(chance * 100);

        //create certain
        for(int i=0; i<num; i++){
            //note: barriers can be created on top of each other
            color.add(new Color());
            int s = GameState.RAND.nextInt(200-20)+20;
            size.add(s);
            System.out.println("Shape of radius " + s + " created.");
            posX.add(GameState.RAND.nextInt(uncalculatedEast) + 3160 - uncalculatedEast);
            posY.add(GameState.RAND.nextInt(1600));
        }

        //check for uncertain
        if(roundedChance >= GameState.RAND.nextInt(100)){
            color.add(new Color());
            int s = GameState.RAND.nextInt(200-20)+20;
            size.add(s);
            System.out.println("Chance of " + roundedChance + "% succeeded. Radius " + s);
            posX.add(GameState.RAND.nextInt(uncalculatedEast) + 3160 - uncalculatedEast);
            posY.add(GameState.RAND.nextInt(1600));
        }

        //delete western
        for(int i=0; i<posX.size(); i++){
            if(posX.get(i) < -600){
                System.out.println("Removing barrier at x position: " + posX.get(i));
                posY.remove(i);
                posX.remove(i);
                color.remove(i);
                size.remove(i);
                System.out.println("Number of barriers remaining: " + posY.size());
            }
        }


        uncalculatedEast = 0;
    }

    private void addWestern(){
        int num;
        double chance;
        int roundedChance;
        double raw;

        System.out.println("Calculating western barriers in area with height " + uncalculatedWest);

        //calculate number of barriers to create
        raw = (1600 * uncalculatedWest * .05)/(14830 * 4);
        num = (int)raw;
        chance = raw - (double)num;
        roundedChance = (int)(chance * 100);

        //create certain
        for(int i=0; i<num; i++){
            //note: barriers can be created on top of each other
            color.add(new Color());
            int s = GameState.RAND.nextInt(200-20)+20;
            size.add(s);
            System.out.println("Shape of radius " + s + " created.");
            posX.add(GameState.RAND.nextInt(uncalculatedWest) -600);
            posY.add(GameState.RAND.nextInt(1600));
        }

        //check for uncertain
        if(roundedChance >= GameState.RAND.nextInt(100)){
            color.add(new Color());
            int s = GameState.RAND.nextInt(200-20)+20;
            size.add(s);
            System.out.println("Chance of " + roundedChance + "% succeeded. Radius " + s);
            posX.add(GameState.RAND.nextInt(uncalculatedWest) -600);
            posY.add(GameState.RAND.nextInt(1600));
        }

        //delete east
        for(int i=0; i<posX.size(); i++){
            if(posX.get(i) > 3160){
                //System.out.println("Removing barrier at x position: " + posX.get(i));
                posY.remove(i);
                posX.remove(i);
                color.remove(i);
                size.remove(i);
                //System.out.println("Number of barriers remaining: " + posY.size());
            }
        }


        uncalculatedWest = 0;
    }

    //add barriers for other directions
    public void checkInput(int key, int action){
        if(key == GLFW_KEY_W && action == GLFW_PRESS){
            direction = Direction.NORTH;
        }

        else if(key == GLFW_KEY_S && action == GLFW_PRESS){
            direction = Direction.SOUTH;
        }

        else if(key == GLFW_KEY_A && action == GLFW_PRESS){
            direction = Direction.WEST;
        }

        else if(key == GLFW_KEY_D && action == GLFW_PRESS){
            direction = Direction.EAST;
        }

        else if(action == GLFW_RELEASE && key == GLFW_KEY_W && direction == Direction.NORTH){
            direction = null;
        }

        else if(action == GLFW_RELEASE && key == GLFW_KEY_S && direction == Direction.SOUTH){
            direction = null;
        }

        else if(action == GLFW_RELEASE && key == GLFW_KEY_D && direction == Direction.EAST){
            direction = null;
        }

        else if(action == GLFW_RELEASE && key == GLFW_KEY_A && direction == Direction.WEST){
            direction = null;
        }


    }
    public void move(){
        if(System.nanoTime()/1000000 - lastMovement < TIMEBETWEENMOVEMENTS){
            return;
        }
        if(direction == Direction.NORTH){
            int adj = collisionAdjustment(Direction.NORTH, Main1.mc.getX(), Main1.mc.getY(), Main1.mc.getSize());
            for(int i=0; i<posX.size(); i++){
                posY.set(i, posY.get(i) - MOVEMENTDISTANCE + adj);
            }
            uncalculatedNorth += MOVEMENTDISTANCE;
        }
        else if(direction == Direction.SOUTH){
            int adj = collisionAdjustment(Direction.SOUTH, Main1.mc.getX(), Main1.mc.getY(), Main1.mc.getSize());
            for(int i=0; i<posX.size(); i++){
                posY.set(i, posY.get(i) + MOVEMENTDISTANCE - adj);
            }
            uncalculatedSouth += MOVEMENTDISTANCE;
        }
        else if(direction == Direction.WEST){
            int adj = collisionAdjustment(Direction.WEST, Main1.mc.getX(), Main1.mc.getY(), Main1.mc.getSize());
            for(int i=0; i<posX.size(); i++){
                posX.set(i, posX.get(i) + MOVEMENTDISTANCE - adj);
            }
            uncalculatedWest += MOVEMENTDISTANCE;
        }
        else if(direction == Direction.EAST){
            int adj = collisionAdjustment(Direction.EAST, Main1.mc.getX(), Main1.mc.getY(), Main1.mc.getSize());
            for(int i=0; i<posX.size(); i++){
                posX.set(i, posX.get(i) - MOVEMENTDISTANCE + adj);
            }
            uncalculatedEast += MOVEMENTDISTANCE;
        }
        lastMovement = System.nanoTime() / 1000000;
    }

    /*check if a barrier overlaps with given quad, if it does calculate adjustment
    * note: assumes that object can only collide with one shape
     */
    private int collisionAdjustment(Direction d, int x, int y, int s){
        //boolean diffInRange;
        //boolean otherAxisAligned;
        boolean axisAligned1;
        boolean axisAligned2;

        int diff = 0, top, bottom, left, right;

        if(d == Direction.NORTH){
            top = y + s + MOVEMENTDISTANCE;
            bottom = y - s + MOVEMENTDISTANCE;
            left = x - s;
            right = x + s;
        }
        else if(d == Direction.SOUTH){
            top = y + s - MOVEMENTDISTANCE;
            bottom = y - s - MOVEMENTDISTANCE;
            left = x - s;
            right = x + s;
        }
        else if(d == Direction.WEST){
            top = y + s;
            bottom = y - s;
            left = x - s - MOVEMENTDISTANCE;
            right = x + s - MOVEMENTDISTANCE;
        }
        else if(d == Direction.EAST){
            top = y + s;
            bottom = y - s;
            left = x - s + MOVEMENTDISTANCE;
            right = x + s + MOVEMENTDISTANCE;
        }
        else{
            return 0;
        }

        for(int i=0; i<posX.size(); i++){
            axisAligned1 = (top > (posY.get(i) - size.get(i)) && top < (posY.get(i) + size.get(i)))
                    || (bottom > (posY.get(i) - size.get(i)) && bottom < (posY.get(i) + size.get(i)))
                    || (top > (posY.get(i) - size.get(i)) && bottom < (posY.get(i) + size.get(i)));

            axisAligned2 = (right > (posX.get(i) - size.get(i)) && right < (posX.get(i) + size.get(i)))
                    || (left > (posX.get(i) - size.get(i)) && left < (posX.get(i) + size.get(i)))
                    || (right > (posX.get(i) - size.get(i)) && left < (posX.get(i) + size.get(i)));
            if(axisAligned1 && axisAligned2){
                if(d == Direction.NORTH) {
                    diff = top - (posY.get(i) - size.get(i));
                }
                else if(d == Direction.SOUTH){
                    diff =  (posY.get(i) + size.get(i)) - bottom;
                }
                else if(d == Direction.WEST){
                    diff =  (posX.get(i) + size.get(i)) - left;
                }
                else if(d == Direction.EAST){
                    diff =  right - (posX.get(i) - size.get(i));
                }
                return diff;
            }
        }

        return 0;
    }

}
