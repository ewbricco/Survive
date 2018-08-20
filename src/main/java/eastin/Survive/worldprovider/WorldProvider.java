package eastin.Survive.worldprovider;

import eastin.Survive.World;

import java.io.Serializable;

/**
 * Created by ebricco on 8/17/18.
 */
public interface WorldProvider {
    WorldProvider worldProvider = null;

    static WorldProvider getWorldProvider() {
        return worldProvider;
    }


    World getWorld();
}
