import org.jetbrains.annotations.NotNull;

public class Item implements Comparable<Item> {
    private String name;
    private int price;

    public Item(String name, int value) {
        this.name = name;
        this.price = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public int compareTo(@NotNull Item o) {
        return this.price - o.price;
    }
}
