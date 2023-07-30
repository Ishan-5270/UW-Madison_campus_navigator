
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Algorithm Engineers Interface
 * @param <NodeType> Generic type for nodes
 * @param <EdgeType> Generic type for edges
 */
public interface GraphReaderAEInterface<NodeType, EdgeType extends Number>
        extends GraphADT<NodeType, EdgeType> {

    public List<DijkstraGraph<NodeType, EdgeType>.SearchNode> findAllPaths(NodeType start)
            throws NoSuchElementException;

    public String getPathString(DijkstraGraph.SearchNode path);

    public double getPathCost(DijkstraGraph.SearchNode path);

}

