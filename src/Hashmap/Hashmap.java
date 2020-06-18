package Hashmap;

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

    public int addPlayer(Player player){

    }

    private Pair getLastInBucket(){
        
    }
}


