public class AVLTreeNode<T> {
    private T data;
    private AVLTreeNode<T> leftChild;
    private AVLTreeNode<T> rightChild;
    private int balance;
    private int height;

    public AVLTreeNode(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public AVLTreeNode<T> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(AVLTreeNode<T> leftChild) {
        this.leftChild = leftChild;
    }

    public AVLTreeNode<T> getRightChild() {
        return rightChild;
    }

    public void setRightChild(AVLTreeNode<T> rightChild) {
        this.rightChild = rightChild;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
