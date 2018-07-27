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
    private int health;

    public MainCharacter(){
        super(STARTPOINTX, STARTPOINTX + WIDTH, STARTPOINTY + HEIGHT, STARTPOINTY, defaultColor);
        health = 1;
    }

    public void render() {
        checkPositionAndRender();
    }

    //add barriers for other directions
    public void checkInput(int key, int action){

        if(key == 0) {
            System.out.println("zero key");
            return;
        }

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
            move(direction, MOVEMENTDISTANCE, Runner.barriers.getObjects());
            lastMovement = System.nanoTime() / 1000000;
        }
    }

    //area representing screen
    public RectangularObject getScreen() {
        return new RectangularObject(getLeftBound() - GameState.WIDTH/2 + WIDTH/2, getRightBound() + GameState.WIDTH/2 - WIDTH/2, getUpperBound() + GameState.HEIGHT/2 - HEIGHT/2, getLowerBound() - GameState.HEIGHT/2 + HEIGHT/2);
    }

    public void takeDamage(int damage) {
        health -= damage;

        if(health <= 0) {
            System.out.println("****YOU'RE DEAD****");
            System.exit(0);
        }
    }
}
