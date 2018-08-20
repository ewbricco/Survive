package eastin.Survive.objects;

import java.io.Serializable;

/**
 * Created by ebricco on 8/7/18.
 */
public abstract class Entity implements Serializable {

    abstract boolean overlapsWith(Entity e);
}
