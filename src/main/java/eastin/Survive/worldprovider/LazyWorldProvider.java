package eastin.Survive.worldprovider;

import eastin.Survive.World;
import eastin.Survive.worldprovider.WorldProvider;

/**
 * Created by ebricco on 8/18/18.
 */
public class LazyWorldProvider implements WorldProvider {
    private WorldProvider worldProvider;

    public World getWorld() {
        if(worldProvider == null) {
            throw new IllegalArgumentException("no world provider set");
        }

        return worldProvider.getWorld();
    }

    public void setWorldProvider(WorldProvider wp) {
        worldProvider = wp;
    }
}
