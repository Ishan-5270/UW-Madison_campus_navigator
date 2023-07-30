import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Backend Developers interface
 */
public interface CampusNavigatorBDInterface {
    public void loadData(String filename) throws FileNotFoundException  ; // adds the data to the graph

    public void addLocation(ArrayList<String> words); // adds the location to the map, checks if the location already exists
    // Input has to be in the form of (Location, Destination, Weight)

    public void removeLocation(ArrayList<String> words); // removes a given location from the map

    public String returnMultipleLocation(String start, List<String> destinations)
            throws NoSuchElementException; //returns the shortest path and cost from a starting point to multiple locations
}

