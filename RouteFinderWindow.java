import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import javax.swing.*;

/**
 * @author Rachel Feddersen
 *
 * @since Date Started: 2/13/2016 - Date Finished: 2/24/2016
 *
 * This is the main class for calling the RouteFinder classes and its methods.
 */

public class RouteFinderWindow extends JFrame {
    private File file;
    private RouteFinder routeFinder;
    private String searchType;
    private JTextArea searchResults;
    private TreeMap<String,TreeMap<String,Double>> mappedRoute = new TreeMap<>();
    private String startVillage;
    private String endVillage;
    private JMenuItem fileMenuItem;
    private JMenuItem exitMenuItem;
    private JMenuItem depthFirstSearch;
    private JMenuItem breadthFirstSearch;
    private JMenuItem findPath;
    private JMenuItem allDistances;

    /**
     * Create a new RouteFinderWindow by calling the constructor in the RouteFinderWindow class
     */

    public static void main(String[] args) {
        new RouteFinderWindow();
    }

    /**
     * This is the constructor for the RouteFinderWindow. It sets up the window and adds the menu bars
     * and the panels. It is called from the RouteFinderWindow class
     */
    public RouteFinderWindow(){
        int windowWidth = 605;
        int windowHeight = 605;

        // Set the title and close operation
        setTitle("Route Finder");

        // Set the size of the GUI
        setSize(new Dimension(windowWidth, windowHeight));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                // Call this method to create the end message dialog
                createExitMessage();
            }
        });

        // Create the menu bar and put it in the window
        JMenuBar bar = buildRouteBar();
        setJMenuBar(bar);

        // Set the menu options to be disabled
        findPath.setEnabled(false);
        depthFirstSearch.setEnabled(false);
        breadthFirstSearch.setEnabled(false);
        allDistances.setEnabled(false);

        // Create the text area that will be in the JOptionPane
        searchResults = new JTextArea();
        searchResults.setText(setMainMessage());
        searchResults.append("Please select a village map file to begin.");
        searchResults.setLineWrap(true);

        // Create the JScrollPane and the JPanel for holding the JTextArea and making it more user friendly
        JScrollPane scroll = new JScrollPane(searchResults);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(600, 600));
        JPanel container = new JPanel();
        container.add(scroll);
        add(container);

        // Show the window
        pack();
        setVisible(true);
    }

    /**
     * The method sets up the menu bar that has the different menu options like file, map, search
     * types, and village distances
     * @return - Return the assembled menu bar
     */
    private JMenuBar buildRouteBar(){
        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create and add the menu options for the menu bar
        JMenu fileMenu = buildFileMenu();
        JMenu routeMenu = buildRouteMenu();
        JMenu searchMenu = buildSearchMenu();
        JMenu distanceMenu = buildDistanceMenu();
        menuBar.add(fileMenu);
        menuBar.add(routeMenu);
        menuBar.add(searchMenu);
        menuBar.add(distanceMenu);

        return menuBar;
    }

    /**
     * The method for creating the options for the file menu item
     * @return - The completed fileMenu
     */
    private JMenu buildFileMenu(){
        // Create
        JMenu fileMenu = new JMenu("File");

        // Create the menu items
        findPath = new JMenuItem("Find Path");
        exitMenuItem = new JMenuItem("Exit");

        // Add these menu items into fileMenu
        fileMenu.add(findPath);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        // Hook up the menu items with the listener
        MyListener listener = new MyListener();
        findPath.addActionListener(listener);
        exitMenuItem.addActionListener(listener);

        return fileMenu;
    }

    /**
     * The method for creating the options for the map menu item
     * @return - The completed mapMenu
     */
    private JMenu buildRouteMenu(){
        // Create
        JMenu routeMenu = new JMenu("Map");

        // Create the menu items
        fileMenuItem = new JMenuItem("Load file");

        // Add these menu items into routeMenu
        routeMenu.add(fileMenuItem);

        // Hook up the menu items with the listener
        MyListener listener = new MyListener();
        fileMenuItem.addActionListener(listener);

        return routeMenu;
    }

    /**
     * The method for creating the options for the search types menu item
     * @return - The completed searchMenu
     */
    private JMenu buildSearchMenu(){
        // Create
        JMenu searchMenu = new JMenu("Search Types");

        // Create the menu items
        depthFirstSearch = new JMenuItem("Depth First Search");
        breadthFirstSearch = new JMenuItem("Breadth First Search");

        // Add these menu items into searchMenu
        searchMenu.add(depthFirstSearch);
        searchMenu.add(breadthFirstSearch);

        // Hook up the menu items with the listener
        MyListener listener = new MyListener();
        depthFirstSearch.addActionListener(listener);
        breadthFirstSearch.addActionListener(listener);

        return searchMenu;
    }

    /**
     * The method for creating the options for the distance menu item
     * @return - The completed distanceMenu
     */
    private JMenu buildDistanceMenu(){
        // Create
        JMenu distanceMenu = new JMenu("Village Distances");

        // Create the menu items
        allDistances = new JMenuItem("All Village Distances");

        // Add these menu items into searchMenu
        distanceMenu.add(allDistances);

        // Hook up the menu items with the listener
        MyListener listener = new MyListener();
        allDistances.addActionListener(listener);

        return distanceMenu;
    }

    /**
     * This is the private class for the action listener
     * @author Rachel Feddersen
     * It has the actionPerformed method inside of it so it can listen to what the user is doing
     * and respond accordingly
     */
    private class MyListener implements ActionListener{
        /**
         * The actionPerformed method that gets the action that was performed and does the
         * corresponding action
         */
        public void actionPerformed(ActionEvent e){
            boolean fileError = false;
            // If the user clicks on the exitMenuItem
            if (e.getSource() == exitMenuItem) {
                // Call this method to create the end message dialog
                createExitMessage();
                // If the user clicks on the fileMenuItem
            } else if (e.getSource() == fileMenuItem) {
                fileError = getFileInformation();
                // If the user clicks on the depthFirstSearch
            } else if (e.getSource() == depthFirstSearch) {
                // Call the pickVillages method for allowing the user to pick the start and end villages
                pickVillages();
                // Set the search type to what the user picked
                setSearchType("depthFirstSearch");
                // If the user clicks on the breadthFirstSearch
            } else if (e.getSource() == breadthFirstSearch) {
                // Call the pickVillages method for allowing the user to pick the start and end villages
                pickVillages();
                // Set the search type to what the user picked
                setSearchType("breadthFirstSearch");
                // If the user clicks on the All Distances option
            } else if (e.getSource() == allDistances) {
                // Call this method for creating the table with the villages and distances
                createTableDisplay();
            // If the user clicks on the findPath option
            } else if (e.getSource() == findPath) {
                // Call the findThePath method to perform the search
                findThePath();

                // Call the endSearch method to reset some of the variables
                endSearch();
            }

            // Call method to enable options
            setEnabledOptions(e, fileError);
        }
    }

    /**
     * The method for getting the file that the user choose and calling methods to retrieve
     * the files information
     * @return - Return the file error
     */
    public boolean getFileInformation() {
        // Show a dialog to allow the user to choose files
        JFileChooser fc = new JFileChooser("./");  //set starting point
        int status = fc.showOpenDialog(null);
        boolean fileError = false;
        // If the user actually chose a file
        if (status == JFileChooser.APPROVE_OPTION){
            // Get the selected file
            file = fc.getSelectedFile();

            // Create a new routeFinder class and get the TreeMap
            routeFinder = new RouteFinder(file);
            fileError = routeFinder.readFile(file);

            // If there was not an error with the file
            if (!fileError) {
                mappedRoute = routeFinder.getMappedRoute();
            } else {
                searchResults.setText("The file chosen does not contain the correct information. Please choose a new one.");
                // Disable the options that might have been enabled
                depthFirstSearch.setEnabled(false);
                breadthFirstSearch.setEnabled(false);
                allDistances.setEnabled(false);
            }
        }

        return fileError;
    }

    /**
     * The method for allowing the user to pick the start and end villages for the search
     */
    public void pickVillages() {
        // Set the list of villages the user has to pick from
        List<String> nodes = routeFinder.getNodes();

        // Call the setStartVillage method to make the user pick a start village
        startVillage = pickStartVillage(nodes);

        // Remove the startVillage node from the list
        nodes.remove(startVillage);

        // Call the setEndVillage method to make the usre pick a start village
        endVillage = pickEndVillage(nodes);
    }

    /**
     * The method for making the user pick a start village
     * @param nodes - The List of all of the villages the user can pick from
     * @return - The village at which to start the search
     */
    public String pickStartVillage(List<String> nodes) {
        String startV = null;
        String errorMessage = "";

        // While the user has not chosen a village, continue to make this request pop up
        while (startV == null || !nodes.contains(startV.toUpperCase())) {

            startV = JOptionPane.showInputDialog(
                    null,
                    "Start Village: " + errorMessage,
                    "Pick Start Village",
                    JOptionPane.QUESTION_MESSAGE
            ); // Initial choice

            // Set the error message
            errorMessage = "That village is not valid.";
        }

        return startV.toUpperCase();
    }

    /**
     * The method for making the user pick an end village
     * @param nodes - The List of all of the villages the user can pick from
     * @return - The village at which to end the search
     */
    public String pickEndVillage(List<String> nodes) {
        String endV = null;
        String errorMessage = "";

        // While the user has not chosen a village, continue to make this request pop up
        while (endV == null || !nodes.contains(endV.toUpperCase())) {
            endV = JOptionPane.showInputDialog(
                    null,
                    "End Village: " + errorMessage,
                    "Pick End Village",
                    JOptionPane.QUESTION_MESSAGE
            ); // Initial choice

            // Set the error message
            errorMessage = "That village is not valid or has already been picked.";
        }

        return endV.toUpperCase();
    }

    /**
     * The method for creating the JTable with the village and distance information
     */
    public void createTableDisplay() {
        // Create the variables for the JTable rows and columns
        Object columnData[] = { "1st Village", "2nd Village", "Distance"};
        Object[][] rowData = routeFinder.createDistanceTable();

        // Create the table and set table's features
        JTable table = new JTable(rowData, columnData);
        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);

        // Open table in message dialog
        JOptionPane.showMessageDialog(null, new JScrollPane(table));
    }

    /**
     * The method that calls the methods for performing the breadthFirst and depthFirst search
     */
    public void findThePath() {
        resetSearch();

        // Call this method for starting the search
        routeFinder.startSearch(startVillage);

        // Append the results to the string
        searchResults.append(toText());
        // If the user choose a depthFirstSearch
        if (getSearchType().equals("depthFirstSearch")) {
            do {
                // Call depthFirstSearch method and append results
                routeFinder.depthFirstSearch(mappedRoute, startVillage, endVillage);
                searchResults.append(toText());
                // Do while the end village has not been found and not all of the nodes have been explored
            } while (!routeFinder.solved() && routeFinder.getExplored().size() != routeFinder.getNumberOfNodes());
            // If the user choose a breadthFirstSearch
        } else if (getSearchType().equals("breadthFirstSearch")) {
            do {
                // Call breadthFirstSearch method and append results
                routeFinder.breadthFirstSearch(mappedRoute, startVillage, endVillage);
                searchResults.append(toText());
                // Do while the end village has not been found and not all of the nodes have been explored
            } while (!routeFinder.solved() && routeFinder.getExplored().size() != routeFinder.getNumberOfNodes());
        }

        // If all of the nodes have been explored but no path was found
        if (routeFinder.getExplored().size() == routeFinder.getNumberOfNodes() && !routeFinder.solved()) {
            searchResults.append("No path can be found between the villages.");
        }
    }

    /**
     * The method for resetting some of the variables after a search has been performed
     */
    public void endSearch() {
        // When the end village has been found, disable most of the options and set the
        // file equal to null so the user will have to choose a new one
        depthFirstSearch.setEnabled(false);
        breadthFirstSearch.setEnabled(false);
        allDistances.setEnabled(false);
        findPath.setEnabled(false);
        file = null;

        searchResults.append("\n\nPlease choose a new map to do another search.");
    }

    /**
     * The method that re-enables the options the user has to pick from on the menu bar
     * @param e - The ActionEvent variable that can be used to determine what action just occured
     */
    public void setEnabledOptions(ActionEvent e, boolean fileError) {
        // If a file has been chosen and a search of distance option has not been chosen
        if (file != null && e.getSource() != depthFirstSearch && e.getSource() != breadthFirstSearch
            && e.getSource() != allDistances && !fileError) {
            // Enable the search and distance options but keep the findPath option disabled
            depthFirstSearch.setEnabled(true);
            breadthFirstSearch.setEnabled(true);
            allDistances.setEnabled(true);
            findPath.setEnabled(false);

            // Reset the JTextArea to blank
            resetSearch();

            // Set the JTextArea with a new message
            searchResults.setText(setMainMessage());
            searchResults.append("Please choose a type of search from the Search Types Menu\n" +
                    "or click on the distance menu to see a table of all distances.");
        // If a file has been chosen and a search type has been chosen
        } else if (file != null
        && (e.getSource() == depthFirstSearch || e.getSource() == breadthFirstSearch)
        && !routeFinder.solved()) {
            // Enable the findPath option
            findPath.setEnabled(true);

            // Reset the JTextArea to blank
            resetSearch();

            // Set the JTextArea with a new message
            searchResults.setText(setMainMessage());
            searchResults.append("Please choose the Find Path option in the File Menu to start the search.");
        }
    }

    /**
     * The method for clearing the JTextArea
     */
    public void resetSearch() {
        searchResults.setText("");
    }

    /**
     * The method for setting the type of search the user requested
     * @param type - The name of the search
     */
    public void setSearchType(String type) {
        searchType = type;
    }

    /**
     * The method for retrieving the search type
     * @return - Return the searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * The method for returning the main message that is frequently displayed in the JTextArea
     * @return - The String with the message
     */
    public String setMainMessage() {
        return "This is a program for performing search algorithms " +
                "on a user-picked village map.\n\n" +
                "There are two different kind of searches that can be performed:\n\n" +
                "1. Depth-First Search:\n\t" +
                "This search expands the deepest node in " +
                "the current frontier of the search tree.\n\t" +
                "In this program, once the goal node is the next to be explored on the frontier, the\n\t" +
                "search is complete. If a node has already been explored and is in the frontier, it\n\t" +
                "will be removed from the frontier such as to no be explored again.\n\n" +
                "2. Breadth-First Search:\n\t" +
                "This search expands the root node first, then all of the successors of the root\n\t" +
                "node are expanded next, then their successors, and so on. In this program, once\n\t" +
                "the goal node is the next to be explored on the frontier, the search is complete.\n\t" +
                "If a node has already been explored and is in the frontier, it will be removed from\n\t" +
                "the frontier such as to no be explored again.\n\n" +
                "To exit the program, click the Exit option under the File Menu.\n\n";
    }

    /**
     * The method creates the messageDialog for when the user exits the program
     */
    private void createExitMessage() {
        // Open table in message dialog
        JOptionPane.showMessageDialog(null,
                "You are now exiting the program. Thanks for using my search algorithm GUI!",
                "Exit Message",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    /**
     * The toText method that sets up the frontier and explored Lists to be printed
     * @return - Return the Lists in String form
     */
    public String toText() {

        String str = "";

        // Get the current explored and frontier Lists
        List<String> explored = routeFinder.getExplored();
        List<String> frontier = routeFinder.getFrontier();

        // If the search has not been complete yet
        if (!routeFinder.solved()) {
            // If the explored List has villages in it
            if (!explored.isEmpty()) {
                str += "Current Node: " + explored.get(explored.size()-1) + "\n";
            }

            boolean firstTime = true;
            str += "Explored: [";

            // For the number of villages in the explored List
            for (String s : explored) {
                if (!firstTime) {
                    str += ", ";
                }

                str += s;
                firstTime = false;
            }

            firstTime = true;

            str += "]\nFrontier: [";

            // For the number of villages in the frontier List
            for (String s : frontier) {
                if (!firstTime) {
                    str += ", ";
                }

                str += s;
                firstTime = false;
            }

            str += "]\n\n";
        }

        // If the search has been completed
        if (routeFinder.solved()) {
            // Create a string with the path and the calculated cost
            str += "Current Node: " + endVillage +
                    "\nPath Found: " + Arrays.toString(routeFinder.getPath().toArray()) +
                    "\nThe cost of the found path is: " + routeFinder.calculatePath();
        }

        return str;
    }
}
