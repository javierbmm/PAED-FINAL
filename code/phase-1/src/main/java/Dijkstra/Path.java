package Dijkstra;

import java.util.ArrayList;

public class Path {
    private ArrayList<SPNode> path;
    private float distance;

    public Path() {
        this.path = new ArrayList<SPNode>();
        this.distance = Integer.MAX_VALUE;
    }

    public Path(float distance) {
        this.path = new ArrayList<SPNode>();
        this.distance = distance;
    }

    public ArrayList<SPNode> getPath() {
        return path;
    }

    public void setPath(ArrayList<SPNode> path) {
        this.path = path;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        String toString = "Probability: " + distance + "\n";

        for(SPNode node : this.path)
            toString += node.getRoom().getRoom_name() + " -> ";

        toString = toString.substring(0, toString.length() - 3);

        return toString;
    }
}
