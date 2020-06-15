package Dijkstra;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Connection {
    private int id;
    private String connection_name;
    private List<Integer> room_connected;
    private int enemy_probability;
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Connection(int id, String connection_name, List<Integer> room_connected, int enemy_probability) {
        this.id = id;
        this.connection_name = connection_name;
        this.room_connected = room_connected;
        this.enemy_probability = enemy_probability;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConnectionName() {
        return connection_name;
    }

    public void setConnectionName(String connection_name) {
        this.connection_name = connection_name;
    }

    public List<Integer> getRoomConnected() {
        return room_connected;
    }

    public void setRoomConnected(List<Integer> room_connected) {
        this.room_connected = room_connected;
    }

    public int getEnemyProbability() {
        return enemy_probability;
    }

    public void setEnemyProbability(int enemy_probability) {
        this.enemy_probability = enemy_probability;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}




