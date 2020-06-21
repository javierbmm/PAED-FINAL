import java.util.*;

public class RTree<K> {
    private static final int n_max_entries = 3;
    private static final int n_min_entries = 1;

    private RTreeNode<K> root;


    public RTree() {
        root = initRoot(true);
    }

    public RTreeNode<K> getRoot() {
        return root;
    }

    private RTreeNode<K> initRoot(boolean is_leaf) {
        int[] coordinates = new int[2];
        int[] size = new int[2];
        coordinates[0] = (int) Math.sqrt(Integer.MAX_VALUE);
        size[0] = -2 * (int) Math.sqrt(Integer.MAX_VALUE);
        coordinates[1] = (int) Math.sqrt(Integer.MAX_VALUE);
        size[1] = -2 * (int) Math.sqrt(Integer.MAX_VALUE);

        return new RTreeNode<>(coordinates, size, is_leaf, null);
    }

    public List<K> search(int x, int y) {
        LinkedList<K> results = new LinkedList<>();
        _search(new int[]{x, y}, new int[]{1, 1}, root, results);
        return results;
    }

    private void _search(int[] coords, int[] dimensions, RTreeNode<K> root, LinkedList<K> results) {
        if (root.isLeaf()) {
            for (RTreeNode<K> node : root.getChildren()) {
                if (overlap(coords, dimensions, node.getCoordinates(), node.getSize()))
                    results.add(node.getObject());
            }
        } else {
            for (RTreeNode<K> node : root.getChildren()) {
                if (overlap(coords, dimensions, node.getCoordinates(), node.getSize()))
                    _search(coords, dimensions, node, results);
            }
        }
    }

    public boolean delete(int x, int y) {
        return _delete(new int[]{x, y}, new int[]{1, 1}, root);
    }


    private boolean _delete(int[] coords, int[] dimensions, RTreeNode<K> node) {
        boolean found = false;
        if (node.isLeaf()) {
            for (int i = 0; i < node.getChildren().size(); i++) {
                RTreeNode<K> child = node.getChildren().get(i);
                if (overlap(coords, dimensions, child.getCoordinates(), child.getSize())) {
                    node.getChildren().remove(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < node.getChildren().size() && !found; i++) {
                RTreeNode<K> child = node.getChildren().get(i);
                if (overlap(coords, dimensions, child.getCoordinates(), child.getSize()))
                    found = _delete(coords, dimensions, child);
            }
        }

        return found;
    }

    public void insert(int x1, int y2, int[] size, K rectangle) {
        int[] coordinates = new int[2];
        coordinates[0] = x1;
        coordinates[1] = y2;

        RTreeNode<K> entry = new RTreeNode<>(coordinates, size, true, rectangle);
        RTreeNode<K> leaf = chooseLeaf(root, entry);
        leaf.getChildren().add(entry);
        entry.parent = leaf;

        if (leaf.getChildren().size() > n_max_entries) {
            ArrayList<RTreeNode<K>> splits = splitNode(leaf);
            adjustTree(splits.get(0), splits.get(1));
        } else {
            adjustTree(leaf, null);
        }
    }

    private boolean overlap(int[] target_coordinates, int[] target_size, int[] coordinates, int[] size) {
        boolean overlapX = false, overlapY = false;
        if (target_coordinates[0] == coordinates[0])
            overlapX = true;
        else if (target_coordinates[0] < coordinates[0]) {
            if (target_coordinates[0] + target_size[0] >= coordinates[0])
                overlapX = true;
        } else {
            if (coordinates[0] + size[0] >= target_coordinates[0])
                overlapX = true;
        }
        if (!overlapX)
            return false;

        if (target_coordinates[1] == coordinates[1])
            overlapY = true;
        else if (target_coordinates[1] < coordinates[1]) {
            if (target_coordinates[1] + target_size[1] >= coordinates[1])
                overlapY = true;
        } else {
            if (coordinates[1] + size[1] >= target_coordinates[1])
                overlapY = true;
        }
        if (!overlapY)
            return false;

        return true;
    }

    private void adjustTree(RTreeNode<K> node1, RTreeNode<K> node2) {
        if (node1 == root) {
            if (node2 != null) {
                root = initRoot(false);
                root.getChildren().add(node1);
                node1.parent = root;
                root.getChildren().add(node2);
                node2.parent = root;
            }
            tighten(root);
            return;
        }
        tighten(node1);
        if (node2 != null) {
            tighten(node2);
            if (node1.parent.getChildren().size() > n_max_entries) {
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
            if ((nodes.get(0).getChildren().size() >= n_min_entries) && (nodes.get(1).getChildren().size() + children.size() == n_min_entries)) {
                nodes.get(1).getChildren().addAll(children);
                children.clear();
                return nodes;
            } else if ((nodes.get(1).getChildren().size() >= n_min_entries) && (nodes.get(1).getChildren().size() + children.size() == n_min_entries)) {
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
                if (node.getCoordinates()[i] < dimension_lower_bound)
                    dimension_lower_bound = node.getCoordinates()[i];
                if (node.getSize()[i] + node.getCoordinates()[i] > dimension_upper_bound)
                    dimension_upper_bound = node.getSize()[i] + node.getCoordinates()[i];
                if (node.getCoordinates()[i] > dimension_max_lower_bound) {
                    dimension_max_lower_bound = node.getCoordinates()[i];
                    max_lower_bound = node;
                }
                if (node.getSize()[i] + node.getCoordinates()[i] < dimension_min_upper_bound) {
                    dimension_min_upper_bound = node.getSize()[i] + node.getCoordinates()[i];
                    min_upper_bound = node;
                }
            }
            int pair = Math.abs((dimension_min_upper_bound - dimension_max_lower_bound) / (dimension_upper_bound - dimension_lower_bound));
            if (pair >= best_pair_value) {
                best_pairs.add(max_lower_bound);
                best_pairs.add(min_upper_bound);
                best_pair_value = pair;
            }
        }
        nodes.remove(best_pairs.get(0));
        nodes.remove(best_pairs.get(1));
        return best_pairs;
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
        int expanded = 1;
        int[] deltas = new int[2];
        for (int i = 0; i < 2; i++) {
            if (coordinates[i] + size[i] < node.getCoordinates()[i] + node.getSize()[i])
                deltas[i] = node.getCoordinates()[i] + node.getSize()[i] - coordinates[i] - size[i];
            else if (coordinates[i] + size[i] > node.getCoordinates()[i] + node.getSize()[i])
                deltas[i] = coordinates[i] - node.getCoordinates()[i];
        }
        for (int i = 0; i < 2; i++)
            area *= size[i] + deltas[i];

        return expanded - area;
    }

    private void tighten(RTreeNode<K> node) {
        int[] min_coordinates = new int[node.getCoordinates().length];
        int[] max_size = new int[node.getSize().length];

        for (int i = 0; i < min_coordinates.length; i++) {
            min_coordinates[i] = Integer.MAX_VALUE;
            max_size[i] = 0;

            for (RTreeNode<K> child : node.getChildren()) {
                child.parent = node;
                if (child.getCoordinates()[i] < min_coordinates[i]) {
                    min_coordinates[i] = child.getCoordinates()[i];
                }
                if ((child.getCoordinates()[i] + child.getSize()[i]) > max_size[i]) {
                    max_size[i] = (child.getCoordinates()[i] + child.getSize()[i]);
                }
            }
        }

        System.arraycopy(min_coordinates, 0, node.getCoordinates(), 0, min_coordinates.length);
        System.arraycopy(max_size, 0, node.getSize(), 0, max_size.length);
    }

    @Override
    public String toString() {
        return "RTree{" +
                "root=" + root +
                '}';
    }
}