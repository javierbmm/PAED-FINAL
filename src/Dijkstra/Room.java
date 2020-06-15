package Dijkstra;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Room {
    private int id;
    private String room_name;

    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // methods
    public Room(int id, String room_name) {
        this.id = id;
        this.room_name = room_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
