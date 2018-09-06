package eastin.Survive.manager;

import eastin.Survive.GameState;
import eastin.Survive.World;
import eastin.Survive.objects.Enemy;
import eastin.Survive.objects.Line;
import eastin.Survive.objects.Rectangle;
import eastin.Survive.utils.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glLineWidth;

/**
 * Created by ebricco on 12/2/16.
 */
public class Enemies implements Manager, Serializable {

    public List<Enemy> objects;

    private NavMesh nav;

    private long lastSpawn;

    private final int TIMEBETWEENSPAWNS = 1500;
    public final int SIZE = 60;
    private final Color COLOR = new Color(.88f, .1f,.88f);
    private final long TIMEBETWEENNAVREFRESH = 1000;
    public long lastNavRefresh;
    private long pausedAt;

    private static final Coordinate STARTINGPOSITION = new Coordinate(250, 450);

    public Enemies(){
        objects = new ArrayList<>();
        //objects.add(new Enemy(STARTINGPOSITION.getX(), STARTINGPOSITION.getX() + SIZE, STARTINGPOSITION.getY() + SIZE, STARTINGPOSITION.getY(), COLOR));
        lastSpawn = 0;
        lastNavRefresh = 0;
        Enemy.lastSpeedUpdate=System.currentTimeMillis();
        Enemy.lastHealthUpdate=System.currentTimeMillis();
    }

    public List<Enemy> getObjects() {
        return objects;
    }

    public void render(){
        if(World.world.renderNav && nav != null) {
            nav.render();
        }

        objects.stream().forEach(object -> {
            if(World.world.renderNav && nav != null && object.path != null) {
                for (int i=0; i<object.path.size()-1; i++) {
                    glLineWidth(3);
                    (new Line(object.path.get(i), object.path.get(i+1))).render(new Color(1,0,1));
                    glLineWidth(1);
                }
            }
            object.checkPositionAndRender();
        });
    }

    //seeks target
    public void move(Rectangle target) {
        objects.removeIf(enemy -> enemy.toDespawn);

        List<Rectangle> interactables = new ArrayList<>();
        interactables.addAll(objects);
        interactables.add(World.world.mc);
        interactables.addAll(World.world.barriers.getObjects());

        objects.forEach(object -> {
            object.update(target, interactables, nav); //doesn't need to interact with barriers if routing is working correctly
        });
    }

    private void createNewEnemy() {

        int retryAttempts = 5;
        Enemy e;

        while(retryAttempts > 0) {
            e = getEnemy(getCoordinateAroundMc());

            if(World.world.barriers.getBarriersInRect(e).size() > 0) {
                    retryAttempts--;
            } else {
                objects.add(e);
                break;
            }

            if(retryAttempts == 0) {
                System.out.println("couldn't place enemy");
            }
        }
    }

    public Coordinate getCoordinateAroundMc() {
        Coordinate c = World.world.mc.getCenter();

        //System.out.println("mc center: " + Runner.mc.getCenter().toString());

        int xDiff = GameState.RAND.nextInt(1000) + 250;
        if(System.currentTimeMillis() %2 == 0) {
            xDiff *= -1;
        }
        int yDiff = GameState.RAND.nextInt(1000) + 250;
        if(xDiff %2 == 0) {
            yDiff *= -1;
        }

        c.addX(xDiff);
        c.addY(yDiff);

        return c;
    }

    public Enemy getEnemy(Coordinate c) {
        return new Enemy(c.getX(), c.getX() + SIZE, c.getY() + SIZE, c.getY(), COLOR);
    }

    //attempts to reach target
    public void update(){

        if(System.currentTimeMillis() - Enemy.lastSpeedUpdate > Enemy.TIMEBETWEENSPEEDINCREASE) {
            System.out.println("increasing speed");
            Enemy.SPEED += 2;
            Enemy.lastSpeedUpdate = System.currentTimeMillis();
        }

        if(System.currentTimeMillis() - Enemy.lastHealthUpdate > Enemy.TIMEBETWEENHEALTHINCREASE) {
            System.out.println("updating health");
            Enemy.INITIALHEALTH += 1;
            Enemy.lastHealthUpdate = System.currentTimeMillis();
        }

        if(System.currentTimeMillis() - lastNavRefresh > TIMEBETWEENNAVREFRESH && objects.size() > 0) {
            int buffer = (int)TIMEBETWEENNAVREFRESH/1000 * MainCharacter.SPEED;
            Rectangle navArea = World.world.mc.getScreen();
            navArea.expandInAllDirections(buffer);

            List<Rectangle> interactables = new ArrayList<>();
            interactables.addAll(World.world.barriers.getBarriersInRect(navArea));
            interactables.addAll(World.world.items.getItemsInRect(navArea));
            nav = new NavMesh(new Rectangle(0, SIZE, SIZE, 0), interactables);
            lastNavRefresh = System.currentTimeMillis();
        }

        move(World.world.getWorld().mc);


        if(System.nanoTime() / 1000000 - lastSpawn > TIMEBETWEENSPAWNS && World.world.getWorld().spawningEnemies) {
            createNewEnemy();
            lastSpawn = System.nanoTime() / 1000000;
        }

    }

    public void pause(long time) {
        pausedAt = time;
    }

    public void unpause(long time) {
        lastNavRefresh = time - (pausedAt - lastNavRefresh);
        lastSpawn = time - (pausedAt - lastSpawn);

        objects.forEach(e -> e.setLastMovement(time - (pausedAt - e.getLastMovement())));
        objects.forEach(e -> e.setLastPathUpdate(time - (pausedAt - e.getLastPathUpdate())));
    }
}
