package Hashmap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Player {
    private String name;
    private int KDA;
    private int Games;

    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Player(String name, int KDA, int games) {
        this.name = name;
        this.KDA = KDA;
        Games = games;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKDA() {
        return KDA;
    }

    public void setKDA(int KDA) {
        this.KDA = KDA;
    }

    public int getGames() {
        return Games;
    }

    public void setGames(int games) {
        Games = games;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }
}
