package test;
import Dijkstra.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.xml.crypto.Data;
import java.io.FileReader;
import java.util.List;

public class testJsonRead {

    public static void main(String[] args) {
        // ROOMS:
        Room[] rooms = utils.roomsFromJson(args[1]);
        if(rooms == null)
            return;

        for(Room aux : rooms)
            System.out.println(aux.toString());

        // CONNECTIONS:
        Connection[] connections = utils.connectionsFromJson(args[0]);
        if(connections == null)
            return;

        for(Connection aux : connections)
            System.out.println(aux.toString());
    }
}
