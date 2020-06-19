import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AVLTree<K extends Comparable<K>> {
    private AVLTreeNode<K> root;
    private int size;

    public AVLTree() {
        this.root = null;
        this.size = 0;
    }
    public AVLTreeNode<K> find(@NotNull K object) {
        AVLTreeNode<K> current = this.root;

        //loop until we'eve found an object equal in order
        while(0 != current.getData().compareTo(object)) {

            //is the object we're looking for smaller than current?
            if(0 > current.getData().compareTo(object)) {

                //go to left subtree
                current = current.getLeftChild();
            }
            else {
                //go to right subtree
                current = current.getRightChild();
            }

            //current is null so the item is not in the tree
            if(null == current) {
                break;
            }
        }

        //return the node
        return current;
    }

    private void updateNodeHeight(@NotNull AVLTreeNode<K> node) {
        int newHeight = 1 + Math.max(
                this.getNodeHeight(node.getLeftChild()),
                this.getNodeHeight(node.getRightChild())
        );

        node.setHeight(newHeight);
    }

    private int getNodeHeight(@Nullable AVLTreeNode<K> node) {
        return node == null ? -1 : node.getHeight();
    }

    private int getBalance(@Nullable AVLTreeNode<K> node) {
        return node == null ?
                0 :
                (this.getNodeHeight(node.getRightChild()) - this.getNodeHeight(node.getLeftChild()));
    }

    private AVLTreeNode<K> leftRotation(@NotNull AVLTreeNode<K> rotationRoot) {
        AVLTreeNode<K> newRoot = rotationRoot.getRightChild();
        AVLTreeNode<K> rootRightLeft = newRoot.getLeftChild();

        newRoot.setLeftChild(rotationRoot);
        rotationRoot.setRightChild(rootRightLeft);

        this.updateNodeHeight(rotationRoot);
        this.updateNodeHeight(newRoot);

        return newRoot;
    }

    private AVLTreeNode<K> rightRotation(@NotNull AVLTreeNode<K> rotationRoot) {
        AVLTreeNode<K> newRoot = rotationRoot.getLeftChild();
        AVLTreeNode<K> rootLeftRight = newRoot.getRightChild();

        newRoot.setRightChild(rotationRoot);
        rotationRoot.setLeftChild(rootLeftRight);

        updateNodeHeight(rotationRoot);
        updateNodeHeight(newRoot);

        return newRoot;
    }

    private AVLTreeNode<K> balanceTree(@NotNull AVLTreeNode<K> treeNode) {
        //make sure root node's height is correct
        updateNodeHeight(treeNode);
        int balance = this.getBalance(treeNode);

        //balance is positive so we need to check for a potential left rotation
        if(1 < balance) {
            AVLTreeNode<K> rootRightRight = treeNode.getRightChild().getRightChild();
            AVLTreeNode<K> rootRightLeft = treeNode.getRightChild().getLeftChild();

            //if the right node's right node's height is bigger than the
            //right node's left node's height then we need a left rotation
            if(this.getNodeHeight(rootRightRight) > this.getNodeHeight(rootRightLeft)) {
                treeNode = this.leftRotation(treeNode);
            }
            else {

                //we need to apply two rotations
                treeNode.setRightChild(this.rightRotation(treeNode));
                treeNode = leftRotation(treeNode);
            }
        }

        //balance is negative so we need to apply right rotations
        else if(-1 > balance) {
            AVLTreeNode<K> leftLeft = treeNode.getLeftChild().getLeftChild();
            AVLTreeNode<K> leftRight = treeNode.getLeftChild().getRightChild();

            //if height of left node's left node is greater than the height
            //of the left node's right node then we apply right rotation
            if(this.getNodeHeight(leftLeft) > this.getNodeHeight(leftRight)) {
                treeNode = this.rightRotation(treeNode);
            }
            else {
                treeNode.setLeftChild(treeNode.getLeftChild());
                treeNode = this.rightRotation(treeNode);
            }
        }

        //return tree state
        return treeNode;
    }

    public AVLTreeNode<K> getRoot() {
        return root;
    }

    public void setRoot(AVLTreeNode<K> root) {
        this.root = root;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
