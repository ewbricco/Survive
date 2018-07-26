package eastin.Survive;

import eastin.Survive.Utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ebricco on 12/2/16.
 */
public class Enemies {
    private List<RectangularObject> objects;
    private long lastMovement;

    final long TIMEBETWEENMOVEMENTS = 100;
    final int MOVEMENTDISTANCE = 5;
    private final Color COLOR = new Color(1,0,0);
    private final int SIZE = 30;

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
        objects.add(new RectangularObject(new GameCoordinate(300,500), SIZE, COLOR));
    }

    //seeks target
    public void move(Coordinate target) {
        objects.forEach(object -> {
            object.getCoord().seek(target, MOVEMENTDISTANCE);
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
