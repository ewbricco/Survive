package eastin.Survive.worldprovider;

/**
 * Created by ebricco on 8/17/18.
 */
public class WorldProviderFactory {

    public static LazyWorldProvider worldProvider = new LazyWorldProvider();

    public static LazyWorldProvider getWorldProvider() {
        return worldProvider;
    }

}
