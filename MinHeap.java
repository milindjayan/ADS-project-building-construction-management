import java.io.IOException;
import java.util.Arrays;
import java.lang.Math;
import java.util.Iterator;

public class   MinHeap {
    int maxSize = 2000;
    building[] minHeap = new building[maxSize];
    int currSize;

    public MinHeap() {
        this.currSize = 0;
        this.minHeap[0] = new building(Integer.MIN_VALUE, Integer.MIN_VALUE,Integer.MIN_VALUE);
    }

    public boolean checkEmpty() {
        return currSize == 0;
    } //returns empty if the heap is empty

    public int findParent(int pos){
        return Math.floorDiv(pos,2);
    } //returns the position of the parent

    public int leftChild(int pos){
        return pos*2;
    } //returns the postion of the left child

    public int rightChild(int pos){
        return pos*2 + 1;
    } //returns the position of the right child

    public void insert(building newBuilding){ //inserts a new building into the heap
        if (currSize == minHeap.length) {
            return;
        }
        minHeap[++currSize] = newBuilding;
        int currentPosition = currSize;
        int parentPosition = findParent(currentPosition);

        while (minHeap[parentPosition].executedTime >= minHeap[currentPosition].executedTime) { //trickle up to the correct position
                if (minHeap[parentPosition].executedTime == minHeap[currentPosition].executedTime){//tie breaker when eecute time is the same
                    if(minHeap[parentPosition].buildingNum > minHeap[currentPosition].buildingNum){
                        swapPos(parentPosition,currentPosition);
                        currentPosition = parentPosition;
                        parentPosition = findParent(currentPosition);
                    } else {
                        break;
                    }
                } else { //normal swap
                    swapPos(parentPosition,currentPosition);
                    currentPosition = parentPosition;
                    parentPosition = findParent(currentPosition);
                }
        }

    }


    public void swapPos(int parentPos,int childPos) { //swap the postion of two elements
        building temp = minHeap[parentPos];
        minHeap[parentPos] = minHeap[childPos];
        minHeap[childPos] = temp;
    }

    public building removeMin() { //remove the minimum element and heapify
        building min = minHeap[1];
        minHeap[1] = minHeap[currSize];
        currSize = currSize - 1;
        maintainHeap(1);
        return min;

    }


    public void maintainHeap(int pos) { //maintain the heap property

        int l = leftChild(pos);
        int r = rightChild(pos);
        boolean lExists = l<=currSize;
        boolean rExists = r<=currSize;

        if (lExists && rExists) {

            if (minHeap[l].buildingNum == minHeap[r].buildingNum || minHeap[l].buildingNum == minHeap[pos].buildingNum || minHeap[r].buildingNum == minHeap[pos].buildingNum){ //since the duplicate building number case is handled in the RBT
                System.out.println("Never here");
            } else {
                if (minHeap[l].executedTime < minHeap[pos].executedTime || minHeap[r].executedTime < minHeap[pos].executedTime){ //case where the executed time is sufficient to make the swapping
                    if (minHeap[l].executedTime != minHeap[r].executedTime) {
                        if (minHeap[l].executedTime > minHeap[r].executedTime){
                            int childPos = r;
                            swapPos(r,pos);
                            maintainHeap(childPos);
                        } else {
                            int childPos = l;
                            swapPos(l,pos);
                            maintainHeap(childPos);
                        }
                    } else {
                        if (minHeap[l].buildingNum < minHeap[r].buildingNum) {
                            int childPos = l;
                            swapPos(l,pos);
                            maintainHeap(childPos);
                        } else {
                            int childPos = r;
                            swapPos(r,pos);
                            maintainHeap(childPos);
                        }
                    }

                }else if(minHeap[l].executedTime == minHeap[pos].executedTime || minHeap[r].executedTime == minHeap[pos].executedTime){ //tie breaker with building number
                    if (minHeap[l].executedTime == minHeap[pos].executedTime && minHeap[pos].executedTime == minHeap[r].executedTime){ //all the buildings have same building number
                        int smallest = pos;
                        if (minHeap[l].buildingNum < minHeap[pos].buildingNum && minHeap[r].buildingNum > minHeap[l].buildingNum){
                            smallest = l;
                        }else if (minHeap[r].buildingNum < minHeap[pos].buildingNum && minHeap[l].buildingNum>minHeap[r].buildingNum){
                            smallest = r;
                        }
                        if (smallest != pos){
                            int childPos = smallest;
                            swapPos(smallest,pos);
                            maintainHeap(childPos);
                        }
                    } else if(minHeap[r].executedTime == minHeap[pos].executedTime && minHeap[l].executedTime > minHeap[pos].executedTime) {
                        if (minHeap[pos].buildingNum > minHeap[r].buildingNum) {
                            int childPos = r;
                            swapPos(r, pos);
                            maintainHeap(childPos);
                        }
                    } else if(minHeap[l].executedTime == minHeap[pos].executedTime && minHeap[r].executedTime > minHeap[pos].executedTime) {
                        if (minHeap[pos].buildingNum > minHeap[r].buildingNum) {
                            int childPos = l;
                            swapPos(l,pos);
                            maintainHeap(childPos);
                        }

                    } else  { //all the execute time are different. Normal swap with the building number
                        if (minHeap[l].buildingNum > minHeap[r].buildingNum) {
                            int childPos = r;
                            swapPos(r,pos);
                            maintainHeap(childPos);
                        } else {
                            int childPos = l;
                            swapPos(l,pos);
                            maintainHeap(childPos);
                        }
                    }

                }
            }

        } else if(lExists) { //only one child present for the current node
            if (minHeap[l].executedTime <= minHeap[pos].executedTime){
                if (minHeap[l].executedTime < minHeap[pos].executedTime) {
                    int childPos = l;
                    swapPos(l,pos);
                    maintainHeap(childPos);
                } else if (minHeap[l].buildingNum < minHeap[pos].buildingNum){
                    int childPos = l;
                    swapPos(l,pos);
                    maintainHeap(childPos);
                }
            }
        }

    }

    public void print() {
        int[] checkList1 = new int[currSize+1];
        int[] checkList2 = new int[currSize+1];
        for (int i=0;i<currSize+1;++i){
            checkList1[i] = minHeap[i].buildingNum;
            checkList2[i] = minHeap[i].executedTime;
        }
        System.out.println(Arrays.toString(checkList1));
        System.out.println(Arrays.toString(checkList2));
    }

    public boolean build(int globalCounter,RBT rbt,String operation,int globTime) throws IOException {
        minHeap[1].executedTime = minHeap[1].executedTime + 1; //increment the executed time of the root by one

        if (minHeap[1].executedTime == minHeap[1].totalTime) { //stop construction and remove from data structures

            if ((globalCounter+1) == globTime && operation != "Insert"){ //check for the print to happen if the deletion and print happens together
                risingCity.printCommand = true;
                return true;
            } else {                                                     // remove min element and heapify
                String printThis = "("+minHeap[1].buildingNum+","+(globalCounter+1)+")";
                System.out.println(printThis);
                risingCity.fileWriter.println(printThis);
                rbt.delete1(minHeap[1].buildingNum);
                removeMin();
                Iterator tr = risingCity.pendingBuildingInsert.iterator(); //insert pending buildings into the heap and rbt
                while(tr.hasNext()) {
                    building b = (building) tr.next();
                    rbt.insert(b);
                    insert(b);
                }
                risingCity.pendingBuildingInsert.clear();
                return true;
            }

        } else if (minHeap[1].executedTime % 5 == 0) {   //heapify after the building is constructed for 5 days
            maintainHeap(1);
            Iterator tr = risingCity.pendingBuildingInsert.iterator(); //insert buildings that came before the current building could finish 5 days of construction
            while(tr.hasNext()) {
                building b = (building) tr.next();
                rbt.insert(b);
                insert(b);
            }
            risingCity.pendingBuildingInsert.clear();
            return true;
        } else {
            return false;
        }
    }

    public boolean buildTillCompletion(int globalCounter,RBT rbt)  { //continue construction until the heap becomes empty
        minHeap[1].executedTime = minHeap[1].executedTime + 1;
        if (minHeap[1].executedTime == minHeap[1].totalTime) {
            String printThis = "("+minHeap[1].buildingNum+","+(globalCounter+1)+")";
            System.out.println(printThis);
            risingCity.fileWriter.println(printThis);
            rbt.delete1(minHeap[1].buildingNum);
            removeMin();
            return true;

        } else if (minHeap[1].executedTime % 5 == 0) {
            maintainHeap(1);
            return true;
        } else {
            return false;
        }
    }
}
