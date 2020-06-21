import java.util.Arrays;
import java.util.LinkedList;

public class RTreeNode<K> {
    private int[] coordinates;
    private int[] size;
    private final LinkedList<RTreeNode<K>> children;
    private final boolean leaf;
    private final K object;

    RTreeNode<K> parent;

    public RTreeNode(int[] coordinates, int[] size, boolean leaf, K object) {
        this.coordinates = new int[coordinates.length];
        this.size = new int[size.length];
        this.coordinates = Arrays.copyOf(coordinates, coordinates.length);
        this.size = Arrays.copyOf(size, size.length);
        this.leaf = leaf;
        this.children = new LinkedList<>();
        this.object = object;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public void setSize(int[] size) {
        this.size = size;
    }

    public K getObject() {
        return object;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public int[] getSize() {
        return size;
    }

    public LinkedList<RTreeNode<K>> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public RTreeNode<K> getParent() {
        return parent;
    }

    public void setParent(RTreeNode<K> parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nRTreeNode{");
        sb.append("coordinates=").append(Arrays.toString(coordinates));
        sb.append(", size=").append(Arrays.toString(size));
        sb.append(", children=").append(children);
        sb.append(",leaf=").append(leaf);
        sb.append(", object=").append(object);
        sb.append('}');
        return sb.toString();
    }
}
