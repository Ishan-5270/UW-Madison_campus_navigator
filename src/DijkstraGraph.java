// --== CS400 File Header Information ==--
// Name: Ishan Rungta
// Email: imrungta@wisc.edu
// Group and Team: <your group name: CS, and blue>
// Group TA: Rahul Chaudhary
// Lecturer: FLorian Heimerl
// Notes to Grader: :)

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes.  This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType,EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph.  The final node in this path is stored in it's node
     * field.  The total cost of this path is stored in its cost field.  And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in it's node field).
     * <p>
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost) return +1;
            if (cost < other.cost) return -1;
            return 0;
        }
    }


    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations.  The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {

        Node startNode = this.nodes.get(start);
        Node endNode = this.nodes.get(end);

        // Check that the start and end nodes exist
        if (startNode == null || endNode == null) {
            throw new NoSuchElementException("Start and end nodes must be in the graph.");
        }
        SearchNode startpoint = new SearchNode(startNode, 0, null);
        // Initializing the priority queue and hashtable
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        Hashtable<NodeType, SearchNode> visited = new Hashtable<>();
        // Add the start node to the priority queue with cost 0
        pq.add(startpoint);


        // Perform Dijkstra's algorithm
        while (!pq.isEmpty()) {
            // Get the node with the lowest cost so far
            SearchNode currentnode = pq.poll();

            // If we have found the end node, return the shortest path
            if (currentnode.node.data.equals(endNode.data)) {
                return currentnode;
            }

            // if the current node has not been visited yet
            if (!visited.containsKey(currentnode.node.data)) {
                visited.put(currentnode.node.data, currentnode);
                Node current = this.nodes.get(currentnode.node.data);
                for (int i = 0; i < current.edgesLeaving.size(); i++) {
                    SearchNode CurrentNode = new SearchNode(current.edgesLeaving.get(i).successor,
                            current.edgesLeaving.get(i).data.doubleValue() + currentnode.cost, currentnode);
                    pq.add(CurrentNode);
                }
            }
        }

        // If we get here, there is no path from startNode to endNode
        if (!visited.containsKey(end)) {
            throw new NoSuchElementException("No path from startNode to endNode");
        }
        return visited.get(end);
    }


    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value.  This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path.  This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {

        SearchNode current = computeShortestPath(start, end);
        List<NodeType> path = new LinkedList<>();
        while (current != null) {
            path.add(0, current.node.data);
            current = current.predecessor;
        }
        return path;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data.  This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        SearchNode endNode = computeShortestPath(start, end);
        return endNode.cost;
    }


    /**
     * This test traces through an example discussed in lecture and computes the shortest path and cost
     * and compares them with solutions previously calculated by hand
     */
    @Test
    public void testComputeShortestPath() {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        // Add nodes and edges to graph - using the example used in lecture
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");
        graph.insertEdge("A", "B", 4.0);
        graph.insertEdge("B", "E", 10.0);
        graph.insertEdge("A", "E", 15.0);
        graph.insertEdge("A", "C", 2.0);
        graph.insertEdge("C", "D", 5.0);
        graph.insertEdge("B", "D", 1.0);
        graph.insertEdge("D", "E", 3.0);
        graph.insertEdge("D", "F", 0.0);
        graph.insertEdge("F", "D", 2.0);
        graph.insertEdge("F", "H", 4.0);
        graph.insertEdge("G", "H", 4.0);

        // Test shortest path from A to E as computed in the lecture
        List<String> expectedPath = Arrays.asList("A", "B", "D", "E");
        List<String> actualPath = graph.shortestPathData("A", "E");
        int actualcost = 8;
        double expectedcost = graph.shortestPathCost("A", "E");

        assertEquals(expectedPath, actualPath);
        Assertions.assertEquals(expectedcost, actualcost);


        // Test shortest path from A to F as computed in the lecture
        List<String> expectedPath1 = Arrays.asList("A", "B", "D", "F");
        List<String> actualPath1 = graph.shortestPathData("A", "F");
        int actualcost1 = 5;
        double expectedcost1 = graph.shortestPathCost("A", "F");

        assertEquals(expectedPath1, actualPath1);
        Assertions.assertEquals(expectedcost1, actualcost1);
    }

    /**
     * This test makes use of the same example that was used in the lecture but now explores the shortest path
     * and cost between nodes that were not previously discussed in the lecture
     */
    @Test
    public void testShortestPathDataAndCost2() {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        // Add nodes and edges to graph
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");
        graph.insertEdge("A", "B", 4.0);
        graph.insertEdge("B", "E", 10.0);
        graph.insertEdge("A", "E", 15.0);
        graph.insertEdge("A", "C", 2.0);
        graph.insertEdge("C", "D", 5.0);
        graph.insertEdge("B", "D", 1.0);
        graph.insertEdge("D", "E", 3.0);
        graph.insertEdge("D", "F", 0.0);
        graph.insertEdge("F", "D", 2.0);
        graph.insertEdge("F", "H", 4.0);
        graph.insertEdge("G", "H", 4.0);

        // computing the shortest path from B to H
        List<String> expectedPath = Arrays.asList("B", "D", "F", "H");
        List<String> actualPath = graph.shortestPathData("B", "H");
        int actualcost = 5;
        double expectedcost = graph.shortestPathCost("B", "H");
        assertEquals(expectedPath, actualPath);
        Assertions.assertEquals(expectedcost, actualcost);

        // Test shortest path from F to E
        List<String> expectedPath1 = Arrays.asList("F", "D", "E");
        List<String> actualPath1 = graph.shortestPathData("F", "E");
        int actualcost1 = 5;
        double expectedcost1 = graph.shortestPathCost("F", "E");

        assertEquals(expectedPath1, actualPath1);
        Assertions.assertEquals(expectedcost1, actualcost1);
    }

    /**
     *  this test checks if an exception is thrown when the node that you are searching for a path
     *  between exist in the graph, but there is no sequence of directed edges that connects them from the start to
     *  the end
     */
    @Test
    public void testNoDirectPath() {
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        // Add nodes and edges to graph
        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");
        graph.insertEdge("A", "B", 4.0);
        graph.insertEdge("B", "E", 10.0);
        graph.insertEdge("A", "E", 15.0);
        graph.insertEdge("A", "C", 2.0);
        graph.insertEdge("C", "D", 5.0);
        graph.insertEdge("B", "D", 1.0);
        graph.insertEdge("D", "E", 3.0);
        graph.insertEdge("D", "F", 0.0);
        graph.insertEdge("F", "D", 2.0);
        graph.insertEdge("F", "H", 4.0);
        graph.insertEdge("G", "H", 4.0);

        // test checking if the null pointer expception is no path exixts between two nodes in the graph
        // checking path between nodes H and F
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathData("H", "F");
        });

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathCost("H", "F");
        });

        // Searching for a path from C to A
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathData("C", "A");
        });

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            graph.shortestPathCost("C", "A");
        });
    }

}
