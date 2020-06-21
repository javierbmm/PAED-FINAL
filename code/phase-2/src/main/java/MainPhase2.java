import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.util.ArrayList;
import java.util.Arrays;

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

    }
}
