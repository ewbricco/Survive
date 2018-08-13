package eastin.Survive;

import eastin.Survive.managers.Barriers;
import eastin.Survive.managers.Enemies;
import eastin.Survive.objects.MainCharacter;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ebricco on 8/13/18.
 *
 * represents all ingame objects
 */
public class World implements Serializable {
    static final long serialVersionUID=1L;

    protected static MainCharacter mc;
    protected static Barriers barriers;
    protected static Enemies enemies;

    public World() {
        mc = new MainCharacter();
        barriers = new Barriers();
        enemies = new Enemies();
    }

    public void handleInput(int key, int action) {
        mc.checkInput(key, action);
    }


    public void update() {
        barriers.update(mc.getCenter());
        enemies.update(mc.getCenter());
        mc.update();
    }

    public void render() {
        barriers.render();
        enemies.render();
        mc.render();
    }


    protected String saveToFile() {

        Date date = new Date();

        Calendar cal = Calendar.getInstance();

        String fileName = Integer.toString(cal.get(Calendar.YEAR)) + Integer.toString((cal.get(Calendar.MONTH) + 1)) + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + Integer.toString(cal.get(Calendar.MINUTE)) + ".world";

        try
        {
            FileOutputStream file = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(this);

            out.close();
            file.close();

        }

        catch(IOException ex)
        {
            System.out.println("problem saving world");
        }

        return fileName;
    }

    protected static World recreateFromFile(String filename) {

        World world = null;

        try
        {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            world = (World)in.readObject();

            in.close();
            file.close();
        }

        catch(IOException ex)
        {
            System.out.println(ex.toString());
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println(ex.toString());
        }

        return world;
    }
}
