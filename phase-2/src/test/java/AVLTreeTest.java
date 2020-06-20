import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class AVLTreeTest {
    private final AVLTree<Integer> tree = new AVLTree<>();
    /**
     * Insert a variable number of integer objects into the tree
     * @param integers Integers to insert
     */
    private void insertValues(Integer...integers) {
        for(Integer i : integers) {
            this.tree.insert(i);
        }
    }

    /**
     * Recursively check fi the AVL tree passed is balanced
     * @param current Current root node
     * @return Whether the tree rooted at current is balanced
     */
    private boolean isBalanced(AVLTreeNode<Integer> current) {
        boolean rightBalance = true;
        boolean leftBalance = true;
        int leftHeight = 0;
        int rightHeight = 0;

        if(null != current.getRightChild()) {
            //recursively get balance for right subtree
            rightBalance = this.isBalanced(current.getRightChild());

            //recursively get the depth of the right subtree
            rightHeight = getDepth(current.getRightChild());
        }

        if(null != current.getLeftChild()) {
            //recursively get balance for left subtree
            leftBalance = this.isBalanced(current.getLeftChild());

            //recursively get the depth of the left subtree
            leftHeight = getDepth(current.getLeftChild());
        }

        //check that both left and right subtree are balanced and that node height
        //difference is 1 or less
        return leftBalance && rightBalance && Math.abs(leftHeight - rightHeight) < 2;
    }

    /**
     * Recursively calculate the depth of the AVL tree rooted
     * at the node past
     * @param node The root of the AVL tree
     * @return The depth of the AVL tree rooted at the node past
     */
    private int getDepth(AVLTreeNode<Integer> node) {
        int leftHeight = 0;
        int rightHeight = 0;

        if(null != node.getRightChild()) {
            rightHeight = this.getDepth(node.getRightChild());
        }

        if(null != node.getLeftChild()) {
            leftHeight = this.getDepth(node.getLeftChild());
        }

        return 1 + Math.max(rightHeight, leftHeight);
    }

    private boolean isWellOrdered(AVLTreeNode<Integer> current) {

        //if we have a left subtree check if its ordered
        if(null != current.getLeftChild()) {
            if(0 < current.getLeftChild().getData().compareTo(current.getData())) {
                return false;
            }
            else {
                //current element is well ordered proceed recursively to the left
                return this.isWellOrdered(current.getLeftChild());
            }
        } else if(null != current.getRightChild()) {
            if(0 > current.getRightChild().getData().compareTo(current.getData())) {
                return false;
            }
            else {
                //current element is ordered well so proceed recursively to the right
                return this.isWellOrdered(current.getRightChild());
            }
        } else if(null == current.getLeftChild() && null == current.getRightChild()) {
            //current has no children so its well balanced
            return true;
        }

        return true;
    }

    @Test
    void insert() {
        assertTrue(this.tree.isEmpty());
        /**16,24,36,19,44,28,61,74,83,64,52,65,86,93,88**/
        this.insertValues(16,24,36,19,44,28,61,74,83,64);

        assertTrue(this.isBalanced(this.tree.getRoot()));
        assertTrue(this.isWellOrdered(this.tree.getRoot()));

        //check that tree is well balanced after removing node
        this.tree.delete(88);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));

        //check tree is well balanced after removing node and
        //that it does not contain the node we removed
        this.tree.delete(19);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));
        assertFalse(this.tree.contains(19));

        this.tree.delete(16);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));
        assertFalse(this.tree.contains(16));

        this.tree.delete(28);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));
        assertFalse(this.tree.contains(28));

        this.tree.delete(24);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));
        assertFalse(this.tree.contains(24));

        this.tree.delete(36);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));
        assertFalse(this.tree.contains(36));

        this.tree.delete(52);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));
        assertFalse(this.tree.contains(52));

        this.tree.delete(93);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));
        assertFalse(this.tree.contains(93));

        this.tree.delete(86);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));
        assertFalse(this.tree.contains(86));

        this.tree.delete(83);
        assertTrue(this.isBalanced(tree.getRoot()));
        assertTrue(this.isWellOrdered(tree.getRoot()));
        assertFalse(this.tree.contains(83));
    }

    @Test
    void delete() {
    }
}