package Dijkstra;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Dijkstra {

    private static int getNextMin(List<Connection> connections, ArrayList<SPNode> graph){
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index = -1;

        for(Connection connection : connections){
            if(connection.getEnemyProbability() < min) {
                min = connection.getEnemyProbability();
                min_index = getFreeNode(connection, graph);
            }
        }

        return min_index;
    }

    private static Connection getNextMinConnection(List<Connection> connections, ArrayList<SPNode> graph){
        // Initialize min value
        int min = Integer.MAX_VALUE;
        Connection minConnection = null;
        for(Connection connection : connections){
            if(connection.getEnemyProbability() < min && getFreeNode(connection, graph) >= 0) {
                min = connection.getEnemyProbability();
                minConnection = connection;
            }
        }

        return minConnection;
    }

    private static int getFreeNode(Connection connection, ArrayList<SPNode> graph){
        int min_index = -1;
        for (int idRoom : connection.getRoomConnected())
            if (!graph.get(idRoom).getInUse())
                min_index = idRoom;

        return min_index;
    }

    private static float getDistance(float from, int to)
    {
        return from + (100-from) * (float)to/100.0f;
    }

    private static Path buildPath(ArrayList<SPNode> graph, int end){
        Path path = new Path(graph.get(end).getWeight());
        SPNode nextNode = graph.get(end);

        // add elements to the first position
        do {
            path.getPath().add(0, nextNode);
            nextNode = nextNode.getPrevRoom();
        }while(nextNode != null);

        return path;
    };

    static public Path getShortestPath(ArrayList<SPNode> graph, int init, int end){
        Boolean endReached = false;
        float finalDistance = Float.MAX_VALUE;

        // Set initial node
        SPNode currentNode = graph.get(init);
        currentNode.setInUse(true);
        currentNode.setWeight(0);
        // Queue to store the successors nodes indexes
        ArrayList<Integer> queue = new ArrayList<Integer>();
        queue.add(currentNode.getRoom().getId());

        while(!endReached && !queue.isEmpty()) {
            // Pop the first element of the queue
            currentNode = graph.get(queue.get(0));
            queue.remove(0);

            int currIndex;
            Connection currConnection;

            // Check every neighbour from current node:
            while((currConnection = getNextMinConnection(currentNode.getConnectionList(), graph)) != null) {
                int freeNodeIndex;
                while( (freeNodeIndex = getFreeNode(currConnection, graph)) >= 0){
                    // Mark it and push it into the queue
                    graph.get(freeNodeIndex).setInUse(true);
                    queue.add(freeNodeIndex);
                    // Calculate distance and compare it with the previous one
                    float currDistance = getDistance(currentNode.getWeight(), currConnection.getEnemyProbability());
                    if (graph.get(freeNodeIndex).getWeight() > currDistance) {
                        graph.get(freeNodeIndex).setWeight(currDistance);
                        graph.get(freeNodeIndex).setPrevRoom(currentNode);
                    }
                    if(graph.get(freeNodeIndex).getRoom().getId() == end)
                        endReached = true;
                }
            }
        }
        if(endReached)
            return buildPath(graph, end);

        return null;
    }
}


