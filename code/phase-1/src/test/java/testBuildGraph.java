import Dijkstra.*;

import java.util.ArrayList;

public class testBuildGraph {

    public static void main(String[] args) {
        // ROOMS:
        /*Room[] rooms = utils.roomsFromJson(args[1]);
        if(rooms == null)
            return;

        // CONNECTIONS:
        Connection[] connections = utils.connectionsFromJson(args[0]);
        if(connections == null)
            return;

        // Building graph (as an array):
        ArrayList<SPNode> graph = new ArrayList<SPNode>();

        for(Room room : rooms){
            SPNode node = new SPNode(room);

            for(Connection connection : connections){

                for(int idConnectedRoom : connection.getRoomConnected()){
                    // if this connection is connected to MY own room, add it to the SPNode
                    if(idConnectedRoom == room.getId()){
                        node.getConnectionList().add(connection);
                        break;
                    }
                }
            }
            // Add node to the graph
            graph.add(node);
            System.out.println(node.toString());
        }
        Path path = Dijkstra.getShortestPath(graph, 1, 53);
        System.out.println("Finished");
        System.out.println(path.toString());*/
    }

}