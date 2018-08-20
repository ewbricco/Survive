package eastin.Survive.objects;

import eastin.Survive.utils.Node;

import java.io.Serializable;

/**
 * Created by ebricco on 8/9/18.
 *
 * s to t directional edge of graph
 */
public class Edge extends Line implements Serializable {
    Node s;
    Node t;

    //used for despawning edges after nodes of a network vanish
    private boolean temporary;

    public Edge(Node s, Node t) {
        super(s, t);

        this.s = s;
        this.t = t;

        temporary = false;
    }

    @Override
    public String toString() {
        return "edge with " + s.toString() + " and " + t.toString();
    }

    public Node getNext() {
        return t;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public Edge setTemporary(boolean temporary) {
        this.temporary = temporary;

        return this;
    }

}
