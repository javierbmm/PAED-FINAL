package Hashmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Pair {
    private Player player;
    private int key;
    private Player next;

    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Pair(Player player, int key) {
        this.player = player;
        this.key = key;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Player getNext() {
        return next;
    }

    public void setNext(Player next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
