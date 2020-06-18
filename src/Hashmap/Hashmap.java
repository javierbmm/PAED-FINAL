package Hashmap;

import java.util.Objects;

public class Hashmap {
    private Pair[] table;
    private int numBuckets = 1000;
    private int bucketsInUse;

    public Hashmap() {
        table = new Pair[numBuckets];
        bucketsInUse = 0;
    }

    public Hashmap(int numBuckets) {
        this.numBuckets = numBuckets;
        table = new Pair[numBuckets];
    }

    private int _hashf(String id){
        int hash = 7;
        int length = id.length();
        for(int i=0; i<length; i++)
            hash = hash*31 + id.charAt(i);

        return hash/numBuckets;
    }

    public Pair getPair(String playerName){
        Pair pair;
        int id = _hashf(playerName);
        pair = table[id];
        if(pair.getPlayer() == null)
            return null;

        while(!pair.getPlayer().getName().equals(playerName)) {
            pair = pair.getNext();
            if(pair.getPlayer() == null)
                return null;
        }

        return pair;
    }

    public int addPlayer(Player player){
        int key = _hashf(player.getName());
        Pair pair = new Pair(player, key);

        if(_emptyBucket(key)) {
            table[key] = pair;
            return key;
        }

        Objects.requireNonNull(_getLastInBucket(key)).setNext(pair);
        return key;
    }

    private Boolean _emptyBucket(int key){
        return table[key] == null;
    }

    private Pair _getLastInBucket(int key){
        if(_emptyBucket(key))
            return null;

        Pair last = table[key];

        while(last.getNext() != null)
            last = last.getNext();

        return last;
    }
}


