package eastin.Survive.manager;

import eastin.Survive.GameState;
import eastin.Survive.objects.Enemy;
import eastin.Survive.objects.RectangularObject;
import eastin.Survive.utils.*;
import eastin.Survive.worldprovider.WorldProvider;
import eastin.Survive.worldprovider.WorldProviderFactory;

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
    private final long TIMEBETWEENNAVREFRESH = 5000;
    private long lastNavRefresh;
    private long pausedAt;

    private final Coordinate STARTINGPOSITION = new Coordinate(250, 450);

    protected WorldProvider worldProvider = WorldProviderFactory.getWorldProvider();

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
    }

    //seeks target
    public void move(Coordinate target) {
        objects.removeIf(enemy -> enemy.toDespawn);
        objects.forEach(object -> {
            object.update(target, Arrays.asList(worldProvider.getWorld().mc), nav);
        });
    }

    private void createNewEnemy() {
        //System.out.println("creating new Enemy");

        Coordinate c = worldProvider.getWorld().mc.getCenter();

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
            nav = new NavMesh(new RectangularObject(0, SIZE, SIZE, 0), worldProvider.getWorld().barriers.getObjects());
            lastNavRefresh = System.currentTimeMillis();
        }

        move(worldProvider.getWorld().mc.getCenter());


        if(System.nanoTime() / 1000000 - lastSpawn > TIMEBETWEENSPAWNS && worldProvider.getWorld().spawning) {
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
