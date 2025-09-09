package com.sait.oop3.appliances;

/**
 * Refrigerator class - Extends Appliance base class
 * <p>
 * Represents a refrigerator appliance with specific attributes such as number of doors,
 * height, and width. Item numbers for refrigerators start with digit '1'.
 * <p>
 * Design Patterns Implemented:
 * - Template Method: Implements appendSpecificDetails() to add refrigerator-specific information
 * - Factory Method: Implements toFileFormat() for refrigerator-specific file formatting
 * <p>
 * File Format: ItemNumber;Brand;Quantity;Wattage;Color;Price;NumberOfDoors;Height;Width
 * Example: 089360200;Bosch;176;60;black;2000;2;62;29
 *
 * @author Yuandong Yang
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
     *
     * @param itemNumber    The unique item number (must start with '1')
     * @param brand         The brand name
     * @param quantity      The stock quantity
     * @param wattage       The power consumption in watts
     * @param color         The color
     * @param price         The price
     * @param numberOfDoors The number of doors (2, 3, or 4)
     * @param height        The height in inches
     * @param width         The width in inches
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Refrigerator(String itemNumber, String brand, int quantity, int wattage, String color, double price, int numberOfDoors, int height, int width) {
        // Call parent constructor
        super(itemNumber, brand, quantity, wattage, color, price);

        // Validate refrigerator-specific parameters
        validateRefrigeratorParameters(itemNumber, numberOfDoors, height, width);

        this.numberOfDoors = numberOfDoors;
        this.height = height;
        this.width = width;
    }

    /**
     * Validates refrigerator-specific parameters
     *
     * @param itemNumber    The item number to validate
     * @param numberOfDoors The number of doors to validate
     * @param height        The height to validate
     * @param width         The width to validate
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validateRefrigeratorParameters(String itemNumber, int numberOfDoors, int height, int width) {
        // Validate item number starts with '1'
        if (itemNumber == null || itemNumber.isEmpty() || itemNumber.charAt(0) != '1') {
            throw new IllegalArgumentException("Refrigerator item number must start with '1'");
        }

        // Validate number of doors
        if (numberOfDoors < MIN_DOORS || numberOfDoors > MAX_DOORS) {
            throw new IllegalArgumentException(String.format("Number of doors must be between %d and %d", MIN_DOORS, MAX_DOORS));
        }

        // Validate dimensions
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be positive");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive");
        }
    }

    /**
     * Template Method implementation - adds refrigerator-specific details to string representation
     * <p>
     * This method is called by the parent class's toString() method to append
     * refrigerator-specific information to the standard appliance information.
     *
     * @param sb StringBuilder to append refrigerator-specific details
     */
    @Override
    protected void appendSpecificDetails(StringBuilder sb) {
        sb.append("Number of Doors: ").append(numberOfDoors);
        sb.append(" (").append(getDoorTypeDescription()).append(")\n");
        sb.append("Height: ").append(height).append(" inches\n");
        sb.append("Width: ").append(width).append(" inches\n");
        sb.append("Dimensions: ").append(height).append("H x ").append(width).append("W inches");
    }

    /**
     * Factory Method implementation - formats refrigerator data for file storage
     * <p>
     * Creates a semicolon-separated string in the format required for the appliances.txt file.
     * Format: ItemNumber;Brand;Quantity;Wattage;Color;Price;NumberOfDoors;Height;Width
     *
     * @return Formatted string suitable for file storage
     */
    @Override
    public String toFileFormat() {
        return String.join(";", itemNumber, brand, String.valueOf(quantity), String.valueOf(wattage), color, String.valueOf(price), String.valueOf(numberOfDoors), String.valueOf(height), String.valueOf(width));
    }

    /**
     * Gets the number of doors
     *
     * @return The number of doors
     */
    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    /**
     * Gets the height in inches
     *
     * @return The height in inches
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the width in inches
     *
     * @return The width in inches
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets a descriptive name for the door configuration
     *
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
     * Calculates the total volume capacity estimation based on dimensions
     * Note: This is a simplified calculation for demonstration purposes
     *
     * @return Estimated volume in cubic feet
     */
    public double getEstimatedVolume() {
        // Simplified volume calculation (assuming average depth of 24 inches)
        final int AVERAGE_DEPTH = 24;
        double volumeInCubicInches = height * width * AVERAGE_DEPTH;
        double volumeInCubicFeet = volumeInCubicInches / 1728.0; // 1728 cubic inches = 1 cubic foot
        return Math.round(volumeInCubicFeet * 100.0) / 100.0; // Round to 2 decimal places
    }

    /**
     * Checks if this is a standard size refrigerator
     * Standard size is typically defined as 2-door with height between 60-70 inches
     *
     * @return true if this is considered a standard size refrigerator
     */
    public boolean isStandardSize() {
        return numberOfDoors == 2 && height >= 60 && height <= 70;
    }

    /**
     * Gets energy efficiency category based on wattage
     * This is a simplified categorization for demonstration
     *
     * @return Energy efficiency category
     */
    public String getEnergyEfficiencyCategory() {
        if (wattage <= 100) {
            return "High Efficiency";
        } else if (wattage <= 200) {
            return "Medium Efficiency";
        } else {
            return "Standard Efficiency";
        }
    }

    /**
     * Creates a detailed description for customer display
     *
     * @return Detailed customer-friendly description
     */
    public String getCustomerDescription() {
        return String.format("%s %s Refrigerator - %s, %d\"H x %d\"W, %s, %s", brand, getDoorTypeDescription(), color, height, width, getEnergyEfficiencyCategory(), getAvailabilityStatus());
    }

    /**
     * Compares refrigerators by size (height * width)
     *
     * @param other The other refrigerator to compare with
     * @return negative if this is smaller, positive if larger, 0 if equal
     */
    public int compareBySize(Refrigerator other) {
        int thisSize = this.height * this.width;
        int otherSize = other.height * other.width;
        return Integer.compare(thisSize, otherSize);
    }

    /**
     * Checks if this refrigerator matches the specified door count filter
     *
     * @param doorFilter The door count to filter by
     * @return true if this refrigerator matches the door filter
     */
    public boolean matchesDoorFilter(int doorFilter) {
        return this.numberOfDoors == doorFilter;
    }
}