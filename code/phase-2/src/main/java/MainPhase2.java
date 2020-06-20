import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.util.Arrays;

public class MainPhase2 {
    public static void main(String[] args) {

        //read shop items
        JsonReader itemReader = utils._getReader("/home/gvozden/projects/PAED-FINAL/datasets/dataset_ObjectS.json");
        assert itemReader != null;
        Item[] items = new Gson().fromJson(itemReader, Item[].class);
        System.out.println(Arrays.toString(items));

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
