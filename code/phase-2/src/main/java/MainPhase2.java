import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainPhase2 {
    public static void main(String[] args) {
        Gson gson = new Gson();


        //read shop items
        JsonReader itemReader = utils._getReader("/home/gvozden/projects/PAED-FINAL/datasets/dataset_ObjectS.json");
        assert itemReader != null;
        Item[] items = gson.fromJson(itemReader, Item[].class);
        System.out.println(Arrays.toString(items));

        JsonReader mapObjectReader = utils._getReader("/home/gvozden/projects/PAED-FINAL/code/phase-2/src/main/resources/dataset_MapS.json");
        assert mapObjectReader != null;

        MapObject[] mapObjects = gson.fromJson(mapObjectReader, MapObject[].class);
        RTree<MapObject> map = new RTree<>();

        for(MapObject mapObject : mapObjects) {
            map.insert(
                    mapObject.getX1(),
                    mapObject.getY2(),
                    mapObject.getSize(),
                    mapObject);
        }

        System.out.println(map);
        //create shop
        AVLTree<Item> shop = new AVLTree<>();

        //fill shop
        for(Item i : items) {
            shop.insert(i);
        }

        //create purchase view
        ConsoleFormView view = new ConsoleFormView();

        ShopController controller = new ShopController(shop, view);
        controller.execute();


        String inputBuffer = view.getQueryResponse("What do you wanna do? ");
        while(!"exit".equalsIgnoreCase(inputBuffer)) {
            switch (inputBuffer) {
                case "1":
                    int x = Integer.parseInt(view.getQueryResponse("Enter X coordinate: "));
                    int y = Integer.parseInt(view.getQueryResponse("Enter Y coordinate: "));

                    List<MapObject> mapObjectsNear = map.search(x, y);

                    if(mapObjectsNear.size() > 0)
                        System.out.println("Hit! Item with ID " + mapObjectsNear + " found at those coordinates!");
                    else
                        System.out.println("Miss, no item found at those coordinates...");

                    break;
                case "2":
                    break;
                default:
                    view.displayError("Unknown option1");
            }
            inputBuffer = view.getQueryResponse("What do you wanna do? ");
        }
    }
}
