

public class RBT {

    private final int r = 0; //red would be 0
    private final int b = 1; //black would be 1

    public class RBTNode { //rbt node

        building key;
        int color = b;
        RBTNode leftChild = nil;
        RBTNode rightChild = nil;
        RBTNode parent = nil;

        RBTNode(building key) {
            this.key = key;
        }
    }
    building nilB = new building(-1,-1,-1); //nil building initialisation
    private final RBTNode nil = new RBTNode(nilB);
    private RBTNode root = nil; //root initially nil


    public int getBuildingNum(RBTNode node) {   //return building number of the current node
        return node.key.buildingNum;
    }


    public String findImbalance(RBTNode node,RBTNode parent,RBTNode uncle,RBTNode grandParent) { //returns the imbalance if any
        String imbalance;
        if (parent.leftChild == node && grandParent.leftChild == parent && grandParent.rightChild.color == b) {
            imbalance="LLb";
        } else if (parent.rightChild == node && grandParent.leftChild == parent && grandParent.rightChild.color == b) {
            imbalance="LRb";
        } else if (parent.rightChild == node && grandParent.rightChild == parent && grandParent.leftChild.color == b) {
            imbalance="RRb";
        } else if (parent.leftChild == node && grandParent.rightChild == parent && grandParent.leftChild.color == b) {
            imbalance="RLb";
        } else if (uncle.color == r && uncle != nil) {
            imbalance="XXr";
        } else {
            imbalance="never here";
        }
        return imbalance;
    }


    public void rightRotationHelper(RBTNode checkNode) { //helper function for left rotation
        checkNode.leftChild.parent = checkNode.parent;
        checkNode.parent = checkNode.leftChild;
        if (checkNode.leftChild.rightChild != nil) {
            checkNode.leftChild.rightChild.parent = checkNode;
        }
        checkNode.leftChild = checkNode.leftChild.rightChild;
        checkNode.parent.rightChild = checkNode;
    }

    public void rightRotationHelperRoot(RBTNode checkNode) { //hrlper function to rotate the root
        RBTNode left = root.leftChild;
        root.leftChild = root.leftChild.rightChild;
        left.rightChild.parent = root;
        root.parent = left;
        left.rightChild = root;
        left.parent = nil;
        root = left;
    }


    public void RRotation(RBTNode node) { //right rotation main function
        if(node.parent != nil){
            if(node.parent.leftChild == node) {
                node.parent.leftChild = node.leftChild;
                rightRotationHelper(node);
            } else {
                node.parent.rightChild = node.leftChild;
                rightRotationHelper(node);
            }
        } else {
            rightRotationHelperRoot(node);
        }
    }


    public void leftRotationHelper(RBTNode checkNode){ //helper function for left rotation
        checkNode.rightChild.parent = checkNode.parent;
        checkNode.parent = checkNode.rightChild;
        if(checkNode.rightChild.leftChild != nil) {
            checkNode.rightChild.leftChild.parent = checkNode;
        }
        checkNode.rightChild = checkNode.rightChild.leftChild;
        checkNode.parent.leftChild = checkNode;
    }

    public void leftRotationHelperRoot(RBTNode checkNode){ //helper function to rotate the root
        RBTNode right = root.rightChild;
        root.rightChild = right.leftChild;
        right.leftChild.parent = root;
        root.parent = right;
        right.leftChild = root;
        right.parent = nil;
        root = right;
    }

    public void LRotation1(RBTNode node){ //left rotation function
        if(node.parent != nil){
            if(node.parent.leftChild == node) {
                node.parent.leftChild = node.rightChild;
                leftRotationHelper(node);
            } else {
                node.parent.rightChild = node.rightChild;
                leftRotationHelper(node);
            }
        } else {
                leftRotationHelperRoot(node);
        }
    }


    public void maintainTreeInsert(RBTNode newNode) { //maintains rbt properties after inserting a new node
        while(newNode.parent.color == r){
            RBTNode parent = newNode.parent;
            RBTNode grandParent = parent.parent;
            RBTNode uncle;
            if (grandParent.rightChild == parent){
                uncle = grandParent.leftChild;
            } else {
                uncle = grandParent.rightChild;
            }
            String imbalance = findImbalance(newNode,parent,uncle,grandParent);// returns the imbalance after insertion
            if (imbalance == "XXr") {
                parent.color = b;
                uncle.color=b;
                grandParent.color = r;
                newNode = grandParent;
                continue;
            } else if(imbalance == "LRb"){
                newNode = newNode.parent;
                LRotation1(newNode);
                newNode.parent.color = b;
                newNode.parent.parent.color = r;
                RRotation(newNode.parent.parent);
            } else if(imbalance == "LLb"){
                newNode.parent.color = b;
                newNode.parent.parent.color = r;
                RRotation(newNode.parent.parent);
            } else if(imbalance == "RLb"){
                newNode = newNode.parent;
                RRotation(newNode);
                newNode.parent.color = b;
                newNode.parent.parent.color = r;
                LRotation1(newNode.parent.parent);
            } else if(imbalance == "RRb"){
                newNode.parent.color = b;
                newNode.parent.parent.color = r;
                LRotation1(newNode.parent.parent);
            }
        }
        root.color = b;
    }

    public void insert(building b1) {
        RBTNode newNode = new RBTNode(b1);
        RBTNode temp = root;
        if (root == nil) {  //insert into initially empty tree
            root = newNode;
            root.color = b;
            root.parent = nil;
        } else {
            newNode.color = r; //newly inserted node will be red in color
            while(true) { //standard BST insertion
                if (getBuildingNum(temp) < getBuildingNum(newNode)){
                    if (temp.rightChild == nil) {
                        temp.rightChild = newNode;
                        newNode.parent = temp;
                        break;
                    } else {
                        temp = temp.rightChild;
                    }
                } else if (getBuildingNum(temp) > getBuildingNum(newNode)){
                    if (temp.leftChild == nil) {
                        temp.leftChild = newNode;
                        newNode.parent = temp;
                        break;
                    } else {
                        temp = temp.leftChild;
                    }
                } else {
                    System.out.println("Duplicate encountered: "+ b1.buildingNum); //Exit program if duplicate is encountered
                    System.exit(1);
                }
            }
            maintainTreeInsert(newNode);
        }
    }

    public int findDegree(RBTNode Node){ //returns the degree of a node
        if (Node.leftChild == nil && Node.rightChild == nil){
            return 0;
        } else if (Node.leftChild == nil || Node.rightChild ==nil){
            return 1;
        } else {
            return 2;
        }
    }



    void maintainTreeDelete(RBTNode checkNode){ //maintains the tree after performing a delete operation
        while(checkNode!=root && checkNode.color == b){
            if(checkNode == checkNode.parent.rightChild){
                RBTNode sibling = checkNode.parent.leftChild;
                if(sibling.color == r){  //Rr0
                    sibling.color = b;
                    checkNode.parent.color = r;
                    RRotation(checkNode.parent);
                    sibling = checkNode.parent.leftChild;
                }
                if(sibling.rightChild.color == b && sibling.leftChild.color == b){//Rb0
                    sibling.color = r;
                    checkNode = checkNode.parent;
                    continue;
                }
                else if(sibling.leftChild.color == b){//Rb1
                    sibling.rightChild.color = b;
                    sibling.color = r;
                    LRotation1(sibling);
                    sibling = checkNode.parent.leftChild;
                }
                if(sibling.leftChild.color == r){ //Rb1
                    sibling.color = checkNode.parent.color;
                    checkNode.parent.color = b;
                    sibling.leftChild.color = b;
                    RRotation(checkNode.parent);
                    checkNode = root;
                }

            }else{
                RBTNode sibling = checkNode.parent.rightChild;
                if(sibling.color == r){ //Lr0
                    sibling.color = b;
                    checkNode.parent.color = r;
                    LRotation1(checkNode.parent);
                    sibling = checkNode.parent.rightChild;
                }
                if(sibling.leftChild.color == b && sibling.rightChild.color == b){ //Lb0
                    sibling.color = r;
                    checkNode = checkNode.parent;
                    continue;
                }
                else if(sibling.rightChild.color == b){//Lb1
                    sibling.leftChild.color = b;
                    sibling.color = r;
                    RRotation(sibling);
                    sibling = checkNode.parent.rightChild;
                }
                if(sibling.rightChild.color == r){ //Lb1
                    sibling.color = checkNode.parent.color;
                    checkNode.parent.color = b;
                    sibling.rightChild.color = b;
                    LRotation1(checkNode.parent);
                    checkNode = root;
                }
            }
        }
        checkNode.color = b; //recolor the checknode to black
    }



    private void performDelete(RBTNode Node, RBTNode child) { //performs the deletion of a node
        if (Node.parent != nil) {
            RBTNode parent = Node.parent;
            child.parent = parent;
            if (parent.leftChild == Node){
                parent.leftChild = child;
            } else if(parent.rightChild == Node){
                parent.rightChild = child;
            }
        } else{
            root = child;
            child.parent = Node.parent;
        }
    }

    public void delete1(int buildingNumber){ //deletes a node from the tree
        RBTNode temp;
        temp = search(buildingNumber,root);
        if (temp == null){
            return;
        } else {
            RBTNode temp2;
            int initialColor = temp.color;
            int degree = findDegree(temp);
            if (degree == 1 || degree == 0) {   // standard BST delete for degree 1 or 0 nodes
                if (temp.leftChild == nil){
                    temp2 = temp.rightChild;
                    performDelete(temp,temp2);
                } else {
                    temp2 = temp.leftChild;
                    performDelete(temp,temp.leftChild);
                }

            } else {

                RBTNode rightMin = rightSubTreeMin(temp.rightChild); // standard BST delete for degree 2 node
                initialColor = rightMin.color;
                temp2 = rightMin.rightChild;
                if (rightMin.parent == temp){
                    temp2.parent = rightMin;
                } else{
                    performDelete(rightMin,rightMin.rightChild);
                    rightMin.rightChild = temp.rightChild;
                    rightMin.rightChild.parent = rightMin;
                }
                performDelete(temp,rightMin);
                rightMin.leftChild = temp.leftChild;
                rightMin.leftChild.parent = rightMin;
                rightMin.color = temp.color;
            }
            if(initialColor != r){ //fix the tree if a black node was deleted
                maintainTreeDelete(temp2);
            }

        }
    }

    private RBTNode rightSubTreeMin(RBTNode node) { //returns the minimum of the right subtree
        if (node.leftChild == nil){
            return node;
        }
        return rightSubTreeMin(node.leftChild);
    }


    public RBTNode search(int searchB,RBTNode Node){ //searches for a node recursively
        if (root == nil) {
            return null;
        }
        if (Node.key.buildingNum == searchB){
            return Node;

        } else if(Node.key.buildingNum < searchB){
            if (Node.rightChild != nil) {
                return search(searchB,Node.rightChild);
            }
        } else if(Node.key.buildingNum > searchB){
            if (Node.leftChild != nil){
                return search(searchB,Node.leftChild);
            }
        }
        return null;
    }

    public building findNode(int buildingNumber) { //used to search the rbt for the given node
        RBTNode node = search(buildingNumber,root);
        if (node == null) {
            return nilB;
        } else {
            return node.key;
        }
    }



    public String printRange(int startRange, int endRange) {//returns the active buildings in the given range
        StringBuilder s = new StringBuilder();
        findNodes(root,startRange,endRange,s);
        if (s.length() != 0){
            String newS = s.toString();
            return newS.substring(0, newS.length() - 1);
        } else {
            return "(0,0,0)";
        }

    }


    public void findNodes(RBTNode node,int startRange,int endRange,StringBuilder s){//inorder traversal to search for buildings in given range
        if(node == null) {
            return;
        }
        if (endRange > node.key.buildingNum){
            findNodes(node.rightChild,startRange,endRange,s);
        }
        if (startRange < node.key.buildingNum) {
            findNodes(node.leftChild,startRange,endRange,s);
        }
        if (startRange <= node.key.buildingNum && endRange >=node.key.buildingNum){
            if (node.key.buildingNum != endRange){
                s.append("(").append(node.key.buildingNum).append(",").append(node.key.executedTime).append(",").append(node.key.totalTime).append("),");
            } else {
                return;
            }
        }
    }
}




