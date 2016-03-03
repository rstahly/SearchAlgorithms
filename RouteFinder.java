import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Rachel Feddersen
 *
 * @since Date Started: 2/13/2016 - Date Finished: 2/24/2016
 *
 * The RouteFinder class that creates the routeFinder and performs the searches through the map
 * It reads the file and sets the variables related to the route
 */
public class RouteFinder {

    private int routeNodes;
    private int numberOfLines;
    private String startVillage;
    private String endVillage;
    private TreeMap<String,TreeMap<String,Double>> mappedRoute = new TreeMap<>();
    private LinkedList<String> path = new LinkedList<>();
    private List<String> nodes = new ArrayList<>();
    private List<String> frontier = new ArrayList<>();
    private List<String> explored = new ArrayList<>();
    private TreeMap<String,String> pathFrom = new TreeMap<>();
    private File pickedFile;

    /**
     * The RouteFinder constructor that builds the routeFinder
     */
    public RouteFinder (File filename) {
        setPickedFile(filename);
    }

    /**
     * This method attempts to open the file
     * @param filename - The location of the file
     * @return - Return the new scanner with the opened file
     */
    private Scanner openFile(File filename) {
        // Open a scanner
        Scanner inputFile = null;

        // Try to create a new Scanner
        try {
            inputFile = new Scanner(filename);
            // If the file cannot be found
        } catch (FileNotFoundException e) {
            System.exit(0);
            e.printStackTrace();
        }

        return inputFile;
    }

    /**
     * This method attempts to read the file
     * @param filename - The location of the file
     * @return - Return the error if the file does not open
     */
    public boolean readFile(File filename) {
        // Open a scanner
        Scanner inputFile;
        // Create a new scanner
        inputFile = openFile(filename);

        // Keep track of the number of lines in the file for later use and set other variables
        int lines = 0;
        boolean fileError = false;

        // Try to read the file
        try {
            // Read the number of villages and set the variable
            setNumberOfNodes(inputFile.nextInt());
            inputFile.nextLine();

            // While there are more lines in the file,
            while(inputFile.hasNextLine()) {
                // Call setNodes method for retrieving list of all villages in file
                setNodes((inputFile.nextLine()).split("\\t", -1));
                lines += 1;
            }

            // Call the method to set the number of lines in the file
            setNumberOfLines(lines);

            // Close the file
            inputFile.close();

            // Call this method for creating the treeMap based on the file's information
            createTreeMap(filename);
            // If the file does not match what the file normally would be like
        } catch (InputMismatchException e) {
            // Set the error to true
            fileError = true;
        }

        return fileError;
    }

    /**
     * The method for creating the TreeMap based on the information in the file
     * @param filename - The file that was chosen by the user
     */
    private void createTreeMap(File filename) {
        // Open a scanner
        Scanner inputFile;
        String currentNode;
        String[] values;

        // While the TreeMap is not larger than the number of villages found eariler
        while(mappedRoute.size() != getNumberOfNodes()) {
            // Create a new scanner
            inputFile = openFile(filename);
            inputFile.nextLine();

            // Set the currentNode to be searched as the next one in the nodes list
            currentNode = nodes.get(mappedRoute.size());

            // If the TreeMap does not already contains the node we are looking at
            if (!mappedRoute.containsKey(currentNode)) {
                // Add the currentNode to the TreeMap
                mappedRoute.put(currentNode, new TreeMap<>(Collections.reverseOrder()));
                // While the file has a next line
                while(inputFile.hasNext()) {
                    // Set a String array to the values from the first line
                    values = (inputFile.nextLine()).split("\\t", -1);
                    // If first value in line equals currentNode and TreeMap does not have the second value
                    if (values[0].equals(currentNode) && !mappedRoute.get(values[0]).containsKey(values[1])) {
                        // Put the second and third values in the map under the first value
                        mappedRoute.get(values[0]).put(values[1], Double.parseDouble(values[2]));
                        // If second value in line equals currentNode and TreeMap does not have the first value
                    } else if (values[1].equals(currentNode) && !mappedRoute.get(values[1]).containsKey(values[0])) {
                        // Put the first and third values in the map under the second value
                        mappedRoute.get(values[1]).put(values[0], Double.parseDouble(values[2]));
                    }
                }
            }

            // Close the file
            inputFile.close();
        }
    }

    /**
     * The method for setting the number of villages
     * @param r - The number of villages
     */
    private void setNumberOfNodes(int r) {
        routeNodes = r;
    }

    /**
     * The method for retrieving the number of villages
     * @return - Return the routeNodes variable
     */
    public int getNumberOfNodes() {
        return routeNodes;
    }

    /**
     * The method for setting the mappedRoute of the villages
     * @param m - The mappedRoute received
     */
    private void setMappedRoute(TreeMap<String, TreeMap<String,Double>> m) {
        mappedRoute = m;
    }

    /**
     * The method for retrieving the mappedRoute of the villages
     * @return - Return the TreeMap with the data in it
     */
    public TreeMap<String,TreeMap<String,Double>> getMappedRoute() {
        return mappedRoute;
    }

    /**
     * The method for setting the villages in the List nodes
     * @param values - The String array that contains the line from the file
     */
    private void setNodes(String[] values) {
        // If the first item in the line is not in the List
        if (!nodes.contains(values[0])) {
            nodes.add(values[0]);
        }

        // If the second item in the line is not in the List
        if (!nodes.contains(values[1])) {
            nodes.add(values[1]);
        }
    }

    /**
     * The method for retrieving the nodes List
     * @return - Return the List of nodes
     */
    public List<String> getNodes() {
        return nodes;
    }

    /**
     * The method for setting the file picked by the user
     * @param f - The File chosen
     */
    private void setPickedFile(File f) {
        pickedFile = f;
    }

    /**
     * The method for retrieving the file picked by the user
     * @return - Return the chosen File
     */
    public File getPickedFile() {
        return pickedFile;
    }

    /**
     * The method for setting the number of lines in the File
     * @param n - The number of lines calculated
     */
    private void setNumberOfLines(int n) {
        numberOfLines = n;
    }

    /**
     * The method for retrieving the number of lines in the File
     * @return - Return the number of Lines
     */
    public int getNumberOfLines() {
        return numberOfLines;
    }

    /**
     * The method for setting the start village chosen by the user
     * @param start - The name of the village
     */
    private void setStartVillage(String start) {
        startVillage = start;
    }

    /**
     * The method for retrieving the start village chosen by the user
     * @return - Return the start village
     */
    public String getStartVillage() {
        return startVillage;
    }

    /**
     * The method for setting the end village chosen by the user
     * @param end - The name of the village
     */
    private void setEndVillage(String end) {
        endVillage = end;
    }

    /**
     * The method for retrieving the end village chosen by the user
     * @return - Return the end village
     */
    public String getEndVillage() {
        return endVillage;
    }

    /**
     * The method for retrieving the explored List so it can be used in the RouteFinderWindow
     * @return - Return the frontier List
     */
    public List getFrontier() {
        return frontier;
    }

    /**
     * The method for retrieving the explored List so it can be used in the RouteFinderWindow
     * @return - Return the explored List
     */
    public List getExplored() {
        return explored;
    }

    /**
     * The method for retrieving the path found from the start village to the end village
     * @return - Return the path
     */
    public LinkedList<String> getPath() {
        return path;
    }

    /**
     * The method for preparing the searches for finding the end village
     * @param s - The start village
     */
    public void startSearch(String s) {
        // Add the startVillage to the frontier and set the frontier in the RouteFinder class
        frontier.add(s);
    }

    /**
     * The method for performing the depthFirstSearch
     * @param m - The TreeMap containing the information from the file
     * @param s - The start village name
     * @param e - The end village name
     */
    public void depthFirstSearch(TreeMap<String,TreeMap<String,Double>> m, String s, String e) {
        // Set the mappedRoute, start village, and end village so they can be used later
        setMappedRoute(m);
        setStartVillage(s);
        setEndVillage(e);

        // Add the last village in the frontier to the explored List
        explored.add(frontier.get(frontier.size() - 1));

        // If this is the first village to be put in the path
        if (path.size() == 0) {
            // Add the village to the path
            path.add(explored.get(explored.size() - 1));
        } else {
            // If the villages are connected
            if (nodesConnected(explored.get(explored.size() - 1), path.getLast())) {
                // Add the village to the path
                path.add(explored.get(explored.size() - 1));
            } else {
                // While village on end of explored List is not connected to last village on path
                while (!nodesConnected(explored.get(explored.size() - 1), path.getLast())) {
                    // Remove the last village from the path
                    path.removeLast();
                }
                // If the villages are finally connected
                if (nodesConnected(explored.get(explored.size() - 1), path.getLast())) {
                    // Add the village to the path
                    path.add(explored.get(explored.size() - 1));
                }
            }
        }

        // Iterate through the currentNode in the explored List's children and add them to the frontier
        Set<Map.Entry<String, Double>> entrySet = mappedRoute.get(explored.get(explored.size() - 1)).entrySet();
        for (Map.Entry<String, Double> entry : entrySet) {
            if (!explored.contains(entry.getKey())) {
                frontier.add(entry.getKey());
            }
        }

        // For the number of villages in the frontier
        for (int i = frontier.size() - 1; i >= 0; i--) {
            // If the village in the frontier is already in the explored, remove it
            if (frontier.get(i).equals(explored.get(explored.size() - 1))) {
                frontier.remove(i);
            }
        }
    }

    /**
     * The method for performing the breadthFirstSearch
     * @param m - The TreeMap containing the information from the file
     * @param s - The start village name
     * @param e - The end village name
     */
    public void breadthFirstSearch(TreeMap<String,TreeMap<String,Double>> m, String s, String e) {
        // Set the mappedRoute, start village, and end village so they can be used later
        setMappedRoute(m);
        setStartVillage(s);
        setEndVillage(e);

        // Iterate through the first node's children
        Set<Map.Entry<String, Double>> entrySet = mappedRoute.get(frontier.get(0)).entrySet();
        for (Map.Entry<String, Double> entry : entrySet) {
            // If the child is not already in the explored List
            if (!explored.contains(entry.getKey())) {
                frontier.add(entry.getKey());
            }
            // If the child is not already in the pathFrom TreeMap that keeps track path from start village
            if (!pathFrom.containsKey(entry.getKey())) {
                pathFrom.put(entry.getKey(), frontier.get(0));
            }
        }

        // Add the current Node to the explored List
        explored.add(frontier.get(0));

        // For the number of villages in the frontier
        for (int i = frontier.size()-1; i >= 0; i--) {
            // If the village in the frontier is already in the explored, remove it
            if (frontier.get(i).equals(frontier.get(0))) {
                frontier.remove(i);
            }
        }

        // If the end village is found and the search is complete
        if (solved()) {
            // Call the createPath method for putting the breadthFirstSearch's path in order
            createPath(pathFrom);
        }
    }

    /**
     * The method for determining if two nodes have a connection
     * @param firstNode - The first village to be checked
     * @param secondNode - The second village to be checked
     * @return - Return true or false depending on whether they are connected or not
     */
    public boolean nodesConnected(String firstNode, String secondNode) {
        return mappedRoute.get(firstNode).containsKey(secondNode);
    }

    /**
     * The method for creating the path for the breadthFirstSearch
     * @param pathFrom - The TreeMap of the villages and from what village they were explored
     */
    public void createPath(TreeMap pathFrom) {
        // Set the currentNode equal to the last village
        String currentNode = getEndVillage();

        // While the currentNode does not equal the start village
        while(!currentNode.equals(getStartVillage())) {
            // Add the currentNode to the beginning of the path and set the new currentNode
            path.addFirst(currentNode);
            currentNode = String.valueOf(pathFrom.get(currentNode));
        }

        // Add the last node to the beginning of the path
        path.addFirst(currentNode);
    }

    /**
     * The method for calculating the cost of the path that was found through the map
     * @return - Return the cost
     */
    public double calculatePath() {
        double pathCost = 0.00;

        // For the number of nodes in the path
        for (int i = 0; i < path.size() - 1; i++) {
            // Get the cost between the nodes and add it to the pathCost
            pathCost += mappedRoute.get(path.get(i)).get(path.get(i+1));
        }

        return pathCost;
    }

    /**
     * The method for creating the table of the distances between all of the villages
     * @return - Return the rows created by the file
     */
    public Object[][] createDistanceTable() {
        // Initialize the variables
        int place = 0;
        String values[];
        Object rowData[][] = new Object[getNumberOfLines()][3];

        // Open a scanner
        Scanner inputFile;

        // Create a new scanner
        inputFile = openFile(getPickedFile());

        inputFile.nextLine();

        // While there is another line in the file
        while(inputFile.hasNextLine()) {
            // Set a String array to the values from the first line
            values = (inputFile.nextLine()).split("\\t", -1);

            // Copy the values String array to the current location in the rowData Object
            System.arraycopy(values, 0, rowData[place], 0, 3);

            // Increment the place count
            place += 1;
        }

        // Close the file
        inputFile.close();

        return rowData;
    }

    /**
     * The method the determines if the end village has been found
     * @return - Return true or false
     */
    public boolean solved() {
        return explored.contains(getEndVillage());
    }
}

