package utils;

import Dijkstra.Connection;
import Dijkstra.Room;
import Hashmap.Player;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;

public class utils {
    static public JsonReader _getReader(String filename){
        FileReader file = null;

        try{
            file = new FileReader(filename);
        }catch(Exception e){
            System.out.println("<"+filename+">: File not found");

            return null;
        }

        return new JsonReader(file);
    }

    static public Player[] playersFromJson(String filename){

        JsonReader players_reader = _getReader(filename);
        Player[] players = new Gson().fromJson(players_reader, Player[].class);

        return players;
    }

    static public Room[] roomsFromJson(String filename){
        JsonReader rooms_reader = _getReader(filename);
        Room[] rooms = new Gson().fromJson(rooms_reader, Room[].class);

        return rooms;
    }

    static public Connection[] connectionsFromJson(String filename) {
        JsonReader connections_reader = _getReader(filename);
        Connection[] connections = new Gson().fromJson(connections_reader, Connection[].class);

        return connections;
    }

}

