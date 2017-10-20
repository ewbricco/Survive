package main.eastin.Survive;

import main.eastin.Survive.Utils.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by ebricco on 11/27/16.
 */
public class MainCharacter {

    private GameCoordinate coords; //location within map
    private RenderCoordinate renderCoords;
    private final int WIDTH = 41;
    private final int HEIGHT = 41;
    private final int STARTPOINTX = GameState.WIDTH/2;
    private final int STARTPOINTY = GameState.HEIGHT/2;
    private Color color;

    final long TIMEBETWEENMOVEMENTS = 25;
    final int MOVEMENTDISTANCE = 15;
    private long lastMovement;
    private Direction direction;

    public MainCharacter(){
        coords = new GameCoordinate(0, 0);
        renderCoords = new RenderCoordinate(STARTPOINTX, STARTPOINTY);
        lastMovement = 0;
        color = new Color(0,0,0);
    }

    public void render(){
        RenderUtils.renderQuad(renderCoords, WIDTH, HEIGHT, color);
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

        if(System.nanoTime()%100==0) {
            System.out.println("moving in direction " + direction + " and currently at " + coords.getX() + "," + coords.getY());
        }

        coords.add(MovementUtils.move(direction, coords, WIDTH, Runner.barriers, MOVEMENTDISTANCE));

        lastMovement = System.nanoTime() / 1000000;
    }

    public AreaQuad getAreaQuad() {
        return new AreaQuad(coords.getY() + GameState.HEIGHT/2, coords.getY() - GameState.HEIGHT/2, coords.getX() - GameState.WIDTH/2, coords.getX() + GameState.WIDTH/2);
    }

    public GameCoordinate getCoords() {
        return coords;
    }
}
