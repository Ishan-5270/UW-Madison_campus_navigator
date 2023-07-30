import java.io.FileNotFoundException;
import java.util.List;

/**
 * Data Wranglers interface
 */
public interface GraphReaderDWInterface {
    //private fields
    //private List<String> vertices;
    //private List<String> edges; //format: “pred,succ,weight”
    //reads DOT files and loads lists of edges and vertices
    public void readDOTfile(String filepath) throws FileNotFoundException;
    //returns vertices
    public List<String> getVertices();
    //returns edges
    public List<String> getEdges();

}

