// --== CS400 File Header Information ==--
// Name: Maximino Salgado
// Email: msalgado4@wisc.edu
// Group and Team: CS, Team Blue
// Group TA: Rahul
// Lecturer: Florian Heimerl
// Notes to Grader: None

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is responsible for creating the user interface of the
 * campus navigator app.
 *
 * @author Maximino Salgado
 *
 */
public class CampusNavigatorFD implements CampusNavigatorFDInterface {

    protected Scanner input; // Takes in user input
    protected CampusNavigatorBDInterface backEnd; // Used for calling methods needed from the backend
    protected ArrayList<String> existingLocations; // Keeps track of all existing locations
    protected GraphReaderDWInterface data;// used for getting data to keep the map up-to-date

    /**
     * This method is the constructor for the CampusNavigator class
     *
     * @param input   The information that the user is inputting into the console
     * @param backend an object used to collect data from the front-end/user input
     * @param data    an object used to keep the map up-to-date
     */
    public CampusNavigatorFD(Scanner input, CampusNavigatorBDInterface backend, GraphReaderDWInterface data) {
        this.input = input;
        this.backEnd = backend;
        this.data = data;
    }

    /*
     * This method is responsible for running the main menu that will prompt the
     * user
     * with what they would like to do in the program
     */
    @Override
    public void runCommandLoop() {
        loadDataCommand(); // load data

        char userCommand = '\0'; // set to null by default
        loadDataCommand(); // Map is loaded in automatically on program startup
        System.out.println("-------------------------------------------------------------------");
        System.out.println("Welcome to the UW-Madison Campus Navigator App!");
        System.out.println("-------------------------------------------------------------------");

        ArrayList<String> locationsToLowerCase = new ArrayList<String>(); // Used for checking if locations inputed
        // exists or not
        for (int j = 0; j < this.existingLocations.size(); j++) {// making all the locations in the map lower-case
            String temp = this.existingLocations.get(j).toLowerCase();
            locationsToLowerCase.add(temp);
        }

        while (userCommand != 'E') {

            userCommand = mainMenu();

            switch (userCommand) {

                case 'F': { // Finding shortest path with possibly additional locations in-between the start
                    // and end
                    locationsToLowerCase.clear();// updating this ArrayList to be up-to-date
                    for (int j = 0; j < this.existingLocations.size(); j++) {// making all the locations in the map
                        // lower-case
                        String temp = this.existingLocations.get(j).toLowerCase();
                        locationsToLowerCase.add(temp);
                    }
                    String startingLocation = null;
                    String startLocationToLower = null;
                    String endLocationToLower = null;
                    while (startingLocation == null || startingLocation == ""
                            || !locationsToLowerCase.contains(startLocationToLower)) {// A check to
                        // ensure a valid location is being given

                        System.out.println("Please select a start location from the following locations: ");
                        printLocationsInNavigator();
                        System.out.print("\n--> ");
                        startingLocation = this.input.nextLine().trim();
                        startLocationToLower = startingLocation.toLowerCase();
                    }

                    String endLocation = null;

                    while (endLocation == null || endLocation == ""
                            || !locationsToLowerCase.contains(endLocationToLower)) {// checking if input is valid
                        System.out.println("-------------------------------------------------------------------");
                        System.out.println("\nPlease select an end location from the following locations: ");
                        printLocationsInNavigator();
                        System.out.println();
                        System.out.print("--> ");
                        endLocation = this.input.nextLine().trim();
                        endLocationToLower = endLocation.toLowerCase();
                    }

                    System.out.println("-------------------------------------------------------------------");
                    System.out.println("Please list any additional locations that you would like to \nvisit "
                            + "between your start and end destination (Note: if there are \nmultiple locations,"
                            + " please separate the locations using commas)");
                    System.out.print("--> ");
                    String furtherLocations = this.input.nextLine();
                    String stops[] = furtherLocations.split(",");
                    ArrayList<String> additionalLocations = new ArrayList<>();

                    for (int i = 0; i < stops.length; i++) {

                        if (stops[i] != "") {
                            additionalLocations.add(stops[i]);
                        }

                    }

                    additionalLocations.add(endLocation);// End destination will be at the end of the ArrayList

                    String shortestPath = returnFromMultipleLocations(startingLocation, additionalLocations);
                    System.out.print(shortestPath);
                    System.out.println("-------------------------------------------------------------------");
                    continue;
                }

                case 'I': { // Inserting a new location(s) to the campus navigator

                    ArrayList<String> locationsToAdd = new ArrayList<>();
                    System.out.println("What is the name of the location that you would like to add?");
                    System.out.print("--> ");
                    String newLocationName = this.input.nextLine().trim();
                    locationsToAdd.add(newLocationName);
                    System.out.println("-------------------------------------------------------------------");
                    System.out.println("Are there any location(s) on the map that have a path \n"
                            + "to the location you would like to add?  If not, hit enter, \notherwise, have it in the following format:\n"
                            + "\"Location name\", \"Distance between this location and the location to be added\"");
                    System.out.println("\nLocations currently in campus navigator: ");
                    printLocationsInNavigator();

                    System.out.print("\n--> ");
                    String input = this.input.nextLine().trim();
                    String[] locations = input.split(",");
                    for (int j = 0; j < locations.length; j++) {
                        String temp = locations[j].trim();
                        locations[j] = temp;
                    }

                    Boolean validInput = false;
                    for (int i = 0; i < locations.length; i += 2) {

                        try {
                            Double distance = Double.parseDouble(locations[i + 1]);
                            if (locationsToLowerCase.contains(locations[i].toLowerCase())) {
                                validInput = true;
                            } else {
                                validInput = false;
                            }

                        } catch (NumberFormatException NFE) {
                            validInput = false;
                            continue;
                        } catch (Exception e) {
                            validInput = false;
                            continue;
                        }

                    }

                    while (validInput == false) {// Verifies that valid input is given
                        if (validInput == true) {
                            break;
                        }
                        System.out.println("-------------------------------------------------------------------");
                        System.out.println("Please make sure that the input is in the correct format");
                        System.out.println("\nPlease select a location from the following locations: ");
                        printLocationsInNavigator();
                        System.out.print("\n--> ");
                        input = this.input.nextLine().trim();
                        String[] verify = input.split(",");
                        for (int i = 0; i < verify.length; i++) {
                            String trimmedString = verify[i].trim();
                            verify[i] = trimmedString;
                        }

                        if (verify.length % 2 != 0) {// Used to ensure that given values are in the correct format
                            continue;
                        }

                        for (int i = 0; i < verify.length; i += 2) {

                            try {
                                Double distance = Double.parseDouble(verify[i + 1]); // Used to check if a number value
                                // is being
                                // inputed after a location is given

                                if (locationsToLowerCase.contains(verify[i].toLowerCase()) == true) {
                                    validInput = true;
                                    break;
                                } else {
                                    continue;
                                }

                            } catch (NumberFormatException NFE) {
                                validInput = false;
                                continue;
                            } catch (Exception e) {
                                validInput = false;
                                continue;
                            }
                        }
                    }

                    String[] locationsWithPaths = input.split(",");
                    for (int i = 0; i < locationsWithPaths.length; i++) {
                        locationsToAdd.add(locationsWithPaths[i].trim());
                    }
                    insertNewLocation(locationsToAdd);

                    continue;
                }

                case 'D': { // Removing a location from the campus navigator
                    removeLocation();
                    continue;
                }

                case 'E': {// Exiting the Campus Navigator App
                    System.out.println("Thank you for using the UW-Madison Campus Navigator!");
                    System.out.println("GoodBye!");
                    break;
                }

                default:
                    System.out.println("Invalid command,  please select a valid command.");
            }
        }
    }

    /**
     * A helper method that will print all the locations currently in the campus
     * navigator map,
     * This helper method will also print 3 locations per line
     */
    private void printLocationsInNavigator() {
        for (int i = 0; i < this.existingLocations.size(); i++) {

            if (i < this.existingLocations.size() - 1) {

                if (i == 0 && this.existingLocations.size() > 0) {
                    System.out.print(this.existingLocations.get(i) + ", ");
                }

                else if ((i + 1) % 3 != 0) {
                    System.out.print(this.existingLocations.get(i) + ", ");
                }

                else {
                    System.out.println(this.existingLocations.get(i));
                }
            } else {
                System.out.print(this.existingLocations.get(i));
            }
        }
    }

    /**
     * This method is responsible for prompting the user what they would like to
     * do in the campus navigator app, and ensures that only valid input is given
     */
    @Override
    public char mainMenu() {

        System.out.println("\nPlease Select an Option, no spaces please: ");
        System.out.println("-->[F] Find the Shortest Path to a Location");
        System.out.println("-->[I] Insert a New Location Into the Navigator");
        System.out.println("-->[D] Delete a Location From the Navigator");
        System.out.println("-->[E] Exit the Campus Navigator App");
        System.out.print("--> ");
        Character userSelected = '\0';
        try {
            userSelected = Character.toUpperCase(this.input.nextLine().charAt(0)); // Will grab ONLY the first character
            // that the user inputs
        } catch (StringIndexOutOfBoundsException SIOOBE) {
            // Thrown when user only hits enter and no input is given
        }
        System.out.println("-------------------------------------------------------------------");

        ArrayList<Character> validOptions = new ArrayList<>(); // contains all the valid command a user can input
        validOptions.add('F');
        validOptions.add('I');
        validOptions.add('D');
        validOptions.add('E');

        while (!validOptions.contains(userSelected)) {
            System.out.println("Please enter a valid command");
            System.out.print("--> ");
            try {
                userSelected = Character.toUpperCase(this.input.nextLine().charAt(0));
            } catch (StringIndexOutOfBoundsException SIOOBE) {
                continue;
            }
        }

        return userSelected;
    }

    /**
     * This method is responsible for loading in the map to be used in the campus
     * navigator.
     * The user does not need to specify a file, the file is loaded in automatically
     * on startup
     */
    @Override
    public void loadDataCommand() {
        String file = "map.dot";
        try {
            backEnd.loadData(file);
            this.existingLocations = (ArrayList<String>) this.data.getVertices();// Getting all the
            // locations initially present at startup
        } catch (FileNotFoundException FNFE) {
            FNFE.printStackTrace();
        }

    }

    /**
     * This method is responsible for prompting the user for valid input so that a
     * location may be removed from the campus navigator
     */
    @Override
    public void removeLocation() {
        System.out.println("Please select a location(s) that you would like to remove: ");
        printLocationsInNavigator();
        System.out.print("\n-->");

        try {

            ArrayList<String> locationsToLowerCase = new ArrayList<String>(); // Used for standardizing all input by
            // setting it to lower-case
            for (int j = 0; j < this.existingLocations.size(); j++) {
                String temp = this.existingLocations.get(j).toLowerCase();
                locationsToLowerCase.add(temp);
            }

            String originalLine = this.input.nextLine(); // Used for printing purposes
            String currentLine = originalLine.toLowerCase();
            String[] locationsGiven = originalLine.split(",");
            for (int i = 0; i < locationsGiven.length; i++) {
                String temp = locationsGiven[i].trim();
                locationsGiven[i] = temp;
            }

            ArrayList<String> locationsToRemove = new ArrayList<>(); // ArrayList to be sent to backend
            Boolean verify = false;
            int counter = 0;

            for (int i = 0; i < locationsGiven.length; i++) {// Insert all of the desired locations to be removed
                // into an ArrayList
                locationsToRemove.add(locationsGiven[i].trim());
            }

            while (verify == false) {// to ensure valid input is given
                for (int x = 0; x < locationsToRemove.size(); x++) {

                    if (locationsToLowerCase.contains(locationsToRemove.get(x).toLowerCase())) {
                        counter++;
                    }
                }
                if (counter == locationsToRemove.size()) {
                    verify = true;
                    break;
                } else {
                    System.out.println("Please select valid locations only");
                    System.out.print("-->");
                    originalLine = this.input.nextLine();
                    currentLine = originalLine.toLowerCase();
                    locationsGiven = originalLine.split(",");
                    for (int i = 0; i < locationsGiven.length; i++) {
                        String temp = locationsGiven[i].trim();
                        locationsGiven[i] = temp;
                    }
                    locationsToRemove.clear();
                    for (int i = 0; i < locationsGiven.length; i++) {// Insert all of the desired locations to be
                        // removed into an ArrayList
                        locationsToRemove.add(locationsGiven[i].trim());
                    }
                }
            }

            this.backEnd.removeLocation(locationsToRemove);
            Boolean restart = false;

            for (int i = 0; i < locationsToRemove.size(); i++) {
                if (restart == true) {
                    i = 0;
                }
                String current = locationsToRemove.get(i).toLowerCase().trim();
                restart = false;
                for (int k = 0; k < this.existingLocations.size(); k++) {
                    if (this.existingLocations.get(k).toLowerCase().trim().equals(current)) {
                        this.existingLocations.remove(k);
                        restart = true;
                        break;
                    }
                }
            }

            System.out.println("The following locations have been removed from the campus navigator: "
                    + originalLine); // If we reach this line, then the removal was successful
            System.out.println("-------------------------------------------------------------------");
        }

        catch (Exception e) {// An error occurred while trying to remove a location(s)
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending off any information needed so that the
     * a new
     * location may be added through the backend.
     *
     * @param location A new location to be added into the campus navigator along
     *                 with
     *                 additional information such as connected locations (i.e.
     *                 locations that have a
     *                 direct path with the location to be added) and the distance
     *                 between these two
     *                 locations. This is all accomplished by having the ArrayList
     *                 in the following
     *                 format: (The new location to be added, any existing locations
     *                 on the campus
     *                 navigator that have a direct path with the new location, the
     *                 distance between
     *                 the existing location(s) and the new location)
     */
    @Override
    public void insertNewLocation(ArrayList<String> location) {

        try {
            this.backEnd.addLocation(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String newLocation = location.get(0);// getting the new location
        this.existingLocations.add(newLocation);
        System.out.println(newLocation + " has been added to the campus navigator map!");
        System.out.println("-------------------------------------------------------------------");
        return;
    }

    /**
     * This method is responsible for prompting the user to define a start and end
     * point and any additional
     * stops between the start and end point. We will then show the user the
     * shortest cost path and the path itself
     * that takes everything the user has inputed into consideration
     */
    @Override
    public String returnFromMultipleLocations(String startLocation, List<String> multipleLocations) {
        List<String> stops = (ArrayList<String>) multipleLocations;

        String result = this.backEnd.returnMultipleLocation(startLocation, stops);
        return result;
    }
}
