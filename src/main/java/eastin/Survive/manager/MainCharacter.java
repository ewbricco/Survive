package eastin.Survive.manager;

import eastin.Survive.GameState;
import eastin.Survive.World;
import eastin.Survive.objects.Enemy;
import eastin.Survive.objects.Item;
import eastin.Survive.objects.MovingRectangle;
import eastin.Survive.objects.Rectangle;
import eastin.Survive.utils.*;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by ebricco on 11/27/16.
 */
public class MainCharacter extends MovingRectangle implements Manager {

    private final static int WIDTH = 80;
    private final static int HEIGHT = 80;
    public final static int STARTPOINTX = GameState.WIDTH/2;
    public final static int STARTPOINTY = GameState.HEIGHT/2;
    private final static Color defaultColor = new Color(0,0,0);
    private final static String GUNSHOT = "gunshot.ogg";
    private final static String RELOAD = "reload.ogg";
    private final static String POWERUP = "powerup.ogg";
    private ArrayList<Rectangle> damageFrom;

    static final int SPEED = 600; //px/s
    private long lastMovement;
    private Direction direction;
    private Direction facing;
    private Direction lastDirection;
    public int health;
    private boolean firing;
    private long lastFire;
    private long pausedAt;
    private boolean loaded;

    private static long TIMEBETWEENFIRING = 1000;
    private final static long RELOADOFFSET = TIMEBETWEENFIRING/2;

    public MainCharacter(){
        super(STARTPOINTX, STARTPOINTX + WIDTH, STARTPOINTY + HEIGHT, STARTPOINTY, defaultColor);
        health = 5;
        firing = false;
        lastFire = 0;
        facing = Direction.NORTH;
        loaded = true;
        damageFrom = new ArrayList<>();
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

        if(World.world.paused) {
            return;
        }

        if(key == 0) {
            System.out.println("zero key");
            return;
        }

        if(key == GLFW_KEY_W && action == GLFW_PRESS){
            setDirection(Direction.NORTH);
        } else if(key == GLFW_KEY_S && action == GLFW_PRESS){
            setDirection(Direction.SOUTH);
        } else if(key == GLFW_KEY_A && action == GLFW_PRESS){
            setDirection(Direction.WEST);
        } else if(key == GLFW_KEY_D && action == GLFW_PRESS){
            setDirection(Direction.EAST);
        } else if(action == GLFW_RELEASE && key == GLFW_KEY_W){
            releaseDirection(Direction.NORTH);
        } else if(action == GLFW_RELEASE && key == GLFW_KEY_S){
            releaseDirection(Direction.SOUTH);
        } else if(action == GLFW_RELEASE && key == GLFW_KEY_D){
            releaseDirection(Direction.EAST);
        } else if(action == GLFW_RELEASE && key == GLFW_KEY_A){
            releaseDirection(Direction.WEST);
        }

        else if(key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
            firing = true;
        } else if(key == GLFW_KEY_SPACE && action == GLFW_RELEASE) {
            firing = false;
        }



    }

    private void setDirection(Direction d) {
        if(lastDirection != direction) {
            lastDirection = direction;
        }
        direction = d;
        facing = d;
    }

    private void releaseDirection(Direction d) {
        if(direction == d) {
            direction = null;
            if(lastDirection != null) {
                direction = lastDirection;
                facing = lastDirection;
                lastDirection = null;
            }
        }
        if(lastDirection == d) {
            lastDirection = null;
        }
    }

    private void fireProjectile() {
        World.world.projectiles.addNewProjectile(facing, getMiddleOfFace(facing));
        World.world.sounds.playSound(GUNSHOT);
        loaded = false;
    }

    private void reload() {
        World.world.sounds.playSound(RELOAD);
        loaded = true;
    }

    public void update(){
        int movementDistance = (int)(SPEED * ((double)(System.nanoTime()/1000000 - lastMovement) / 1000d));
        lastMovement = System.nanoTime() / 1000000;

        if(direction == null && lastDirection != null) {
            direction = lastDirection;
        }
        if(direction != null) {
            List<Rectangle> interactables = new ArrayList<>();
            interactables.addAll(World.world.barriers.getObjects());
            interactables.addAll(World.world.enemies.getObjects());
            interactables.addAll(World.world.items.getObjects());

            handleCollision(move(direction, movementDistance, interactables));
        }

        if(firing) {
            if(System.currentTimeMillis() - lastFire > TIMEBETWEENFIRING) {
                fireProjectile();
                lastFire = System.currentTimeMillis();
            }
        }

        if(!loaded && System.currentTimeMillis() - lastFire > RELOADOFFSET) {
            reload();
        }
    }



    private void handleCollision(Rectangle object) {
        if(object instanceof Enemy) {
            Enemy enemy = ((Enemy)object);
            if(!enemy.dead) {
                enemy.takeDamage(1, true);
                takeDamage(1, object);
            }
        } else if(object instanceof Item) {
            ((Item)object).despawn();
            TIMEBETWEENFIRING /= 2;
            World.world.sounds.playSound(POWERUP);
        }
    }

    //area representing screen
    public Rectangle getScreen() {
        return new Rectangle(getLeftBound() - GameState.WIDTH/2 + WIDTH/2, getRightBound() + GameState.WIDTH/2 - WIDTH/2, getUpperBound() + GameState.HEIGHT/2 - HEIGHT/2, getLowerBound() - GameState.HEIGHT/2 + HEIGHT/2);
    }

    public void takeDamage(int damage, Rectangle enemy) {
        if(damageFrom.contains(enemy)) {
            return;
        }

        health -= damage;

        damageFrom.add(enemy);

        if(health <= 0) {
            System.out.println("****YOU'RE DEAD****");
            System.out.println("score: " + Enemy.killCount);
            System.exit(0);
        }
    }


}
