package com.sait.oop3.appliances;

/**
 * Abstract base class for all appliances - Implements Template Method and Factory Method patterns
 *
 * Design Patterns Applied:
 * 1. Template Method Pattern: toString() defines the algorithm skeleton for displaying appliance info,
 *    subclasses fill in specific details through appendSpecificDetails() method
 * 2. Factory Method Pattern: toFileFormat() abstract method allows subclasses to implement
 *    their own file formatting logic
 * 3. Encapsulation: All attributes use protected modifier, allowing subclass access while
 *    protecting external access
 *
 * @author Your Name
 * @version 2.0 (Refactored version)
 */
public abstract class Appliance {
    // Core attributes shared by all appliances
    protected String itemNumber;    // Item number (9 digits)
    protected String brand;         // Brand name
    protected int quantity;         // Stock quantity
    protected int wattage;          // Power consumption
    protected String color;         // Color
    protected double price;         // Price

    /**
     * Protected constructor - prevents direct instantiation of abstract class
     *
     * @param itemNumber The unique item number
     * @param brand The brand name
     * @param quantity The stock quantity
     * @param wattage The power consumption in watts
     * @param color The color
     * @param price The price
     */
    protected Appliance(String itemNumber, String brand, int quantity,
                        int wattage, String color, double price) {
        // Input validation
        validateInput(itemNumber, brand, quantity, wattage, color, price);

        this.itemNumber = itemNumber;
        this.brand = brand;
        this.quantity = quantity;
        this.wattage = wattage;
        this.color = color;
        this.price = price;
    }

    /**
     * Input validation method - ensures data integrity
     *
     * @param itemNumber The item number to validate
     * @param brand The brand to validate
     * @param quantity The quantity to validate
     * @param wattage The wattage to validate
     * @param color The color to validate
     * @param price The price to validate
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validateInput(String itemNumber, String brand, int quantity,
                               int wattage, String color, double price) {
        if (itemNumber == null || itemNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Item number cannot be null or empty");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be null or empty");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (wattage < 0) {
            throw new IllegalArgumentException("Wattage cannot be negative");
        }
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("Color cannot be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    /**
     * Template Method Pattern - defines the algorithm skeleton for displaying appliance information
     *
     * This method defines the standard format for displaying all appliance information:
     * 1. First displays common attributes (item number, brand, quantity, etc.)
     * 2. Then calls subclass appendSpecificDetails() method to display specific attributes
     * 3. final keyword prevents subclasses from overriding this algorithm structure
     *
     * @return Formatted appliance information string
     */
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        // Display basic information common to all appliances
        sb.append("Item Number: ").append(itemNumber).append("\n");
        sb.append("Brand: ").append(brand).append("\n");
        sb.append("Quantity: ").append(quantity).append("\n");
        sb.append("Wattage: ").append(wattage).append(" watts\n");
        sb.append("Color: ").append(color).append("\n");
        sb.append("Price: $").append(String.format("%.2f", price)).append("\n");

        // Hook method - allows subclasses to add specific details
        appendSpecificDetails(sb);

        return sb.toString();
    }

    /**
     * Hook method - subclasses implement specific detail display
     *
     * This is a key part of the Template Method pattern. Subclasses override this method
     * to add their specific attribute information, such as number of doors for refrigerators,
     * voltage for vacuums, etc.
     *
     * @param sb String builder to which subclasses add specific information
     */
    protected abstract void appendSpecificDetails(StringBuilder sb);

    /**
     * Factory Method Pattern - subclasses implement specific file formatting logic
     *
     * Each appliance type has a different storage format in the file. This abstract method
     * allows subclasses to implement their own file formatting logic, ensuring data
     * can be correctly saved to the file.
     *
     * @return Formatted string suitable for file storage
     */
    public abstract String toFileFormat();

    /**
     * Business logic method - checks if appliance is available for purchase
     *
     * @return true if quantity is greater than 0, false otherwise
     */
    public boolean isAvailable() {
        return quantity > 0;
    }

    /**
     * Business logic method - purchase appliance (decrease stock)
     *
     * Uses defensive programming to ensure negative stock cannot occur
     *
     * @return true if successfully purchased, false if insufficient stock
     */
    public boolean checkout() {
        if (quantity > 0) {
            quantity--;
            return true;
        }
        return false;
    }

    /**
     * Gets a friendly description of stock status
     *
     * @return User-friendly stock status description
     */
    public String getAvailabilityStatus() {
        if (quantity == 0) {
            return "Out of Stock";
        } else if (quantity == 1) {
            return "1 item available";
        } else {
            return quantity + " items available";
        }
    }

    // =============== Getter Methods ===============

    /**
     * Gets the item number
     * @return The item number
     */
    public String getItemNumber() {
        return itemNumber;
    }

    /**
     * Gets the brand name
     * @return The brand name
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Gets the current stock quantity
     * @return The current stock quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the power consumption
     * @return The power consumption in watts
     */
    public int getWattage() {
        return wattage;
    }

    /**
     * Gets the color
     * @return The color
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the price
     * @return The price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the appliance type based on the first digit of item number
     *
     * @return Appliance type description
     */
    public String getApplianceType() {
        if (itemNumber == null || itemNumber.isEmpty()) {
            return "Unknown";
        }

        char firstDigit = itemNumber.charAt(0);
        switch (firstDigit) {
            case '1': return "Refrigerator";
            case '2': return "Vacuum";
            case '3': return "Microwave";
            case '4':
            case '5': return "Dishwasher";
            default: return "Unknown";
        }
    }

    // =============== Object Class Method Overrides ===============

    /**
     * Overrides equals method - compares based on item number
     *
     * @param obj The object to compare with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Appliance appliance = (Appliance) obj;
        return itemNumber.equals(appliance.itemNumber);
    }

    /**
     * Overrides hashCode method - based on item number
     *
     * @return Hash code for this object
     */
    @Override
    public int hashCode() {
        return itemNumber.hashCode();
    }

    /**
     * Creates a brief description of the appliance (for logging or debugging)
     *
     * @return Brief description string
     */
    public String toShortString() {
        return String.format("%s [%s] - %s (%s)",
                getApplianceType(), itemNumber, brand, getAvailabilityStatus());
    }
}