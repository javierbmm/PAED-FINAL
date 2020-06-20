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

    public Pair[] getTable() {
        return table;
    }

    public int getNumBuckets() {
        return numBuckets;
    }

    public int getBucketsInUse() {
        return bucketsInUse;
    }

    private int _hashf(String id){
        long hash = 7;
        int length = id.length();
        for(int i=0; i<length; i++) {
            hash = hash * 31 + id.charAt(i);
            hash %= Integer.MAX_VALUE;
        }

        int result = (int)hash%numBuckets;
        return (int)result;
    }

    public Pair getPair(String playerName){
        Pair pair;
        int id = _hashf(playerName);
        if(_emptyBucket(id))
            return null;

        pair = table[id];
        while(!pair.getPlayer().getName().equals(playerName)) {
            pair = pair.getNext();
            if(pair == null || pair.getPlayer() == null)
                return null;
        }

        return pair;
    }

    public int addPlayer(Player player){
        if(player == null)
            return -1;

        int key = _hashf(player.getName());
        Pair pair = new Pair(player, key);

        if(_emptyBucket(key)) {
            table[key] = pair;
            bucketsInUse++;
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


