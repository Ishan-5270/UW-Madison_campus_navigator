import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.*;

public class BackendDeveloperTests {

    /**
     * This tester tests for the loadData function of the CampusNavigatorBD class
     *
     * @throws FileNotFoundException
     */
    @Test
    public void testLoadData() throws FileNotFoundException {
        GraphReaderAEInterface<String, Double> mapReader = new GraphReaderAEBD<>();
        GraphReaderDWInterface graphReader = new GraphReaderDWBD();
        CampusNavigatorBD navigator = new CampusNavigatorBD(mapReader, graphReader);
        navigator.loadData("data/testdata.dot");
        // these nodes and edges are the same as the ones in the GraphReaderDWBD class
        assertTrue(mapReader.containsNode("A"));
        assertTrue(mapReader.containsNode("B"));
        assertTrue(mapReader.containsNode("C"));
        assertTrue(mapReader.containsNode("D"));
        assertTrue(mapReader.containsEdge("A", "B"));
        assertTrue(mapReader.containsEdge("A", "C"));
        assertTrue(mapReader.containsEdge("B", "C"));
        assertTrue(mapReader.containsEdge("C", "D"));
    }

    /**
     * This tester tests the addLocation function of the CampusNavigatorBD class
     * Nodes C and A are already present in the class through the placeholder
     * implementation of GraphReaderDWBD
     *
     * @throws FileNotFoundException
     */
    @Test
    public void testAddLocation() throws FileNotFoundException {
        GraphReaderAEInterface<String, Double> mapReader = new GraphReaderAEBD<>();
        GraphReaderDWInterface graphReader = new GraphReaderDWBD();
        CampusNavigatorBD navigator = new CampusNavigatorBD(mapReader, graphReader);
        ArrayList<String> words = new ArrayList<>(Arrays.asList("F", "C", "5", "A", "6"));
        // F is the new node to be inserted
        navigator.loadData("data/testdata.dot");
        navigator.addLocation(words);
        assertTrue(mapReader.containsNode("F"));
        assertTrue(mapReader.containsNode("C"));
        assertTrue(mapReader.containsNode("A"));
        assertTrue(mapReader.containsEdge("F", "C"));
        assertTrue(mapReader.containsEdge("F", "A"));
    }

    /**
     * this method tests if an exception is thrown if the user tries to add a
     * location to the map that already exists
     */
    @Test
    public void testAddLocationDuplicate() {
        GraphReaderAEInterface<String, Double> mapReader = new GraphReaderAEBD<>();
        GraphReaderDWInterface graphReader = new GraphReaderDWBD();
        CampusNavigatorBD navigator = new CampusNavigatorBD(mapReader, graphReader);
        ArrayList<String> words = new ArrayList<>(Arrays.asList("A", "B", "1"));
        navigator.addLocation(words);
        ArrayList<String> wordsdup = new ArrayList<>(Arrays.asList("A", "B", "1"));
        // Use assertThrows to check for the expected exception
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class,
                () -> navigator.addLocation(wordsdup));
        // Check that the exception message is correct
        assertEquals("Location already exists.", thrownException.getMessage());
    }

    /**
     * this method tests the removelocation function of the CampusNavigatorBD
     * function
     */
    @Test
    public void testRemoveLocation() {
        GraphReaderAEInterface<String, Double> mapReader = new GraphReaderAEBD<>();
        GraphReaderDWInterface graphReader = new GraphReaderDWBD();
        CampusNavigatorBD navigator = new CampusNavigatorBD(mapReader, graphReader);
        // Adds A to the map
        mapReader.insertNode("A");
        mapReader.insertNode("B");
        mapReader.insertNode("C");
        mapReader.insertNode("D");
        mapReader.insertEdge("A", "B", 10.0);
        mapReader.insertEdge("B", "A", 5.0);
        mapReader.insertEdge("B", "C", 1.0);
        mapReader.insertEdge("C", "A", 5.0);
        mapReader.insertEdge("D", "B", 3.0);
        // Since I modified the Remove function of the Backend so that it works with the rest of the mcode
        // the older placeholder test now returns an IndexOutofBoundsException
        // This is because the getAllPaths method returns an empty list in the placeholder implemementation
        ArrayList<String> toberemoved = new ArrayList<>(Arrays.asList("A", "B", "C"));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            navigator.removeLocation(toberemoved);
        });
    }

    /**
     * This method tests the ReturnMultipleLocation function of the
     * CampusNavigatorBD. This method makes
     * use of the Algorithm Engineers GraphReaderAEBD findAllpaths, getPathCost and
     * getPathString methods.
     */
    @Test
    public void testReturnMultipleLocation() throws FileNotFoundException {
        GraphReaderAEInterface<String, Double> mapReader = new GraphReaderAEBD<>();
        GraphReaderDWInterface graphReader = new GraphReaderDWBD();
        CampusNavigatorBD navigator = new CampusNavigatorBD(mapReader, graphReader);
        StringBuilder sb = new StringBuilder();
        navigator.loadData("data/testdata.dot");
        List<String> destinations = new ArrayList<>(Arrays.asList("B", "C", "D"));
        String result = navigator.returnMultipleLocation("A", destinations);
        // Since this method is not implemented yet the returnMultipleLocation function
        // returns No path found
        // to the respective destination nodes just like expected. The findAllPaths
        // method of the GraphReaderAEBD
        // returns an empty arraylist in the placeholder implementation
        sb.append("No path to B found.\nNo path to C found.\nNo path to D found.\n");
        assertEquals(sb.toString(),
                result);
    }

    /**
     * This test is responsible checking the frontends add location functionality
     */
    @Test
    public void CodeReviewofFrontendDeveloper1() {

                String insertLocationString = "i\n1315 W Dayton Street\nUnion South, 1\n";
                String quitcommand = "e\n";
                // tests the insert location
                TextUITester tester = new TextUITester(
                        insertLocationString + quitcommand);
                Scanner in = new Scanner(System.in);
                GraphReaderAE<String, Double> graphviewer = new GraphReaderAE();
                GraphReaderDW graphReader = new GraphReaderDW();
                CampusNavigatorBD navigator = new CampusNavigatorBD(graphviewer, graphReader);
                CampusNavigatorFD frontend = new CampusNavigatorFD(in, navigator, graphReader);
                frontend.runCommandLoop(); // it runs the program loop of the frontend
                String output = tester.checkOutput();
                assertTrue(output.contains("What is the name of the location that you would like to add?")); // checks that
        // the insert text is present
                // the file loaded 15 items
                int nodeCount = graphviewer.getNodeCount();
                assertEquals(16, nodeCount); // there were 15 data items in the file before so now it should be
                // 16
                // tests that the graph contains the new addition in the graph
                boolean newlocation = graphviewer.containsNode("1315 W Dayton Street");
                assertEquals(true, newlocation);
        }

    /**
     * This test is responsible for checking the deletion segment of the frontends code
     */
    @Test
    public void CodeReviewofFrontendDeveloper2() {

                String removeLocationString = "d\nComputer Sciences\n";
                String quit = "e\n";
                // tests the insert location
                TextUITester tester = new TextUITester(
                        removeLocationString + quit);
                Scanner in = new Scanner(System.in);
                GraphReaderAE<String, Double> graph = new GraphReaderAE();
                GraphReaderDW graphReader = new GraphReaderDW();
                CampusNavigatorBD backend = new CampusNavigatorBD(graph, graphReader);
                CampusNavigatorFD frontend = new CampusNavigatorFD(in, backend, graphReader);
                frontend.runCommandLoop(); // run program loop
                String output = tester.checkOutput();
                assertTrue(output.contains("Please select a location(s) that you would like to remove"));// checks that the remove text is ptresent
                int nodeCount = graph.getNodeCount();
                assertEquals(14, nodeCount); // 15 data items before reduced to 14
                // tests that the graph does not contain the removed node
                boolean delLocation = graph.containsNode("Computer Sciences");
                assertEquals(false, delLocation); // returns false  because the location is no longer present
        }

    /**
     * The first integration test checks for a) the initial setup of the map
     * the second part b) checks if the application is ending the way it should
     */
    @Test
    public void Integrationtest1() {
        // this test checks for the nodes and edges in the graphs before any additions or deletions are made
        try {
            // create objects
            TextUITester tester = new TextUITester(
                    "e\n");
            Scanner in = new Scanner(System.in);
            GraphReaderAE<String, Double> graph = new GraphReaderAE();
            GraphReaderDW graphReader = new GraphReaderDW();
            CampusNavigatorBD backend = new CampusNavigatorBD(graph, graphReader);
            CampusNavigatorFD frontend = new CampusNavigatorFD(in, backend, graphReader);
            frontend.runCommandLoop(); // run program loop
            String output = tester.checkOutput();
            assertEquals(15, graph.getNodeCount());
            assertEquals(19, graph.getEdgeCount() / 2); // because it is an undirected graph
        } catch (Exception e) {
            assertTrue(false, "Test false");
        }

        // This segment of the test is checking for the exit part of the application
        try {
            TextUITester tester = new TextUITester("e\n");
            Scanner in = new Scanner(System.in);
            GraphReaderAE<String, Double> graph = new GraphReaderAE();
            GraphReaderDW graphReader = new GraphReaderDW();
            CampusNavigatorBD backend = new CampusNavigatorBD(graph, graphReader);
            CampusNavigatorFD frontend = new CampusNavigatorFD(in, backend, graphReader);
            frontend.runCommandLoop(); // run program loop
            String output = tester.checkOutput();

            assertEquals(true, output.contains("Welcome to the UW-Madison Campus Navigator App!"));
            assertEquals(true, output.contains("Thank you for using the UW-Madison Campus Navigator!"));

        } catch (Exception e) {
            assertTrue(false, "Test failed");
        }
    }

    /**
     * Integration Test 2. Test removing location and finding shortest path.
     */
    @Test
    public void Integrationtest2() {
        // this segment of the test is checking what happens when the user tries to remove a single location twice
        try {
            TextUITester tester = new TextUITester(
                    "d\n Union South, Computer Sciences\nd\nComputer Sciences\nMemorial Union\ne\n");
            // run program
            Scanner in = new Scanner(System.in);
            GraphReaderAE<String, Double> graphviewer = new GraphReaderAE();
            GraphReaderDW graphReader = new GraphReaderDW();
            CampusNavigatorBD navigator = new CampusNavigatorBD(graphviewer, graphReader);
            CampusNavigatorFD frontend = new CampusNavigatorFD(in, navigator, graphReader);
            frontend.runCommandLoop();
            String output = tester.checkOutput();
            // we cannot remove the Computer Science building twice
            assertEquals(true, output.contains("Please select valid locations only"));
        } catch (Exception e) {
            assertTrue(false, "Test failed");
        }

        // this segment of the test is checking if the shortest location is correctly displayed with no additional middle locations
        try {
            TextUITester tester = new TextUITester(
                    "f\nUnion South\n Red Gym\n\ne\n");
            Scanner in = new Scanner(System.in);
            GraphReaderAE<String, Double> graphviewer = new GraphReaderAE();
            GraphReaderDW graphReader = new GraphReaderDW();
            CampusNavigatorBD navigator = new CampusNavigatorBD(graphviewer, graphReader);
            CampusNavigatorFD frontend = new CampusNavigatorFD(in, navigator, graphReader);
            frontend.runCommandLoop();
            String output = tester.checkOutput();
            assertEquals(true, output.contains("Union South->Bascom Hall->Library Mall->Red Gym (Cost: 0.8500000000000001)"));
        } catch (Exception e) {
            assertTrue(false, "Test failed");
        }
    }
}