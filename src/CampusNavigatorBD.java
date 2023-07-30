// --== CS400 Spring 2023 File Header Information ==--
// Name: Ishan Rungta
// Email: imrungta@wisc.edu
// Team: Blue
// TA: Rahul
// Lecturer: Florian
// Notes to Grader: None

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.text.html.HTMLDocument.Iterator;

/**
 * Main class
 */
public class CampusNavigatorBD implements CampusNavigatorBDInterface {

    private GraphReaderAEInterface<String, Double> mapReader;
    private GraphReaderDWInterface graphReader;
    // private Map<NodeType, List<NodeType>> graph;

    public CampusNavigatorBD(GraphReaderAEInterface<String, Double> mapReader, GraphReaderDWInterface graphReader) {
        this.mapReader = mapReader;
        this.graphReader = graphReader;
    }

    /**
     * This method loads the dot files from the Data Wragler and passes the vertices
     * and edges to the Algorithm Engineer
     *
     * @param filename the file to be loaded
     * @throws FileNotFoundException if the file to be loaded is not found
     */
    @Override
    public void loadData(String filename) throws FileNotFoundException {
        // Load data from the given filename using the graphReader
        graphReader.readDOTfile(filename);

        // Get the vertices and edges from the GraphReaderDWInterface implementation
        List<String> vertices = graphReader.getVertices();
        List<String> edges = graphReader.getEdges();

        // Loop through the vertices and add them to the graph
        for (String vertex : vertices) {
            mapReader.insertNode(vertex);
        }

        // Loop through the edges, parse the string into its components, and add the
        // edge to the graph
        for (String edge : edges) {
            String[] edgeComponents = edge.split(",");
            String pred = edgeComponents[0];
            String succ = edgeComponents[1];
            double weight = Double.parseDouble(edgeComponents[2]);
            mapReader.insertEdge(pred, succ, weight);
            mapReader.insertEdge(succ, pred, weight);
        }
    }

    /**
     * Helps the user add a location. Input has to be of the type (Location,
     * Destination, Weight)
     * The destination and the weight signify the distance from the added location
     * to already existing locations
     * on the map
     *
     * @param words - the arraylist to containing (Location, Destination, Weight)
     */
    @Override
    public void addLocation(ArrayList<String> words) {
        // Add the location to the map, checks if the location already exists
        // Check if the location already exists
        // Location should be added in this format - (Location, Destination, Weight)
        String location = words.get(0);
        if (mapReader.containsNode(location)) {
            throw new IllegalArgumentException("Location already exists.");
        }
        // Add the location to the map
        mapReader.insertNode(location);
        // Add the edges to the map
        for (int i = 1; i < words.size(); i += 2) {
            String destination = words.get(i);
            double weight = Double.parseDouble(words.get(i + 1));
            mapReader.insertEdge(location, destination, weight);
            mapReader.insertEdge(destination, location, weight);

        }
    }

    /**
     * This method removes an already existing location from the map
     *
     * @param toberemoved The location to be deleted
     */
    @Override
    public void removeLocation(ArrayList<String> toberemoved) {
        // Check if the location exists
        for (String s : toberemoved) {
            System.out.println(s);
            if (!mapReader.containsNode(s)) {
                throw new NoSuchElementException("Location does not exist.");
            }

            // Remove the location from the map
            // Remove the edges
            List<GraphReaderAE<String, Double>.Edge> edges = mapReader.findAllPaths(s).get(0).node.edgesLeaving;
            List<GraphReaderAE<String, Double>.Edge> toRemove = new ArrayList<>();

            //collect edges to remove
            for (GraphReaderAE<String, Double>.Edge edge : edges) {
                toRemove.add(edge);
            }
            //remove edges
            for (GraphReaderAE<String, Double>.Edge edge : toRemove) {
                mapReader.removeEdge(edge.predecessor.data, edge.successor.data);
                mapReader.removeEdge(edge.successor.data, edge.predecessor.data);
            }
            // Remove the Node
            mapReader.removeNode(s);
        }
    }

    /**
     * This method makes use of the algorithm engineers implementation to return the
     * location from a starting point
     * to multiple places on the map
     *
     * @param start        the starting location
     * @param destinations all the places the user wants to go to
     * @return The shortest path and cost from the starting location to all the
     *         location user wants to visit
     */
    @Override
    public String returnMultipleLocation(String start, List<String> destinations) {
        // Find all paths from the start node
        List<DijkstraGraph<String, Double>.SearchNode> paths = mapReader.findAllPaths(start);

        // Find the shortest paths to each destination node
        StringBuilder sb = new StringBuilder();
        for (String dest : destinations) {
            // Find the path with minimum cost to the destination node
            DijkstraGraph<String, Double>.SearchNode shortestPath = null;
            double minCost = Double.POSITIVE_INFINITY;
            for (DijkstraGraph<String, Double>.SearchNode path : paths) {
                if (path.node.data.equals(dest) && mapReader.getPathCost(path) < minCost) {
                    shortestPath = path;
                    minCost = mapReader.getPathCost(path);
                }
            }
            // If a path exists to the destination, append it to the result string
            if (shortestPath != null) {
                sb.append("Shortest path to ").append(dest).append(": ");
                sb.append(mapReader.getPathString(shortestPath)).append(" (Cost: ").append(minCost).append(")\n");
            } else {
                sb.append("No path to ").append(dest).append(" found.\n");
            }
        }
        return sb.toString();
    }

}
