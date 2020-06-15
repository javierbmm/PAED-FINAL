package Dijkstra;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;

public class utils {

    static public Room[] roomsFromJson(String filename){
        FileReader file = null;
        try{
            file = new FileReader(filename);
        }catch(Exception e){
            System.out.println("File not found");

            return null;
        }

        JsonReader rooms_reader = new JsonReader(file);
        Room[] rooms = new Gson().fromJson(rooms_reader, Room[].class);

        return rooms;
    }

    static public Connection[] connectionsFromJson(String filename) {
        FileReader file = null;

        try{
            file = new FileReader(filename);
        }catch(Exception e){
            System.out.println("File not found");

            return null;
        }

        JsonReader connections_reader = new JsonReader(file);
        Connection[] connections = new Gson().fromJson(connections_reader, Connection[].class);

        return connections;
    }

}
