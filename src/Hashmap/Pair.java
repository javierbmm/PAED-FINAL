package Hashmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Pair {
    private Player player = null;
    private int key;
    private Pair next = null;

    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Pair(Player player, int key) {
        this.player = player;
        this.key = key;
    }

    public Pair() {
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

    public Pair getNext() {
        return next;
    }

    public void setNext(Pair next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
