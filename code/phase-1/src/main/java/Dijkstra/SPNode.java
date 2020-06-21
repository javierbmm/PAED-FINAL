package Dijkstra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class SPNode {
    private Room room;
    private List<Connection> connectionList;
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Boolean inUse;
    private float weight;
    private SPNode prevRoom;

    public SPNode(Room room) {
        this.room = room;
        this.connectionList = new ArrayList();
        this.inUse = false;
        this.weight = Float.MAX_VALUE;
        this.prevRoom = null;
    }

    public SPNode getPrevRoom() {
        return prevRoom;
    }

    public void setPrevRoom(SPNode prevRoom) {
        this.prevRoom = prevRoom;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

}
