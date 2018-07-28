package eastin.Survive.managers;

import eastin.Survive.GameState;
import eastin.Survive.objects.Enemy;
import eastin.Survive.Runner;
import eastin.Survive.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eastin.Survive.Runner;

/**
 * Created by ebricco on 12/2/16.
 */
public class Enemies {

    private List<Enemy> objects;

    private long lastMovement;
    private long lastSpawn;
    final long TIMEBETWEENMOVEMENTS = 25;
    final long TIMEBETWEENSPAWNS = 100;

    private final int SIZE = 60;
    private final Color COLOR = new Color(1,0,0);

    private final Coordinate STARTINGPOSITION = new Coordinate(250, 450);

    public Enemies(){
        objects = new ArrayList<>();
        lastMovement = System.nanoTime() / 1000000;
        init();
        lastSpawn = 0;
    }

    public void render(){
        objects.stream().forEach(object -> {
            object.checkPositionAndRender();
        });
    }

    private void init(){
        objects.add(new Enemy(STARTINGPOSITION.getX(), STARTINGPOSITION.getX() + SIZE, STARTINGPOSITION.getY() + SIZE, STARTINGPOSITION.getY(), COLOR));
    }

    //seeks target
    public void move(Coordinate target) {
        objects.removeIf(enemy -> enemy.toDespawn);
        objects.forEach(object -> {
            object.update(target, Arrays.asList(Runner.mc));
        });
    }

    private void createNewEnemy() {
        System.out.println("creating new Enemy");

        Coordinate c = Runner.mc.getCenter();

        System.out.println("mc center: " + Runner.mc.getCenter().toString());

        int xDiff = GameState.RAND.nextInt(1000) + 250;
        if(System.currentTimeMillis() %2 == 0) {
            xDiff *= -1;
        }
        int yDiff = GameState.RAND.nextInt(1000) + 250;
        if(xDiff %2 == 0) {
            yDiff *= -1;
        }

        System.out.println("xDiff: "  + xDiff);
        System.out.println("yDiff: "  + yDiff);

        objects.add(new Enemy(c.getX() + xDiff, c.getX() + xDiff + SIZE, c.getY() + yDiff + SIZE, c.getY() + yDiff, COLOR));
    }

    //attempts to reach target
    public void update(Coordinate target){
        if(System.nanoTime() / 1000000 - lastMovement > TIMEBETWEENMOVEMENTS){
            move(target);
            lastMovement = System.nanoTime() / 1000000;
        }

        if(System.nanoTime() / 1000000 - lastSpawn > TIMEBETWEENSPAWNS){
            createNewEnemy();
            lastSpawn = System.nanoTime() / 1000000;
        }

    }
}
