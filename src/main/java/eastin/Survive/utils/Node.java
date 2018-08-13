package eastin.Survive.utils;

import eastin.Survive.objects.Edge;
import eastin.Survive.objects.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ebricco on 8/7/18.
 */
public class Node extends Coordinate {
    private List<Edge> edges;

    public Node(Coordinate c) {
        super(c.getX(), c.getY());
        edges = new ArrayList<>();
    }

    public Node(int x, int y) {
        super(x, y);
        edges = new ArrayList<>();
    }

    public void addEdges(List<Edge> edges) {
        this.edges.addAll(edges);
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
