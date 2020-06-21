package test;

import Hashmap.*;

import java.util.Scanner;

public class testHashmap {
    public static void main(String[] args) {
        Player[] players = utils.utils.playersFromJson(args[2]);
        if(players == null)
            return;

        Hashmap table = new Hashmap(5000);
        for(Player player : players)
            table.addPlayer(player);

        System.out.println("finished");
        System.out.println("Number of buckets in use: " + table.getBucketsInUse() + " out of " + table.getNumBuckets());

        // Inputs
        Scanner in = new Scanner(System.in);
        String input;
        while(true){
            System.out.println("\n1- Type a player name to search or 'exit' to stop the execution");
            input = in.nextLine();
            if(input.equals("exit"))
                break;

            Pair pair = table.getPair(input);
            if(pair == null)
                System.out.println("ERROR 404: This player doesn't exists. Please try again with another name\n");
            else
                System.out.println(pair.getPlayer().toString());
        }
    }
}
