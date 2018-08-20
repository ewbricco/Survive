package eastin.Survive.manager;

import eastin.Survive.GameState;
import eastin.Survive.objects.MovingRectangle;
import eastin.Survive.objects.RectangularObject;
import eastin.Survive.utils.*;
import eastin.Survive.worldprovider.WorldProvider;
import eastin.Survive.worldprovider.WorldProviderFactory;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by ebricco on 11/27/16.
 */
public class MainCharacter extends MovingRectangle implements Manager {

    private final static int WIDTH = 41;
    private final static int HEIGHT = 41;
    public final static int STARTPOINTX = GameState.WIDTH/2;
    public final static int STARTPOINTY = GameState.HEIGHT/2;
    private final static Color defaultColor = new Color(0,0,0);

    static final int SPEED = 600; //px/s
    private long lastMovement;
    private Direction direction;
    private Direction facing;
    private int health;
    private boolean firing;
    private long lastFire;
    private long pausedAt;

    private final static long TIMEBETWEENFIRING = 1000;

    private WorldProvider worldProvider = WorldProviderFactory.getWorldProvider();

    public MainCharacter(){
        super(STARTPOINTX, STARTPOINTX + WIDTH, STARTPOINTY + HEIGHT, STARTPOINTY, defaultColor);
        health = 5;
        firing = false;
        lastFire = 0;
    }

    public void pause(long time) {
        pausedAt = time;
    }

    public void unpause(long time) {
        lastFire = time - (pausedAt - lastFire);
        lastMovement = time - (pausedAt - lastMovement);
    }

    public void render() {
        checkPositionAndRender();
    }

    //add barriers for other directions
    public void checkInput(int key, int action){

        if(worldProvider.getWorld().paused) {
            return;
        }

        if(key == 0) {
            System.out.println("zero key");
            return;
        }

        if(key == GLFW_KEY_W && action == GLFW_PRESS){
            direction = Direction.NORTH;
            facing = Direction.NORTH;
        } else if(key == GLFW_KEY_S && action == GLFW_PRESS){
            direction = Direction.SOUTH;
            facing = Direction.SOUTH;
        } else if(key == GLFW_KEY_A && action == GLFW_PRESS){
            direction = Direction.WEST;
            facing = Direction.WEST;
        } else if(key == GLFW_KEY_D && action == GLFW_PRESS){
            direction = Direction.EAST;
            facing = Direction.EAST;
        } else if(action == GLFW_RELEASE && key == GLFW_KEY_W && direction == Direction.NORTH){
            direction = null;
        } else if(action == GLFW_RELEASE && key == GLFW_KEY_S && direction == Direction.SOUTH){
            direction = null;
        } else if(action == GLFW_RELEASE && key == GLFW_KEY_D && direction == Direction.EAST){
            direction = null;
        } else if(action == GLFW_RELEASE && key == GLFW_KEY_A && direction == Direction.WEST){
            direction = null;
        }

        else if(key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            firing = true;
        } else if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
            firing = false;
        }



    }

    private void fireProjectile() {
        worldProvider.getWorld().projectiles.addNewProjectile(facing, getMiddleOfFace(facing));
    }

    public void update(){
        int movementDistance = (int)(SPEED * ((double)(System.nanoTime()/1000000 - lastMovement) / 1000d));
        lastMovement = System.nanoTime() / 1000000;


        if(direction != null) {
            move(direction, movementDistance, worldProvider.getWorld().barriers.getObjects());
        }

        if(firing) {
            if(System.currentTimeMillis() - lastFire > TIMEBETWEENFIRING) {
                fireProjectile();
                lastFire = System.currentTimeMillis();
            }
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
