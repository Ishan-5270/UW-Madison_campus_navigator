import java.util.ArrayList;
import java.util.List;

/**
 * Placeholder implementation of the Data Wranglers interface
 */
public class GraphReaderDWBD implements GraphReaderDWInterface {

    private List<String> vertices;
    private List<String> edges; //format: “pred,succ,weight”
    @Override
    public void readDOTfile(String filepath) {
        // TODO: Implemented by Data Wrangler
    }

    /**
     *
     * @return a list of vertices that is used for testing
     */
    @Override
    public List<String> getVertices() {
        vertices = new ArrayList<>();
        vertices.add("A");
        vertices.add("B");
        vertices.add("C");
        vertices.add("D");
        vertices.add("E");
        return vertices;
    }

    /**
     *
     * @return list of edges that is used for testing
     */
    @Override
    public List<String> getEdges() {
        edges = new ArrayList<>();
        edges.add("A,B,2");
        edges.add("A,C,3");
        edges.add("B,C,1");
        edges.add("C,D,4");
        edges.add("D,E,5");
        return edges;
    }
}

