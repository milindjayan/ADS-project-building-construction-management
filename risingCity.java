import java.io.*;
import java.util.*;


public class risingCity {


    public static List<building> pendingBuildingInsert = new ArrayList<>();
    public static PrintWriter fileWriter;
    public static boolean printCommand = false; //to ensure that the printing happens before deletion
    static {
        try {
            fileWriter = new PrintWriter("output_file.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String findOperation(String line){ //returns the operation read in
        if (line.matches("(.*)Insert(.*)")) {
            return "Insert";
        } else if(line.matches("(.*),(.*)")){
            return "PrintRange";
        } else {
            return "Print";
        }
    }

    public static void main(String [] args) throws IOException {

        if (args.length == 0){
            System.out.println("Please enter the file path");
            System.exit(1);
        }
        String s1 = args[0]; //read in the cmd argument

        File text = new File(s1);


        Scanner scan = new Scanner(text);
        MinHeap minHeap = new MinHeap(); //initialise minHeap object
        RBT rbt = new RBT(); //initialise rbt object
        int globalCounter = 0; //set global counter to 0
        boolean canProceed = true; //check if a building can be inserted



        while(scan.hasNextLine()) { //start reading the input from the file
            String line = scan.nextLine();
            String operation = findOperation(line);
            int globTime = Integer.parseInt(line.substring(0, line.indexOf(":")).trim()); // read in the input time
            while (globalCounter < globTime) {//check weather the read time is equal to the global counter, else continue construction

                if (minHeap.checkEmpty()) { //required if the the next line comes in after a while
                    globalCounter = globalCounter + 1;
                } else {
                    canProceed = minHeap.build(globalCounter,rbt,operation,globTime);
                    globalCounter = globalCounter + 1;
                }
            }
            if (operation == "Insert") { //insert the building to the data strucutres
                int buildingNum = Integer.parseInt(line.substring(line.indexOf("(") + 1, line.indexOf(",")).trim());
                int totalTime = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(")")).trim());
                building newBuilding = new building(buildingNum, 0, totalTime);

                if (canProceed) {           //insert building if the build function returns True
                    rbt.insert(newBuilding);
                    minHeap.insert(newBuilding);
                    minHeap.build(globalCounter,rbt,operation,globTime);
                    globalCounter = globalCounter + 1;
                } else {                    //insert the building into the pending building array and wait for current execution to reach 5 days
                    pendingBuildingInsert.add(newBuilding);
                    minHeap.build(globalCounter,rbt,operation,globTime);    //construction will continue on min building after inserting all buildings
                    globalCounter = globalCounter + 1;
                }

            } else if(operation == "PrintRange"){//prints the current active buildings in the range
                if (printCommand == true){ //if the print command and a building deletion happen during the same time
                    int startRange = Integer.parseInt(line.substring(line.indexOf("(") + 1, line.indexOf(",")).trim());
                    int endRange = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(")")).trim());
                    String returnString = rbt.printRange(startRange,endRange);
                    fileWriter.println(returnString);
                    System.out.println(returnString);
                    String printThis = "("+minHeap.minHeap[1].buildingNum+","+(globalCounter)+")";
                    System.out.println(printThis);
                    risingCity.fileWriter.println(printThis);
                    rbt.delete1(minHeap.minHeap[1].buildingNum);
                    minHeap.removeMin();
                    Iterator tr = risingCity.pendingBuildingInsert.iterator();
                    while(tr.hasNext()) {
                        building b1 = (building) tr.next();
                        rbt.insert(b1);
                        minHeap.insert(b1);
                    }
                    risingCity.pendingBuildingInsert.clear();
                    printCommand = false;
                } else {

                    int startRange = Integer.parseInt(line.substring(line.indexOf("(") + 1, line.indexOf(",")).trim());
                    int endRange = Integer.parseInt(line.substring(line.indexOf(",") + 1, line.indexOf(")")).trim());
                    String returnString = rbt.printRange(startRange,endRange);
                    fileWriter.println(returnString);
                    System.out.println(returnString);
                }

            } else {//print the building details if present
                if (printCommand == true){ //if the print command and a building deletion happen during the same time
                    int buildingNum = Integer.parseInt(line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim());
                    building b = rbt.findNode(buildingNum);
                    if (b == rbt.nilB) {
                        String printString = "(0,0,0)";
                        System.out.println(printString);
                        fileWriter.println(printString);
                    } else {
                        String printString = "("+b.buildingNum+","+b.executedTime+","+b.totalTime+")";
                        System.out.println(printString);
                        fileWriter.println(printString);
                    }
                    String printThis = "("+minHeap.minHeap[1].buildingNum+","+(globalCounter)+")";
                    System.out.println(printThis);
                    risingCity.fileWriter.println(printThis);
                    rbt.delete1(minHeap.minHeap[1].buildingNum);
                    minHeap.removeMin();
                    Iterator tr = risingCity.pendingBuildingInsert.iterator();
                    while(tr.hasNext()) {
                        building b2 = (building) tr.next();
                        rbt.insert(b2);
                        minHeap.insert(b2);
                    }
                    risingCity.pendingBuildingInsert.clear();
                    printCommand = false;

                } else { //normal print of a building
                    int buildingNum = Integer.parseInt(line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim());
                    building b = rbt.findNode(buildingNum);
                    if (b == rbt.nilB) {
                        String printString = "(0,0,0)";
                        System.out.println(printString);
                        fileWriter.println(printString);
                    } else {
                        String printString = "("+b.buildingNum+","+b.executedTime+","+b.totalTime+")";
                        System.out.println(printString);
                        fileWriter.println(printString);
                    }
                }

            }

        }

        //Finished reading in all the lines. Continue working until heap is empty
        while (!(minHeap.checkEmpty())) {
            minHeap.buildTillCompletion(globalCounter,rbt);
            globalCounter = globalCounter + 1;

        }

        System.out.println("Finishing time for the city is "+globalCounter);
        fileWriter.close();
    }

}
