import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.util.Arrays;

public class MainPhase2 {
    public static void main(String[] args) {
        JsonReader itemReader = utils._getReader("/home/gvozden/projects/PAED-FINAL/datasets/dataset_ObjectS.json");
        assert itemReader != null;
        Item[] items = new Gson().fromJson(itemReader, Item[].class);
        System.out.println(Arrays.toString(items));

        AVLTree<Item> shop = new AVLTree<>();

        for(Item i : items) {
            shop.insert(i);
        }

        System.out.println(shop);
    }
}
