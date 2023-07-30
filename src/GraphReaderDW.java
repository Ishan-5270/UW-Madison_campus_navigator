// --== CS400 Project Three File Header ==--
// Name: Luis R. Ylizaliturri
// CSL Username: ylizaliturri
// Email: lylizaliturr@wisc.edu
// Team: Blue GROUP: CS
// TA: Rahul
// Lecturer: Florian
// Notes to Grader: None

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a DOT file reader.
 */
public class GraphReaderDW implements GraphReaderDWInterface {

    private List<String> vertices; // list of vertices to add to the graph
    private List<String> edges; // list of edges to add to the graph

    /**
     * Constructor for GraphReaderDW
     */
    public GraphReaderDW() {
        vertices = new ArrayList<>(); // initialize vertex list
        edges = new ArrayList<>(); // initialize edge list ; String format: "predecesor,successor,weight"
    }

    /**
     * Uses a BufferReader to read DOT file and parse the contents into a list of
     * vertices and
     * a list of edges.
     *
     * @param filepath - String rerpesentation of the file path of the DOT file.
     * @throws IOException if file path is not found
     */
    @Override
    public void readDOTfile(String filepath) throws FileNotFoundException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            while (line != null) {
                // Identify Edge
                if (line.contains("--")) {
                    // parse edge
                    // identify predecessor
                    int index1 = line.indexOf("\"") + 1;
                    int index2 = line.indexOf("\"", index1 + 1);
                    String vertex1 = line.substring(index1, index2);

                    // identify successor
                    index1 = line.indexOf("\"", index2 + 1) + 1;
                    index2 = line.indexOf("\"", index1 + 1);
                    String vertex2 = line.substring(index1, index2);

                    // identify the weight
                    int equals = line.indexOf("=");
                    index1 = line.indexOf("\"", equals) + 1;
                    index2 = line.indexOf("\"", index1 + 1);
                    String weight = line.substring(index1, index2);

                    // add the edge in string form to the list of edges
                    edges.add(vertex1 + "," + vertex2 + "," + weight);
                    if (!vertices.contains(vertex1)) {
                        vertices.add(vertex1);
                    }
                    if (!vertices.contains(vertex2)) {
                        vertices.add(vertex2);
                    }
                }
                line = reader.readLine(); // next line
            }
            reader.close(); // terminate BufferReader
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    /**
     * Accessor method for the list of verices.
     */
    @Override
    public List<String> getVertices() {
        return vertices;
    }

    /**
     * Accessor method for the list of edges. String format:
     * "predecesor,successor,weight"
     */
    @Override
    public List<String> getEdges() {
        return edges;
    }

    /**
     * Main method to call the methods implemented in this class.
     */
    public static void main(String[] args) {
        GraphReaderDW graph = new GraphReaderDW();
        try {
            graph.readDOTfile("map.dot");
            System.out.println(graph.getVertices().size());
            System.out.println(graph.getEdges());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
