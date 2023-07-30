// --== CS400 Project Three File Header ==--
// Name: Luis R. Ylizaliturri
// CSL Username: ylizaliturri
// Email: lylizaliturr@wisc.edu
// Team: Blue GROUP: CS
// TA: Rahul
// Lecturer: Florian
// Notes to Grader: None
import java.util.Scanner;
/**
 * Main class to run the Compus Navigator Application
 */
public class CampusNavigatorApp {

    /**
     * Main method to run the program
     */
    public static void main(String[] args) {
        //cretae DW class object to get the graph data
        GraphReaderDW reader = new GraphReaderDW();
        //create AE class object to make graph
        GraphReaderAE<String, Double> graph = new GraphReaderAE();
        //create backend class object
        CampusNavigatorBD backend = new CampusNavigatorBD(graph , reader);
        //create Scanner for user input
        Scanner scanner = new Scanner(System.in);
        //create FD class object
        CampusNavigatorFD frontend = new CampusNavigatorFD(scanner, backend, reader);
        //run the program.
        frontend.runCommandLoop();
    }
}