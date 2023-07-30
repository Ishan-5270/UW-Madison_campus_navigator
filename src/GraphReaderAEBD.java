import java.util.*;

/**
 * Placeholder implementation of the Algorithm Engineers method
 * @param <NodeType> Generic NodeType
 * @param <EdgeType> Generic EdgeType
 */
public class GraphReaderAEBD<NodeType, EdgeType extends Number>
        extends DijkstraGraph<NodeType, EdgeType>
        implements GraphReaderAEInterface<NodeType, EdgeType> {

    private List<DijkstraGraph<NodeType, EdgeType>.SearchNode> paths;

    @Override
    public List<DijkstraGraph<NodeType, EdgeType>.SearchNode> findAllPaths(NodeType start) throws NoSuchElementException {
        List<DijkstraGraph<NodeType, EdgeType>.SearchNode> paths = new ArrayList<>();
        return paths;
    }

    @Override
    public double getPathCost(DijkstraGraph.SearchNode path) {
        return 4;
    }


    @Override
    public String getPathString(DijkstraGraph.SearchNode path) {
        return "ABCDEF";
    }

}