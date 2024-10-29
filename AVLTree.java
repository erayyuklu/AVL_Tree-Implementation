import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class AVLTree {
    public Node rootNode;
    private FileWriter writer;

    //constructor. It includes the writer. So, I can use the same writer and it is more efficient than use 2 different writers at same time.
    public AVLTree(FileWriter writer) {
        rootNode = null;
        this.writer = writer;
    }

    public void insertElement(float element, String name) throws Exception {
        String order = "";
        rootNode = insertElement(element, name, rootNode, order);
    }

    public void removeElement(float element) throws Exception {
        rootNode = removeElement(element, rootNode);
    }


    private Node insertElement(float element, String name, Node node, String order) throws Exception {
        //check whether the node is null or not
        if (node != null) {
            order = node.name;
            //System.out.println(order+" welcomed "+name);
            writer.write(order + " welcomed " + name + "\n");
        }
        if (node == null)
            node = new Node(element, name);
            //insert a node in case when the given element is lesser than the element of the root node
        else if (element < node.element) {
            node.leftChild = insertElement(element, name, node.leftChild, order);

            if (getHeight(node.leftChild) - getHeight(node.rightChild) == 2)
                if (element < node.leftChild.element)
                    node = rotateWithLeftChild(node);
                else
                    node = doubleWithLeftChild(node);
        } else if (element > node.element) {
            node.rightChild = insertElement(element, name, node.rightChild, order);
            if (getHeight(node.rightChild) - getHeight(node.leftChild) == 2)
                if (element > node.rightChild.element)
                    node = rotateWithRightChild(node);
                else
                    node = doubleWithRightChild(node);
        } else ;  // if the element is already present in the tree, we will do nothing
        node.h = getMaxHeight(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;

        return node;
    }

    Node success; //if removed node has two children
    Node notSuccess;//if removed node has one children or no children

    //if removed node has one children or no children we use findNotSuccess
    public String findNotSuccess() {
        if (notSuccess == null) {
            return "nobody";
        } else {
            return notSuccess.name;
        }
    }

    private Node removeElement(float element, Node node) throws Exception {
        if (node == null) {
            // Element not found in the tree, nothing to remove
            return null;
        }

        if (element < node.element) {
            node.leftChild = removeElement(element, node.leftChild);
        } else if (element > node.element) {
            node.rightChild = removeElement(element, node.rightChild);
        } else {
            // Element is found, and it needs to be removed
            if (node.leftChild == null) {
                // Node has only a right child or no children
                notSuccess = node.rightChild;
                return node.rightChild;
            } else if (node.rightChild == null) {
                // Node has only a left child
                notSuccess = node.leftChild;
                return node.leftChild;

            } else {
                // Node has two children, find the in-order successor
                Node successor = findMin(node.rightChild);
                success = successor;
                // Copy the successor's values to the current node
                node.element = successor.element;
                node.name = successor.name;
                // Remove the successor from the right subtree
                node.rightChild = removeElement(successor.element, node.rightChild);
            }
        }

        // Update height and balance factor of the current node
        node.h = getMaxHeight(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;

        // Check for balance and perform rotations if necessary
        if (getHeight(node.leftChild) - getHeight(node.rightChild) == 2) {
            if (getHeight(node.leftChild.leftChild) >= getHeight(node.leftChild.rightChild)) {
                node = rotateWithLeftChild(node);
            } else {
                node = doubleWithLeftChild(node);
            }
        } else if (getHeight(node.rightChild) - getHeight(node.leftChild) == 2) {
            if (getHeight(node.rightChild.rightChild) >= getHeight(node.rightChild.leftChild)) {
                node = rotateWithRightChild(node);
            } else {
                node = doubleWithRightChild(node);
            }
        }

        return node;
    }

    //findMin method is essential to balance after removing. It determines successor of removed node
    private Node findMin(Node node) {
        while (node.leftChild != null) {
            node = node.leftChild;
        }
        return node;
    }

    //findCommonAncestor is the method of finding target. It finds common ancstor node of given two nodes.
    public String findCommonAncestor(float gms1, float gms2) {
        Node arbit = findCommonAncestor(rootNode, gms1, gms2);
        return arbit.name + " " + String.format("%.3f", arbit.element).replace(",", ".");
    }


    private Node findCommonAncestor(Node root, float gms1, float gms2) {
        if (root == null) {
            return null;
        }

        if (root.element > gms1 && root.element > gms2) {
            return findCommonAncestor(root.leftChild, gms1, gms2);
        } else if (root.element < gms1 && root.element < gms2) {
            return findCommonAncestor(root.rightChild, gms1, gms2);
        }

        return root;
    }

    // creating rotateWithLeftChild() method to perform rotation of binary tree node with left child
    private Node rotateWithLeftChild(Node node2) {
        Node node1 = node2.leftChild;
        node2.leftChild = node1.rightChild;
        node1.rightChild = node2;
        node2.h = getMaxHeight(getHeight(node2.leftChild), getHeight(node2.rightChild)) + 1;
        node1.h = getMaxHeight(getHeight(node1.leftChild), node2.h) + 1;
        return node1;
    }

    // creating rotateWithRightChild() method to perform rotation of binary tree node with right child
    private Node rotateWithRightChild(Node node1) {
        Node node2 = node1.rightChild;
        node1.rightChild = node2.leftChild;
        node2.leftChild = node1;
        node1.h = getMaxHeight(getHeight(node1.leftChild), getHeight(node1.rightChild)) + 1;
        node2.h = getMaxHeight(getHeight(node2.rightChild), node1.h) + 1;
        return node2;
    }

    //creating doubleWithLeftChild() method to perform double rotation of binary tree node. This method first rotate the left child with its right child, and after that, node3 with the new left child
    private Node doubleWithLeftChild(Node node3) {
        node3.leftChild = rotateWithRightChild(node3.leftChild);
        return rotateWithLeftChild(node3);
    }

    //creating doubleWithRightChild() method to perform double rotation of binary tree node. This method first rotate the right child with its left child and after that node1 with the new right child
    private Node doubleWithRightChild(Node node1) {
        node1.rightChild = rotateWithLeftChild(node1.rightChild);
        return rotateWithRightChild(node1);
    }

    //creating allSameRanks() method to write nodes that has same ranks starting from rootNode
    public void allSameRanks() throws IOException {
        allSameRanks(increment, rootNode);
        increment = -1;
    }

    private void allSameRanks(int increm, Node root) throws IOException {
        if (root != null && increm == 0) {
            //System.out.print(" "+root.name+" "+String.format("%.3f", root.element));
            writer.write(" " + root.name + " " + String.format("%.3f", root.element).replace(",", "."));
        }
        increm--;
        if (increm != -1 && root != null) {
            allSameRanks(increm, root.leftChild);
            allSameRanks(increm, root.rightChild);
        }


    }
    //increment is the number of moving downward from rootNode
    int increment = -1;

    //I use findNode method to state ranks of nodes when it needs
    public void findNode(float gms, int inc) {
        findNode(rootNode, gms, inc);
    }

    private Node findNode(Node node, float gms, int incr) {
        increment += 1;
        if (node == null) {
            return null;
        }

        if (gms < node.element) {
            return findNode(node.leftChild, gms, this.increment);
        } else if (gms > node.element) {
            return findNode(node.rightChild, gms, this.increment);
        } else {
            return node;
        }
    }

    //gethHeight and getMaxHeight are essential metrhods to balance tree while insertin or removing
    private int getHeight(Node node) {
        return node == null ? -1 : node.h;
    }

    //create maxNode() method to get the maximum height from left and right node
    private int getMaxHeight(int leftNodeHeight, int rightNodeHeight) {
        return leftNodeHeight > rightNodeHeight ? leftNodeHeight : rightNodeHeight;
    }


    public int divide() {
        Map<Node, Integer> memo = new HashMap<>();
        return divide(rootNode, memo);
    }

    //I used hashMap in divide function to keep variables efficiently.
    private int divide(Node root, Map<Node, Integer> memo) {
        if (root == null)
            return 0;

        // Check if we've already calculated the size for this node
        if (memo.containsKey(root))
            return memo.get(root);

        // Calculate size excluding the current node
        int size_excl = divide(root.leftChild, memo) + divide(root.rightChild, memo);

        // Calculate size including the current node
        int size_incl = 1;
        if (root.leftChild != null)
            size_incl += divide(root.leftChild.leftChild, memo) + divide(root.leftChild.rightChild, memo);
        if (root.rightChild != null)
            size_incl += divide(root.rightChild.leftChild, memo) + divide(root.rightChild.rightChild, memo);

        // Store the result in the memoization map
        int result = Math.max(size_incl, size_excl);
        memo.put(root, result);

        return result;
    }


}