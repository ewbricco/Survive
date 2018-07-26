package eastin.Survive;

import eastin.Survive.Utils.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by ebricco on 11/27/16.
 */
public class MainCharacter extends MovingRectangle {

    private final static int WIDTH = 41;
    private final static int HEIGHT = 41;
    private final static int STARTPOINTX = GameState.WIDTH/2;
    private final static int STARTPOINTY = GameState.HEIGHT/2;
    private final static Color defaultColor = new Color(0,0,0);

    static final long TIMEBETWEENMOVEMENTS = 25;
    static final int MOVEMENTDISTANCE = 15;
    private long lastMovement;
    private Direction direction;

    public MainCharacter(){
        super(new GameCoordinate(STARTPOINTX, STARTPOINTY), WIDTH, HEIGHT, defaultColor);
    }

    public void render() {
        checkPositionAndRender();
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

    public void update(){
        if(System.nanoTime()/1000000 - lastMovement < TIMEBETWEENMOVEMENTS){
            return;
        }

        if(direction != null) {
            System.out.println("moving mc. current quad: " + toString());
            move(direction, MOVEMENTDISTANCE, Runner.barriers.getObjects());
            lastMovement = System.nanoTime() / 1000000;
        }
    }

    //coordinates that are on screen
    public AreaQuad getAreaQuad() {
        return new AreaQuad(coord.getY() + GameState.HEIGHT/2, coord.getY() - GameState.HEIGHT/2, coord.getX() - GameState.WIDTH/2, coord.getX() + GameState.WIDTH/2);
    }

    public GameCoordinate getCoords() {
        return coord;
    }
}
