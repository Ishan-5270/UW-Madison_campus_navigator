// --== CS400 File Header Information ==--
// Name: Maximino Salgado
// Email: msalgado4@wisc.edu
// Group and Team: CS, Team Blue
// Group TA: Rahul
// Lecturer: Florian Heimerl
// Notes to Grader: None

import java.util.ArrayList;
import java.util.List;

public interface CampusNavigatorFDInterface {

    public void runCommandLoop();

    public char mainMenu(); // prompts the user with what they would like to do in the
    // navigator

    public void loadDataCommand(); // Loads in the data from the DW

    public void insertNewLocation(ArrayList<String> location); // insert a new location into the map

    public void removeLocation(); // removes a location from the map

    public String returnFromMultipleLocations(String startLocation, List<String> multipleLocations);
    // Pick a starting location and an end location and the campus navigator will
    // find the shortest path

}
