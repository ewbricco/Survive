package eastin.Survive.managers;

import eastin.Survive.objects.Enemy;
import eastin.Survive.Runner;
import eastin.Survive.utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ebricco on 12/2/16.
 */
public class Enemies {

    private List<Enemy> objects;

    private long lastMovement;
    final long TIMEBETWEENMOVEMENTS = 100;

    private final int SIZE = 60;
    private final Color COLOR = new Color(1,0,0);

    private final Coordinate STARTINGPOSITION = new Coordinate(250, 450);

    public Enemies(){
        objects = new ArrayList<>();
        lastMovement = System.nanoTime() / 1000000;
        init();
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

    //attempts to reach target
    public void update(Coordinate target){
        if(System.nanoTime() / 1000000 - lastMovement > TIMEBETWEENMOVEMENTS){
            move(target);
            lastMovement = System.nanoTime() / 1000000;
        }
    }
}
