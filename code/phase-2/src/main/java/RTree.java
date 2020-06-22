import java.util.*;
import java.util.function.*;

public class RTree<K> {
    private static final int[] BASE_DIMS = {1,1};
    private static final int MAX_ENTRIES = 3;
    private static final int MIN_ENTRIES = 1;
    private static final int[] BASE_COORDS = new int[]{(int) Math.sqrt(Integer.MAX_VALUE),  (int) Math.sqrt(Integer.MAX_VALUE)};
    private static final int[] BASE_SIZE
            = new int[]{-2 * (int) Math.sqrt(Integer.MAX_VALUE), -2 * (int) Math.sqrt(Integer.MAX_VALUE)};

    private RTreeNode<K> root;


    public RTree() {
        this.root = newRoot(true);
    }

    public RTreeNode<K> getRoot() {
        return this.root;
    }

    private RTreeNode<K> newRoot(boolean is_leaf) {
        return new RTreeNode<>(BASE_COORDS, BASE_SIZE, is_leaf, null);
    }

    public List<K> search(int x, int y) {
        LinkedList<K> results = new LinkedList<>();
        _search(new int[]{x, y}, BASE_DIMS, this.root, results);
        return results;
    }

    private void _search(int[] coords, int[] dimensions, RTreeNode<K> root, LinkedList<K> results) {
        Consumer<RTreeNode<K>> overlapAction = root.isLeaf() ?
                (node) -> results.add(node.getObject()) :
                (node) -> _search(coords, dimensions, node, results);

        Predicate<RTreeNode<K>> overlapPredicate =
                (node) -> overlap(coords, dimensions, node.getCoordinates(), node.getSize());

        root
                .getChildren()
                .stream()
                .filter(overlapPredicate)
                .forEachOrdered(overlapAction);
    }

    public boolean delete(int x, int y) {
        return _delete(new int[]{x, y}, BASE_DIMS, this.root);
    }


    private boolean _delete(int[] coords, int[] dimensions, RTreeNode<K> node) {
        boolean found = false;

        if(node.isLeaf()) {
            for (int i = 0; i < node.getChildren().size(); i++) {
                RTreeNode<K> child = node.getChildren().get(i);
                if (overlap(coords, dimensions, child.getCoordinates(), child.getSize())) {
                    node.getChildren().remove(i);
                    return true;
                }
            }
        }
        else {
            for (int i = 0; i < node.getChildren().size() && !found; i++) {
                RTreeNode<K> child = node.getChildren().get(i);
                if (overlap(coords, dimensions, child.getCoordinates(), child.getSize()))
                    found = _delete(coords, dimensions, child);
            }
        }
        return found;
    }

    public void insert(int x1, int y2, int[] size, K object) {
        int[] coordinates = new int[]{x1, y2};
        RTreeNode<K> entry = new RTreeNode<>(coordinates, size, true, object);
        RTreeNode<K> leaf = chooseLeaf(this.root, entry);

        leaf.getChildren().add(entry);
        entry.parent = leaf;

        if (leaf.getChildren().size() > MAX_ENTRIES) {
            ArrayList<RTreeNode<K>> splits = splitNode(leaf);
            this.adjustTree(splits.get(0), splits.get(1));
        } else {
            this.adjustTree(leaf, null);
        }
    }

    private boolean overlapOneDim(int t_coord, int t_size, int coord, int size) {
        return t_coord == coord ?
                t_coord + t_size >= coord :
                coord + size >= t_coord;
    }

    private boolean overlap(int[] t_coords, int[] t_size, int[] coords, int[] size) {
        boolean overlap;
        for(int i = 0; i < t_coords.length; i++) {
            overlap = this.overlapOneDim(t_coords[i], t_size[i], coords[i], size[i]);

            if(!overlap) {
                return false;
            }
        }

        return true;
    }

    private void makeParentChild(RTreeNode<K> parent, RTreeNode<K> child) {
        parent.getChildren().add(child);
        child.parent = parent;
    }

    private void splitRoot(RTreeNode<K> node1, RTreeNode<K> node2) {
        this.root = newRoot(false);
        this.makeParentChild(this.root, node1);
        this.makeParentChild(this.root, node2);
    }

    private void adjustAtRoot(RTreeNode<K> node2) {
        if (null != node2) {
            this.splitRoot(this.root, node2);
        }

        this.tighten(this.root);
    }

    private void adjustTree(RTreeNode<K> node1, RTreeNode<K> node2) {
        if (this.root == node1) {
            this.adjustAtRoot(node2);
            return;
        }

        tighten(node1);

        if (null != node2) {
            tighten(node2);

            if (null != node1.parent && node1.parent.getChildren().size() > MAX_ENTRIES) {
                ArrayList<RTreeNode<K>> splits = splitNode(node1.parent);
                adjustTree(splits.get(0), splits.get(1));
            }
        } else if (node1.parent != null) {
            adjustTree(node1.parent, null);
        }
    }

    private ArrayList<RTreeNode<K>> splitNode(RTreeNode<K> node) {
        ArrayList<RTreeNode<K>> nodes = new ArrayList<>(2);
        nodes.add(0, node);
        nodes.add(1, new RTreeNode<>(node.getCoordinates(), node.getSize(), node.isLeaf(), node.getObject()));
        nodes.get(1).parent = node.parent;
        if (nodes.get(1).parent != null)
            nodes.get(1).parent.getChildren().add(nodes.get(1));
        LinkedList<RTreeNode<K>> children = new LinkedList<>(node.getChildren());
        node.getChildren().clear();

        ArrayList<RTreeNode<K>> seeds = pickSeeds(children);
        nodes.get(0).getChildren().add(seeds.get(0));
        nodes.get(1).getChildren().add(seeds.get(1));


        while (!children.isEmpty()) {
            if ((nodes.get(0).getChildren().size() >= MIN_ENTRIES) && (nodes.get(1).getChildren().size() + children.size() == MIN_ENTRIES)) {
                nodes.get(1).getChildren().addAll(children);
                children.clear();
                return nodes;
            } else if ((nodes.get(1).getChildren().size() >= MIN_ENTRIES) && (nodes.get(1).getChildren().size() + children.size() == MIN_ENTRIES)) {
                nodes.get(0).getChildren().addAll(children);
                children.clear();
                return nodes;
            }
            RTreeNode<K> c = children.pop();
            RTreeNode<K> preferred;
            double e0 = getRequiredExpansion(nodes.get(0).getCoordinates(), nodes.get(0).getSize(), c);
            double e1 = getRequiredExpansion(nodes.get(1).getCoordinates(), nodes.get(1).getSize(), c);
            if (e0 < e1)
                preferred = nodes.get(0);
            else if (e0 > e1)
                preferred = nodes.get(1);
            else {
                int[] size1 = nodes.get(0).getSize();
                int[] size2 = nodes.get(0).getSize();
                int a0 = size1[0] * size1[1];
                int a1 = size2[0] * size2[1];

                if (a0 < a1)
                    preferred = nodes.get(0);
                else if (e0 > a1)
                    preferred = nodes.get(1);
                else {
                    if (nodes.get(0).getChildren().size() < nodes.get(1).getChildren().size()) {
                        preferred = nodes.get(0);
                    } else if (nodes.get(0).getChildren().size() > nodes.get(1).getChildren().size()) {
                        preferred = nodes.get(1);
                    } else {
                        preferred = nodes.get((int) Math.round(Math.random()));
                    }
                }
            }
            preferred.getChildren().add(c);
        }
        tighten(nodes.get(0));
        tighten(nodes.get(1));
        return nodes;
    }

    private ArrayList<RTreeNode<K>> pickSeeds(LinkedList<RTreeNode<K>> nodes) {
        ArrayList<RTreeNode<K>> best_pairs = new ArrayList<>();
        int best_pair_value = 0;
        int dimension_lower_bound, dimension_max_lower_bound, dimension_upper_bound, dimension_min_upper_bound;

        for (int i = 0; i < 2; i++) {
            dimension_lower_bound = Integer.MAX_VALUE;
            dimension_max_lower_bound = -1 * Integer.MAX_VALUE;
            dimension_upper_bound = -1 * Integer.MAX_VALUE;
            dimension_min_upper_bound = Integer.MAX_VALUE;

            RTreeNode<K> max_lower_bound = null, min_upper_bound = null;

            for (RTreeNode<K> node : nodes) {
                dimension_lower_bound = Math.min(node.getCoordinates()[i], dimension_lower_bound);
                dimension_upper_bound = Math.max(node.getSize()[i] + node.getCoordinates()[i], dimension_upper_bound);

                if (node.getCoordinates()[i] > dimension_max_lower_bound) {
                    dimension_max_lower_bound = node.getCoordinates()[i];
                    max_lower_bound = node;
                }

                if (node.getSize()[i] + node.getCoordinates()[i] < dimension_min_upper_bound) {
                    dimension_min_upper_bound = node.getSize()[i] + node.getCoordinates()[i];
                    min_upper_bound = node;
                }
            }
            int pair = this.getPair(
                    dimension_min_upper_bound,
                    dimension_upper_bound,
                    dimension_max_lower_bound,
                    dimension_lower_bound
            );

            if (pair >= best_pair_value) {
                best_pairs.add(max_lower_bound);
                best_pairs.add(min_upper_bound);
                best_pair_value = pair;
            }
        }

        best_pairs.forEach(nodes::remove);
        return best_pairs;
    }

    private int getPair(int d_min_upper_bound, int d_upper_bound, int d_max_lower_bound, int d_lower_bound) {
        return Math.abs((d_min_upper_bound - d_max_lower_bound) / (d_upper_bound - d_lower_bound));
    }

    private RTreeNode<K> chooseLeaf(RTreeNode<K> n, RTreeNode<K> e) {
        int min_delta = Integer.MAX_VALUE;
        RTreeNode<K> next_node = null;
        if (n.isLeaf())
            return n;

        for (RTreeNode<K> child : n.getChildren()) {
            int delta = getRequiredExpansion(child.getCoordinates(), child.getSize(), e);
            if (delta < min_delta) {
                min_delta = delta;
                next_node = child;
            } else if (delta == min_delta) {
                int area1 = 1, area2 = 1;

                for (int i = 0; i < 2; i++) {
                    area1 *= next_node.getSize()[i];
                    area2 *= child.getSize()[i];
                }
                if (area2 < area1)
                    next_node = child;
            }
        }
        return chooseLeaf(next_node, e);
    }

    private int getRequiredExpansion(int[] coordinates, int[] size, RTreeNode<K> node) {
        int area = size[0] * size[1];
        int[] deltas = new int[2];
        for (int i = 0; i < coordinates.length; i++) {
            if (coordinates[i] + size[i] < node.getCoordinates()[i] + node.getSize()[i])
                deltas[i] = node.getCoordinates()[i] + node.getSize()[i] - coordinates[i] - size[i];
            else if (coordinates[i] + size[i] > node.getCoordinates()[i] + node.getSize()[i])
                deltas[i] = coordinates[i] - node.getCoordinates()[i];
        }
        for (int i = 0; i < 2; i++)
            area *= size[i] + deltas[i];

        return 1 - area;
    }

    private void tighten(RTreeNode<K> node) {
        int[] min_coordinates = new int[node.getCoordinates().length];
        int[] max_size = new int[node.getSize().length];

        for (int i = 0; i < min_coordinates.length; i++) {
            min_coordinates[i] = Integer.MAX_VALUE;

            for (RTreeNode<K> child : node.getChildren()) {
                child.parent = node;
                min_coordinates[i] = Math.min(child.getCoordinates()[i], min_coordinates[i]);
                max_size[i] = Math.max((child.getCoordinates()[i] + child.getSize()[i]), max_size[i]);
            }
        }

        node.setCoordinates(min_coordinates);
        node.setSize(max_size);
    }

    @Override
    public String toString() {
        return "RTree{" +
                "root=" + root +
                '}';
    }
}