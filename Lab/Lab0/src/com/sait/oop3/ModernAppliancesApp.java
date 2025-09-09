package com.sait.oop3;

import com.sait.oop3.appliances.*;
import com.sait.oop3.io.ApplianceFileManager;

import java.util.*;

/**
 * Main application class for Modern Appliances management system
 * Handles menu operations and business logic
 *
 */
public class ModernAppliancesApp {
    private static List<Appliance> appliances = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
        // Load appliances from file using the new FileManager
        appliances = ApplianceFileManager.loadAppliances();

        // Display menu and handle user input
        int option;
        do {
            displayMenu();
            option = getIntInput("Enter option:");

            switch (option) {
                case 1:
                    checkoutAppliance();
                    break;
                case 2:
                    findAppliancesByBrand();
                    break;
                case 3:
                    displayAppliancesByType();
                    break;
                case 4:
                    produceRandomApplianceList();
                    break;
                case 5:
                    saveAndExit();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (option != 5);
    }

    /**
     * Displays the main menu
     */
    private static void displayMenu() {
        System.out.println("\nWelcome to Modern Appliances!");
        System.out.println("\nHow may we assist you?");
        System.out.println("1 – Check out appliance");
        System.out.println("2 – Find appliances by brand");
        System.out.println("3 – Display appliances by type");
        System.out.println("4 – Produce random appliance list");
        System.out.println("5 – Save & exit");
    }

    /**
     * Handles appliance checkout functionality
     */
    private static void checkoutAppliance() {
        System.out.print("Enter the item number of an appliance:");
        String itemNumber = scanner.nextLine().trim();

        Appliance appliance = findApplianceByItemNumber(itemNumber);

        if (appliance == null) {
            System.out.println("No appliances found with that item number.");
        } else if (!appliance.isAvailable()) {
            System.out.println("The appliance is not available to be checked out.");
        } else {
            appliance.checkout();
            System.out.println("Appliance \"" + itemNumber + "\" has been checked out.");
            System.out.println("\n" + appliance.toString());
        }
    }

    /**
     * Finds an appliance by its item number
     *
     * @param itemNumber The item number to search for
     * @return The appliance if found, null otherwise
     */
    private static Appliance findApplianceByItemNumber(String itemNumber) {
        for (Appliance appliance : appliances) {
            if (appliance.getItemNumber().equals(itemNumber)) {
                return appliance;
            }
        }
        return null;
    }

    /**
     * Finds and displays appliances by brand name (case-insensitive)
     */
    private static void findAppliancesByBrand() {
        System.out.print("Enter brand to search for:");
        String brand = scanner.nextLine().trim();

        List<Appliance> matchingAppliances = new ArrayList<>();
        for (Appliance appliance : appliances) {
            if (appliance.getBrand().equalsIgnoreCase(brand)) {
                matchingAppliances.add(appliance);
            }
        }

        if (matchingAppliances.isEmpty()) {
            System.out.println("No appliances found for brand: " + brand);
        } else {
            System.out.println("\nMatching Appliances:");
            for (Appliance appliance : matchingAppliances) {
                System.out.println(appliance.toString());
                System.out.println(); // Empty line between appliances
            }
        }
    }

    /**
     * Displays appliances by type with specific filtering options
     */
    private static void displayAppliancesByType() {
        System.out.println("\nAppliance Types");
        System.out.println("1 – Refrigerators");
        System.out.println("2 – Vacuums");
        System.out.println("3 – Microwaves");
        System.out.println("4 – Dishwashers");

        int type = getIntInput("Enter type of appliance:");

        switch (type) {
            case 1:
                displayRefrigerators();
                break;
            case 2:
                displayVacuums();
                break;
            case 3:
                displayMicrowaves();
                break;
            case 4:
                displayDishwashers();
                break;
            default:
                System.out.println("Invalid appliance type.");
        }
    }

    /**
     * Displays refrigerators filtered by number of doors
     */
    private static void displayRefrigerators() {
        int doors = getIntInput("Enter number of doors: 2 (double door), 3 (three doors) or 4 (four doors):");

        System.out.println("\nMatching refrigerators:");
        boolean found = false;
        for (Appliance appliance : appliances) {
            if (appliance instanceof Refrigerator) {
                Refrigerator fridge = (Refrigerator) appliance;
                if (fridge.getNumberOfDoors() == doors) {
                    System.out.println(fridge.toString());
                    System.out.println();
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No refrigerators found with " + doors + " doors.");
        }
    }

    /**
     * Displays vacuums filtered by battery voltage
     */
    private static void displayVacuums() {
        int voltage = getIntInput("Enter battery voltage value. 18 V (low) or 24 V (high):");

        System.out.println("\nMatching vacuums:");
        boolean found = false;
        for (Appliance appliance : appliances) {
            if (appliance instanceof Vacuum) {
                Vacuum vacuum = (Vacuum) appliance;
                if (vacuum.getBatteryVoltage() == voltage) {
                    System.out.println(vacuum.toString());
                    System.out.println();
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No vacuums found with " + voltage + "V battery.");
        }
    }

    /**
     * Displays microwaves filtered by room type
     */
    private static void displayMicrowaves() {
        System.out.print("Room where the microwave will be installed: K (kitchen) or W (work site):");
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.isEmpty()) {
            System.out.println("Invalid input. Please enter K or W.");
            return;
        }

        char roomType = input.charAt(0);

        System.out.println("\nMatching microwaves:");
        boolean found = false;
        for (Appliance appliance : appliances) {
            if (appliance instanceof Microwave) {
                Microwave microwave = (Microwave) appliance;
                if (microwave.getRoomType() == roomType) {
                    System.out.println(microwave.toString());
                    System.out.println();
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No microwaves found for room type: " + roomType);
        }
    }

    /**
     * Displays dishwashers filtered by sound rating
     */
    private static void displayDishwashers() {
        System.out.print("Enter the sound rating of the dishwasher: Qt (Quietest), Qr (Quieter), Qu(Quiet) or M (Moderate):");
        String soundRating = scanner.nextLine().trim();

        System.out.println("\nMatching dishwashers:");
        boolean found = false;
        for (Appliance appliance : appliances) {
            if (appliance instanceof Dishwasher) {
                Dishwasher dishwasher = (Dishwasher) appliance;
                if (dishwasher.getSoundRating().equalsIgnoreCase(soundRating)) {
                    System.out.println(dishwasher.toString());
                    System.out.println();
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No dishwashers found with sound rating: " + soundRating);
        }
    }

    /**
     * Produces and displays a random list of appliances
     */
    private static void produceRandomApplianceList() {
        int count = getIntInput("Enter number of appliances:");

        if (count <= 0) {
            System.out.println("Please enter a positive number.");
            return;
        }

        if (count > appliances.size()) {
            count = appliances.size();
            System.out.println("Only " + count + " appliances available.");
        }

        System.out.println("\nRandom appliances:");
        List<Appliance> shuffled = new ArrayList<>(appliances);
        Collections.shuffle(shuffled, random);

        for (int i = 0; i < count; i++) {
            System.out.println(shuffled.get(i).toString());
            System.out.println();
        }
    }

    /**
     * Saves appliances back to file and exits the program
     */
    private static void saveAndExit() {
        boolean success = ApplianceFileManager.saveAppliances(appliances);

        if (success) {
            System.out.println("Appliances saved to file successfully.");
        } else {
            System.out.println("Warning: There was an error saving the appliances.");
        }

        System.out.println("Thank you for using Modern Appliances!");
        scanner.close();
    }

    /**
     * Gets integer input from user with error handling
     *
     * @param prompt The prompt to display
     * @return Valid integer input
     */
    private static int getIntInput(String prompt) {
        int input = -1;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.print(prompt);
                String inputStr = scanner.nextLine().trim();
                input = Integer.parseInt(inputStr);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        return input;
    }
}