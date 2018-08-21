package eastin.Survive.manager;

import eastin.Survive.GameState;
import eastin.Survive.World;
import eastin.Survive.objects.Enemy;
import eastin.Survive.objects.RectangularObject;
import eastin.Survive.utils.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ebricco on 12/2/16.
 */
public class Enemies implements Manager, Serializable {

    public List<Enemy> objects;

    private NavMesh nav;

    private long lastSpawn;

    private final int TIMEBETWEENSPAWNS = 3000;
    public final int SIZE = 60;
    private final Color COLOR = new Color(1,0,0);
    private final long TIMEBETWEENNAVREFRESH = 1000;
    private long lastNavRefresh;
    private long pausedAt;

    private static final Coordinate STARTINGPOSITION = new Coordinate(250, 450);

    public Enemies(){
        objects = new ArrayList<>();
        //objects.add(new Enemy(STARTINGPOSITION.getX(), STARTINGPOSITION.getX() + SIZE, STARTINGPOSITION.getY() + SIZE, STARTINGPOSITION.getY(), COLOR));
        lastSpawn = 0;
        lastNavRefresh = 0;
    }

    public List<Enemy> getObjects() {
        return objects;
    }

    public void render(){
        objects.stream().forEach(object -> {
            object.checkPositionAndRender();
        });

        if(World.world.renderNav && nav != null) {
            nav.render();
        }
    }

    //seeks target
    public void move(RectangularObject target) {
        objects.removeIf(enemy -> enemy.toDespawn);
        /*List<RectangularObject> interactables = new ArrayList<RectangularObject>();
        interactables.addAll(World.world.getWorld().barriers.getObjects());
        interactables.add(World.world.mc);*/
        objects.forEach(object -> {
            object.update(target, Arrays.asList(World.world.mc), nav); //doesn't need to interact with barriers if routing is working correctly
        });
    }

    private void createNewEnemy() {
        //System.out.println("creating new Enemy");

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

        //System.out.println("xDiff: "  + xDiff);
        //System.out.println("yDiff: "  + yDiff);

        addEnemy(c);
    }

    public void addEnemy(Coordinate c) {
        objects.add(new Enemy(c.getX(), c.getX() + SIZE, c.getY() + SIZE, c.getY(), COLOR));
    }

    //attempts to reach target
    public void update(){

        if(System.currentTimeMillis() - lastNavRefresh > TIMEBETWEENNAVREFRESH && objects.size() > 0) {
            int buffer = (int)TIMEBETWEENNAVREFRESH/1000 * MainCharacter.SPEED;
            RectangularObject navArea = World.world.mc.getScreen();
            navArea.expandInAllDirections(buffer);
            nav = new NavMesh(new RectangularObject(0, SIZE, SIZE, 0), World.world.barriers.getBarriersInRect(navArea));
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
