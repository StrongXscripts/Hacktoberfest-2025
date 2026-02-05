/**
 * Implementation of Red-Black Tree data structure
 * A Red-Black Tree is a self-balancing Binary Search Tree where every node follows these rules:
 * 1. Every node is either red or black
 * 2. The root is black
 * 3. All leaves (NIL) are black
 * 4. If a node is red, then both its children are black
 * 5. Every path from the root to any leaf has the same number of black nodes
 */
public class RedBlackTree<T extends Comparable<T>> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        T data;
        Node left, right;
        boolean color;
        int size;

        Node(T data, boolean color) {
            this.data = data;
            this.color = color;
            this.size = 1;
        }
    }

    private Node root;

    public RedBlackTree() {
        root = null;
    }

    private boolean isRed(Node node) {
        if (node == null) return false;
        return node.color == RED;
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.size;
    }

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void insert(T data) {
        root = insert(root, data);
        root.color = BLACK;
    }

    private Node insert(Node node, T data) {
        if (node == null) {
            return new Node(data, RED);
        }

        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = insert(node.left, data);
        } else if (cmp > 0) {
            node.right = insert(node.right, data);
        }

        // Fix any right-leaning red links
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = 1 + size(h.left) + size(h.right);
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    public boolean contains(T data) {
        return contains(root, data);
    }

    private boolean contains(Node node, T data) {
        if (node == null) {
            return false;
        }
        
        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            return contains(node.left, data);
        } else if (cmp > 0) {
            return contains(node.right, data);
        } else {
            return true;
        }
    }

    // Method to get the black height of the tree (number of black nodes in any path from root to leaf)
    public int getBlackHeight() {
        return getBlackHeight(root);
    }

    private int getBlackHeight(Node node) {
        if (node == null) {
            return 0;
        }
        
        int blackHeight = node.color == BLACK ? 1 : 0;
        int leftHeight = getBlackHeight(node.left);
        int rightHeight = getBlackHeight(node.right);
        
        // Both paths should have same black height in a valid Red-Black tree
        if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight) {
            return -1;
        }
        
        return blackHeight + leftHeight;
    }

    // Method to verify Red-Black Tree properties
    public boolean isValidRedBlackTree() {
        if (root != null && root.color == RED) {
            return false;
        }
        return isValidRedBlackTree(root) != -1;
    }

    private int isValidRedBlackTree(Node node) {
        if (node == null) {
            return 0;
        }
        
        // Check Red-Black tree property: red nodes should not have red children
        if (isRed(node) && (isRed(node.left) || isRed(node.right))) {
            return -1;
        }
        
        int leftHeight = isValidRedBlackTree(node.left);
        int rightHeight = isValidRedBlackTree(node.right);
        
        if (leftHeight == -1 || rightHeight == -1 || leftHeight != rightHeight) {
            return -1;
        }
        
        return node.color == BLACK ? leftHeight + 1 : leftHeight;
    }
}
