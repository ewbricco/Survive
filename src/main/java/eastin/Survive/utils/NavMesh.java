package eastin.Survive.utils;

import eastin.Survive.World;
import eastin.Survive.objects.Edge;
import eastin.Survive.objects.Line;
import eastin.Survive.objects.Rectangle;

import java.io.Serializable;
import java.util.*;

import static org.lwjgl.opengl.GL11.glLineWidth;

/**
 * Created by ebricco on 8/7/18.
 *
 * represents graph of movement possibilities
 *
 * probably doesn't have to be totally recalculated for stationary objects
 *
 */
public class NavMesh implements Serializable {

    //rects where the bottom left of mover can't be
    private List<Rectangle> constraints;

    private List<Node> nodes;

    //target coordinate should be bottom left of its rect
    //maybe shouldn't include s and t in constructor to reuse nav mesh aslong as obstancles don't change
    public NavMesh(Rectangle mover, List<? extends Rectangle> obstacles) {

        constraints = new ArrayList<>();
        nodes = new ArrayList<>();

        for(Rectangle r:obstacles) {

            Rectangle constraint = new Rectangle(r.getLeftBound() - mover.getWidth() + 1, r.getRightBound(), r.getUpperBound(), r.getLowerBound() - mover.getHeight() + 1);
            constraints.add(constraint);

            int temp = 0;

            //bottom left
            nodes.add(new Node(constraint.getBottomLeft().addX(-1-temp).addY(-1-temp)));

            //top left
            nodes.add(new Node(constraint.getTopLeft().addX(-1-temp).addY(1+temp)));

            //bottom right
            nodes.add(new Node(constraint.getBottomRight().addX(1+temp).addY(-1-temp)));

            //top right
            nodes.add(new Node(constraint.getTopRight().addX(1+temp).addY(1+temp)));
        }

        //int count = 0;

        //System.out.println(nodes.size());
        //System.out.println(constraints.size());

        //add unobstructed edges
        //for every possible combination of nodes
        for(Node m: nodes) {

            List<Edge> edges = new ArrayList<>();

            for(Node n: nodes) {
                if(!m.equals(n)) {
                    //construct edge
                    //TODO stop separate lines from being checked for n1 -> n2 and n2 -> n1
                    Edge edge = new Edge(m, n);
                    boolean overlaps = false;
                    for(Rectangle r:constraints) {
                        if(edge.overlapsWith(r)) {
                            overlaps = true;
                        }
                    }

                    if(!overlaps) {
                        edges.add(edge);
                        //System.out.println(edge.toString());
                        //count++;
                    }

                }
            }

            m.addEdges(edges);
        }

    }

    public void addEdgeInBothDirectionsIfPossible(Node u, Node v, boolean temporary) {
        Edge edge = new Edge(u, v);

        edge.setTemporary(temporary);

        boolean overlaps = false;
        for(Rectangle r:constraints) {
            if(edge.overlapsWith(r)) {
                overlaps = true;
            }
        }

        if(!overlaps) {
            if(temporary && World.world.renderNav) {
                glLineWidth(1);
                edge.render(new Color(1,1,1));
                glLineWidth(1);
            }
            u.addEdges(Arrays.asList(edge));

            //add other direction for consistency, eventually remove
            v.addEdges(Arrays.asList(new Edge(v, u).setTemporary(true)));
        }

    }

    public int computeNumberOfEdges() {
        int sum = 0;

        for(Node n:nodes) {
            sum += n.getEdges().size();
        }

        return sum;
    }

    List<Coordinate> getPath(Node s, Node t) {

        //System.out.println("getting path from mesh with edge count: " + computeNumberOfEdges());

        List<Coordinate> path = new ArrayList<>();

        Map<Node, NodeInfo> infoMap = new HashMap<>();

        for(Node m: nodes) {
            addEdgeInBothDirectionsIfPossible(s, m, true);
            addEdgeInBothDirectionsIfPossible(t, m, true);

            infoMap.put(m, new NodeInfo(m));
        }

        addEdgeInBothDirectionsIfPossible(s, t, true);

        //System.out.println("after adding s and t edge count: " + computeNumberOfEdges());

        infoMap.put(s, new NodeInfo(s));
        infoMap.put(t, new NodeInfo(t));


        for(Node n:infoMap.keySet()) {
            //System.out.println("\n" + n.toString());
            //System.out.println("edges: ");

            for(Line e:n.getEdges()) {
                //System.out.println(e.toString());
            }
        }

        //System.out.println("\n\n\n******\n\n\n");

        //begin a*
        Set<NodeInfo> closedSet = new HashSet<>();

        PriorityQueue<NodeInfo> openSet = new PriorityQueue<>();

        NodeInfo startInfo = infoMap.get(s);
        startInfo.setScore(0d);
        startInfo.computeFScore(t);

        openSet.add(startInfo);

        NodeInfo cur = null;

        while(openSet.size() > 0) {

            cur = openSet.remove();

            //System.out.println("\n\n" + cur.toString());

            if(cur.equals(infoMap.get(t))) {
                //System.out.println("at target");
                break;
            }

            closedSet.add(cur);

            //System.out.println("this node has " + cur.node.getEdges().size() + " edges");

            for(Edge e:cur.node.getEdges()) {
                NodeInfo neighbor = infoMap.get(e.getNext());

                if(closedSet.contains(neighbor)) {
                    //System.out.println("closed set contains neighbor: " + neighbor.toString());
                    continue;
                }

                //System.out.println(neighbor.node.toString());

                double tentativeGScore = cur.score + cur.node.distanceTo(neighbor.node);

                if(!openSet.contains(neighbor)) {
                    //System.out.println("adding " + neighbor.toString() + " to openSet");
                    openSet.add(neighbor);
                } else if(tentativeGScore >= neighbor.score) {
                    //System.out.println("not adding " + neighbor + " to open set");
                    continue;
                }

                neighbor.previous = cur;
                neighbor.score = tentativeGScore;
                neighbor.fScore = neighbor.score + neighbor.node.distanceTo(t);
            }

            //System.out.println("open set size: " + openSet.size());

        }

        resetMesh();

        NodeInfo i = cur;
        path.add(i.node);

        //System.out.println("\n\nfinal " + i.toString());
        //System.out.println("previous: " + i.previous.toString());

        while(i.previous != null) {
            path.add(i.previous.node);

            i = i.previous;
        }

        Collections.reverse(path);


        if(!path.get(path.size()-1).equals(t)) {
           //System.out.println("no path found");
            return new ArrayList<Coordinate>();
        }


        return path;
    }

    public void render() {
        for(Node n:nodes) {
            for(Edge e:n.getEdges()) {
                e.render();
            }
        }
    }

    public List<Coordinate> getPath(Rectangle mover, Rectangle target) {
        Coordinate bottomLeft = target.getBottomLeft();

        boolean possible = true;
        for(Rectangle r:constraints) {
            if (bottomLeft.overlapsWith(r)) {
                possible = false;
                break;
            }
        }

        if(possible) {
            return getPath(new Node(mover.getBottomLeft()), new Node(target.getBottomLeft()));
        }

        //else return bottom left target where topRight of mover = topRight of target
        Coordinate adjustedTarget = target.getTopRight();

        adjustedTarget.addX(1-mover.getWidth());
        adjustedTarget.addY(1-mover.getHeight());

        return getPath(new Node(mover.getBottomLeft()), new Node(adjustedTarget));
    }

    public void resetMesh() {
        for(Node n:nodes) {

            List<Line> toRemove = new ArrayList<>();

            for(Edge e: n.getEdges()) {
                if(e.isTemporary()) {
                    toRemove.add(e);
                    continue;
                }
            }

            n.getEdges().removeAll(toRemove);
        }
    }


    public static class NodeInfo implements Comparable {
        Node node;
        NodeInfo previous;
        Double score;
        Double fScore;

        public NodeInfo(Node node) {
            this.node = node;
            score = Double.POSITIVE_INFINITY;

            fScore = Double.POSITIVE_INFINITY;

            previous = null;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public void computeFScore(Node target) {
            fScore = node.distanceTo(target);
        }

        @Override
        public int compareTo(Object n) {
            if(n instanceof NodeInfo) {
                return Double.compare(this.fScore, ((NodeInfo)n).fScore);
            }

            return 0;
        }

        public String toString() {
            return "nodeInfo with " + node.toString() + " gscore: " + score + " fscore: " + fScore;
        }

    }

}
