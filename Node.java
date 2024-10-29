class Node
{
    float element;
    String name;
    int h;  //for height
    Node leftChild;
    Node rightChild;

    //default constructor to create null node
    public Node()
    {
        leftChild = null;
        rightChild = null;
        element = 0;
        h = 0;
        name="";
    }
    // parameterized constructor
    public Node(float element, String name)
    {
        leftChild = null;
        rightChild = null;
        this.element = element;
        h = 0;
        this.name=name;
    }
}

