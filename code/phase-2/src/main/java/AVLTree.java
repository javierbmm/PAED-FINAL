import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AVLTree<K extends Comparable<? super K>> {
    private AVLTreeNode<K> root;
    private int size;

    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return null == root;
    }

    public boolean contains(@NotNull K object) {
        return null != this._find(object);
    }

    public K get(K object) {
        AVLTreeNode<K> node = _find(object);
        K data = null;

        if(null != node) {
            data = node.getData();
        }

        return data;
    }

    private AVLTreeNode<K> _find(@NotNull K object) {
        if(null == this.root) {
            return null;
        }

        AVLTreeNode<K> current = this.root;

        //loop until we'eve found an object equal in order
        while(0 != current.getData().compareTo(object)) {

            //is the object we're looking for smaller than current?
            if(0 > current.getData().compareTo(object)) {

                //go to left subtree
                current = current.getRightChild();
            }
            else {
                //go to right subtree
                current = current.getLeftChild();
            }

            //current is null so the item is not in the tree
            if(null == current) {
                break;
            }
        }

        //return the node
        return current;
    }

    public void insert(K key) {
        this.root = this._insert(this.getRoot(), key);
    }

    public void delete(K key) {
        this.root = this._delete(this.getRoot(), key);
    }

    private AVLTreeNode<K> _delete(AVLTreeNode<K> node, K key) {
        //node is null nothing to delete
        if(null == node) {
            return null;
        }

        //check if we have the key at this node or whether its
        //in the left or right subtree
        if(0 < node.getData().compareTo(key)) {
            //key is somewhere in the left subtree so delete recursively
            node.setLeftChild(_delete(node.getLeftChild(), key));
        }
        else if(0 > node.getData().compareTo(key)) {
            //key is somewhere in the right subtree so search for it and
            //delete recursively
            node.setRightChild(_delete(node.getRightChild(), key));
        }
        else {
            if(null == node.getLeftChild() || null == node.getRightChild()) {
                node = (null == node.getLeftChild()) ? node.getRightChild() : node.getLeftChild();
            }
            else {
                AVLTreeNode<K> leftMost = this.getLeftMostChild(node.getRightChild());
                node.setData(leftMost.getData());
                node.setRightChild(this._delete(node.getRightChild(), key));
            }
        }

        if(node != null) {
            node = this.balanceTree(node);
        }

        return node;
    }

    private AVLTreeNode<K> getLeftMostChild(AVLTreeNode<K> node) {
        AVLTreeNode<K> tmp = node;

        while(null != tmp.getLeftChild()) {
            tmp = tmp.getLeftChild();
        }

        return tmp;
    }

    private AVLTreeNode<K> _insert(AVLTreeNode<K> node, K key) {
        //tree is empty so return new node with the first data point
        if(null == node) {
            node = new AVLTreeNode<>(key);
        }
        //tree is not empty so decide which subtree it needs to go to
        else if(0 < node.getData().compareTo(key)) {
            //new point is headed towards the left subtree from this node
            //so insert to the left node recursively
            node.setLeftChild(this._insert(node.getLeftChild(), key));
        }
        else if(0 > node.getData().compareTo(key)) {
            //new point is headed towards the right subtree from this node
            //so insert to the right node recursively
            node.setRightChild(this._insert(node.getRightChild(), key));
        }
        else {
            throw new RuntimeException("AVL Tree cannot contain duplicate keys!");
        }

        //rebalance tree
        return this.balanceTree(node);
    }

    private void updateNodeHeight(@NotNull AVLTreeNode<K> node) {
        int newHeight = 1 + Math.max(
                this.height(node.getLeftChild()),
                this.height(node.getRightChild())
        );

        node.setHeight(newHeight);
    }

    private int height(@Nullable AVLTreeNode<K> node) {
        return node == null ? -1 : node.getHeight();
    }

    private int getBalance(@Nullable AVLTreeNode<K> node) {
        return node == null ?
                0 :
                (this.height(node.getRightChild()) - this.height(node.getLeftChild()));
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
            if(this.height(rootRightRight) > this.height(rootRightLeft)) {
                treeNode = this.leftRotation(treeNode);
            }
            else {

                //we need to apply two rotations
                treeNode.setRightChild(this.rightRotation(treeNode.getRightChild()));
                treeNode = leftRotation(treeNode);
            }
        }

        //balance is negative so we need to apply right rotations
        else if(-1 > balance) {
            AVLTreeNode<K> leftLeft = treeNode.getLeftChild().getLeftChild();
            AVLTreeNode<K> leftRight = treeNode.getLeftChild().getRightChild();

            //if height of left node's left node is greater than the height
            //of the left node's right node then we apply right rotation
            if(this.height(leftLeft) > this.height(leftRight)) {
                treeNode = this.rightRotation(treeNode);
            }
            else {
                treeNode.setLeftChild(this.leftRotation(treeNode.getLeftChild()));
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

    @Override
    public String toString() {
        return "AVLTree{" + "root=" + root +
                ", size=" + size +
                '}';
    }
}
