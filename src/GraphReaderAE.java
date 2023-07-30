
// --== CS400 File Header Information ==--
// Name: Adam Rosler
// Email: arosler@wisc.edu
// Group and Team: Blue
// TA: Rahul
// Lecturer: Florian
// Notes to Grader: None

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// --== CS400 File Header Information ==--
// Name: Adam Rosler
// Email: arosler@wisc.edu
// Group and Team: Blue
// TA: Rahul
// Lecturer: Florian
// Notes to Grader: None

public class GraphReaderAE<NodeType, EdgeType extends Number>
        extends DijkstraGraph<NodeType, EdgeType>
        implements GraphReaderAEInterface<NodeType, EdgeType> {
    /**
     * Computes every single possible path from the given starting point. Will
     * return all of these paths in a BFS order
     *
     * @param start the data item in the starting node for the path
     * @throws NoSuchElementException if the given start data is not a valid node
     * @return list of all paths from the start location
     */
    public List<SearchNode> findAllPaths(NodeType start) throws NoSuchElementException {
        // contains the visited edges to avoid repeats
        List<Edge> visitedEdges = new ArrayList<Edge>();

        // grab the starting node
        Node startNode = nodes.get(start);

        // throw if the starting node is not valid
        if (startNode == null) {
            throw new NoSuchElementException("given start data is not a valid node");
        }
        // holds the list of all the paths
        List<SearchNode> paths = new ArrayList<SearchNode>();
        // holds the list of the results paths
        List<SearchNode> resultPaths = new ArrayList<SearchNode>();

        // create the starting path
        SearchNode startPath = new SearchNode(startNode, 0, null);
        // add the path to the list
        paths.add(startPath);
        // continue until paths is not empty anymore
        while (!paths.isEmpty()) {
            // remove the front index, and begin search
            SearchNode path = paths.remove(0);

            resultPaths.add(path);
            // get the node at the current path
            Node currentNode = path.node;
            // grab all the connect nodes
            List<Edge> connectedEdges = currentNode.edgesLeaving;

            for (Edge edge : connectedEdges) {

                // check if the current edge has already been visited
                if (visitedEdges.contains(edge)) {

                    // add to results
                    continue;
                }

                // get the node to continue the path
                Node node = edge.successor;
                // get the cost and add them
                double cost = edge.data.doubleValue() + path.cost;
                SearchNode pathCreated = new SearchNode(node, cost, path);
                paths.add(pathCreated);
                visitedEdges.add(edge);

            }

        }

        return resultPaths;
    }

    /**
     * Computes every single possible path from the given starting point. Will
     * return all of these paths in a BFS order
     *
     * @param start the data item in the starting node for the path
     * @return the path in a string format
     */
    public String getPathString(DijkstraGraph.SearchNode path) {
        ArrayList<String> resultPath = new ArrayList<String>();
        // loop until at starting node
        while (true) {
            // get the current nodes data
            NodeType data = (NodeType) path.node.data;
            // since we are going from last to first, continue adding at starting index
            resultPath.add(0, (String) data);

            // if we are at the starting node
            // checks that the predecessor is null
            SearchNode predecessor = path.predecessor;
            if (predecessor == null) {
                break;
            }
            // continue down the chain
            path = predecessor;

        }

        // convert to a String
        String results = String.join("->", resultPath);

        return results;
    }

    /**
     * gets the path cost that is passed in
     *
     * @param path
     * @return the path cost
     */
    public double getPathCost(DijkstraGraph.SearchNode path) {
        // return the cost of the path
        return path.cost;
    }
}
