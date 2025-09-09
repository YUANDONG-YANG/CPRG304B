package com.sait.oop3.appliances;

/**
 * Refrigerator class - Extends Appliance base class
 * Updated to match Lab 0 expected output format
 */
public class Refrigerator extends Appliance {

    // Refrigerator-specific attributes
    private int numberOfDoors;    // Number of doors (2, 3, or 4)
    private int height;           // Height in inches
    private int width;            // Width in inches

    // Constants for door validation
    public static final int MIN_DOORS = 2;
    public static final int MAX_DOORS = 4;

    /**
     * Constructor for Refrigerator
     */
    public Refrigerator(String itemNumber, String brand, int quantity, int wattage,
                        String color, double price, int numberOfDoors, int height, int width) {
        super(itemNumber, brand, quantity, wattage, color, price);
        validateRefrigeratorParameters(itemNumber, numberOfDoors, height, width);
        this.numberOfDoors = numberOfDoors;
        this.height = height;
        this.width = width;
    }

    /**
     * Validates refrigerator-specific parameters
     */
    private void validateRefrigeratorParameters(String itemNumber, int numberOfDoors, int height, int width) {
        if (itemNumber == null || itemNumber.isEmpty() || itemNumber.charAt(0) != '1') {
            throw new IllegalArgumentException("Refrigerator item number must start with '1'");
        }
        if (numberOfDoors < MIN_DOORS || numberOfDoors > MAX_DOORS) {
            throw new IllegalArgumentException(String.format("Number of doors must be between %d and %d", MIN_DOORS, MAX_DOORS));
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive");
        }
    }

    /**
     * Template Method implementation - adds refrigerator-specific details to string representation
     * Updated to match expected output format from Lab 0
     */
    @Override
    protected void appendSpecificDetails(StringBuilder sb) {
        sb.append("Number of Doors: ").append(getDoorTypeDescription()).append("\n");
        sb.append("Height: ").append(height).append("\n");
        sb.append("Width: ").append(width);
    }

    /**
     * Gets a descriptive name for the door configuration matching expected output
     * @return Description of door type
     */
    public String getDoorTypeDescription() {
        switch (numberOfDoors) {
            case 2:
                return "Double Door";
            case 3:
                return "Three Doors";
            case 4:
                return "Four Doors";
            default:
                return "Unknown Configuration";
        }
    }

    /**
     * Factory Method implementation - formats refrigerator data for file storage
     */
    @Override
    public String toFileFormat() {
        return String.join(";",
                itemNumber,
                brand,
                String.valueOf(quantity),
                String.valueOf(wattage),
                color,
                String.valueOf(price),
                String.valueOf(numberOfDoors),
                String.valueOf(height),
                String.valueOf(width));
    }

    // Getter methods
    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Checks if this refrigerator matches the specified door count filter
     */
    public boolean matchesDoorFilter(int doorFilter) {
        return this.numberOfDoors == doorFilter;
    }
}