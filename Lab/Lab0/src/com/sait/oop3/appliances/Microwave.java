package com.sait.oop3.appliances;

import java.util.Arrays;
import java.util.List;

/**
 * Microwave class - Extends Appliance base class
 *
 * Represents a microwave appliance with specific attributes such as capacity and room type.
 * Item numbers for microwaves start with digit '3'.
 *
 * Design Patterns Implemented:
 * - Template Method: Implements appendSpecificDetails() to add microwave-specific information
 * - Factory Method: Implements toFileFormat() for microwave-specific file formatting
 *
 * File Format: ItemNumber;Brand;Quantity;Wattage;Color;Price;Capacity;RoomType
 * Example: 383477937;Miele;201;2350;white;179.9;1.8;K;
 *
 * Room Type Options:
 * - K (Kitchen)
 * - W (Work site)
 *
 * @author Your Name
 * @version 2.0 (Refactored version)
 */
public class Microwave extends Appliance {

    // Microwave-specific attributes
    private double capacity;    // Capacity in cubic feet
    private char roomType;      // Room type ('K' for Kitchen, 'W' for Work site)

    // Room type constants
    public static final char KITCHEN = 'K';
    public static final char WORK_SITE = 'W';

    // Valid room types for validation
    private static final List<Character> VALID_ROOM_TYPES = Arrays.asList(KITCHEN, WORK_SITE);

    // Capacity ranges for classification
    public static final double COMPACT_MAX_CAPACITY = 1.0;
    public static final double STANDARD_MAX_CAPACITY = 2.0;
    public static final double LARGE_MIN_CAPACITY = 2.0;

    // Wattage ranges for power classification
    public static final int LOW_POWER_MAX = 800;
    public static final int MEDIUM_POWER_MAX = 1200;
    public static final int HIGH_POWER_MIN = 1200;

    /**
     * Constructor for Microwave
     *
     * @param itemNumber The unique item number (must start with '3')
     * @param brand The brand name
     * @param quantity The stock quantity
     * @param wattage The power consumption in watts
     * @param color The color
     * @param price The price
     * @param capacity The capacity in cubic feet
     * @param roomType The room type ('K' for Kitchen, 'W' for Work site)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Microwave(String itemNumber, String brand, int quantity, int wattage,
                     String color, double price, double capacity, char roomType) {
        // Call parent constructor
        super(itemNumber, brand, quantity, wattage, color, price);

        // Validate microwave-specific parameters
        validateMicrowaveParameters(itemNumber, capacity, roomType);

        this.capacity = capacity;
        this.roomType = Character.toUpperCase(roomType);
    }

    /**
     * Validates microwave-specific parameters
     *
     * @param itemNumber The item number to validate
     * @param capacity The capacity to validate
     * @param roomType The room type to validate
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validateMicrowaveParameters(String itemNumber, double capacity, char roomType) {
        // Validate item number starts with '3'
        if (itemNumber == null || itemNumber.isEmpty() || itemNumber.charAt(0) != '3') {
            throw new IllegalArgumentException("Microwave item number must start with '3'");
        }

        // Validate capacity
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (capacity > 5.0) {
            throw new IllegalArgumentException("Capacity cannot exceed 5.0 cubic feet");
        }

        // Validate room type
        char upperRoomType = Character.toUpperCase(roomType);
        if (!VALID_ROOM_TYPES.contains(upperRoomType)) {
            throw new IllegalArgumentException(
                    "Room type must be 'K' (Kitchen) or 'W' (Work site)");
        }
    }

    /**
     * Template Method implementation - adds microwave-specific details to string representation
     *
     * This method is called by the parent class's toString() method to append
     * microwave-specific information to the standard appliance information.
     *
     * @param sb StringBuilder to append microwave-specific details
     */
    @Override
    protected void appendSpecificDetails(StringBuilder sb) {
        sb.append("Capacity: ").append(capacity).append(" cu. ft.\n");
        sb.append("Room Type: ").append(getRoomTypeDescription()).append("\n");
        sb.append("Size Category: ").append(getSizeCategory()).append("\n");
        sb.append("Power Level: ").append(getPowerLevel()).append("\n");
        sb.append("Cooking Features: ").append(getCookingFeatures());
    }

    /**
     * Factory Method implementation - formats microwave data for file storage
     *
     * Creates a semicolon-separated string in the format required for the appliances.txt file.
     * Format: ItemNumber;Brand;Quantity;Wattage;Color;Price;Capacity;RoomType
     *
     * @return Formatted string suitable for file storage
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
                String.valueOf(capacity),
                String.valueOf(roomType)
        );
    }

    /**
     * Gets the capacity in cubic feet
     *
     * @return The capacity in cubic feet
     */
    public double getCapacity() {
        return capacity;
    }

    /**
     * Gets the room type character
     *
     * @return The room type ('K' or 'W')
     */
    public char getRoomType() {
        return roomType;
    }

    /**
     * Gets a descriptive name for the room type
     *
     * @return Human-readable description of room type
     */
    public String getRoomTypeDescription() {
        switch (roomType) {
            case KITCHEN: return "Kitchen";
            case WORK_SITE: return "Work Site";
            default: return "Unknown";
        }
    }

    /**
     * Gets the size category based on capacity
     *
     * @return Size category description
     */
    public String getSizeCategory() {
        if (capacity <= COMPACT_MAX_CAPACITY) {
            return "Compact";
        } else if (capacity <= STANDARD_MAX_CAPACITY) {
            return "Standard";
        } else {
            return "Large";
        }
    }

    /**
     * Gets the power level classification based on wattage
     *
     * @return Power level description
     */
    public String getPowerLevel() {
        if (wattage <= LOW_POWER_MAX) {
            return "Low Power";
        } else if (wattage <= MEDIUM_POWER_MAX) {
            return "Medium Power";
        } else {
            return "High Power";
        }
    }

    /**
     * Gets estimated cooking features based on wattage and capacity
     *
     * @return Description of available cooking features
     */
    public String getCookingFeatures() {
        StringBuilder features = new StringBuilder();

        // Basic features for all microwaves
        features.append("Reheat, Defrost");

        // Add features based on power level
        if (wattage >= 900) {
            features.append(", Quick Cook");
        }

        if (wattage >= 1100) {
            features.append(", Sensor Cook");
        }

        if (wattage >= 1300) {
            features.append(", Convection");
        }

        // Add features based on capacity
        if (capacity >= 1.5) {
            features.append(", Large Item Cooking");
        }

        // Add features based on room type
        if (roomType == WORK_SITE) {
            features.append(", Heavy-Duty Use");
        }

        return features.toString();
    }

    /**
     * Checks if this microwave is suitable for commercial use
     *
     * @return true if suitable for commercial/work site use
     */
    public boolean isCommercialGrade() {
        return roomType == WORK_SITE && wattage >= 1000;
    }

    /**
     * Checks if this is a compact microwave
     *
     * @return true if capacity is 1.0 cu. ft. or less
     */
    public boolean isCompact() {
        return capacity <= COMPACT_MAX_CAPACITY;
    }

    /**
     * Checks if this microwave has high power
     *
     * @return true if wattage is 1200 or higher
     */
    public boolean isHighPower() {
        return wattage >= HIGH_POWER_MIN;
    }

    /**
     * Estimates cooking time for a standard item based on wattage
     *
     * @param standardMinutes Base cooking time for 1000W microwave
     * @return Adjusted cooking time for this microwave's wattage
     */
    public double estimateCookingTime(double standardMinutes) {
        // Cooking time is inversely proportional to wattage
        final double STANDARD_WATTAGE = 1000.0;
        return (standardMinutes * STANDARD_WATTAGE) / wattage;
    }

    /**
     * Gets energy efficiency rating based on wattage and capacity ratio
     *
     * @return Energy efficiency rating
     */
    public String getEnergyEfficiency() {
        double wattsPerCubicFoot = wattage / capacity;

        if (wattsPerCubicFoot <= 600) {
            return "High Efficiency";
        } else if (wattsPerCubicFoot <= 800) {
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
        return String.format("%s %s %s Microwave - %.1f cu. ft., %dW, %s Use, %s",
                brand,
                getSizeCategory(),
                getPowerLevel(),
                capacity,
                wattage,
                getRoomTypeDescription(),
                getAvailabilityStatus()
        );
    }

    /**
     * Gets installation recommendations based on room type and capacity
     *
     * @return Installation recommendation string
     */
    public String getInstallationRecommendation() {
        StringBuilder recommendation = new StringBuilder();

        if (roomType == KITCHEN) {
            if (isCompact()) {
                recommendation.append("Perfect for countertop installation");
            } else if (capacity >= 2.0) {
                recommendation.append("Suitable for over-range or built-in installation");
            } else {
                recommendation.append("Ideal for countertop or microwave cart");
            }
        } else { // WORK_SITE
            recommendation.append("Designed for commercial/industrial environments");
            if (wattage >= 1300) {
                recommendation.append(", requires dedicated electrical circuit");
            }
        }

        return recommendation.toString();
    }

    /**
     * Compares microwaves by capacity
     *
     * @param other The other microwave to compare with
     * @return negative if this is smaller, positive if larger, 0 if equal
     */
    public int compareByCapacity(Microwave other) {
        return Double.compare(this.capacity, other.capacity);
    }

    /**
     * Compares microwaves by power efficiency (wattage per cubic foot)
     *
     * @param other The other microwave to compare with
     * @return negative if this is more efficient, positive if less efficient, 0 if equal
     */
    public int compareByEfficiency(Microwave other) {
        double thisEfficiency = this.wattage / this.capacity;
        double otherEfficiency = other.wattage / other.capacity;
        return Double.compare(thisEfficiency, otherEfficiency);
    }

    /**
     * Checks if this microwave matches the specified room type filter
     *
     * @param roomTypeFilter The room type to filter by ('K' or 'W')
     * @return true if this microwave matches the room type filter
     */
    public boolean matchesRoomTypeFilter(char roomTypeFilter) {
        return this.roomType == Character.toUpperCase(roomTypeFilter);
    }

    /**
     * Checks if this microwave matches the specified room type filter (string version)
     *
     * @param roomTypeFilter The room type to filter by ("K", "Kitchen", "W", "Work", etc.)
     * @return true if this microwave matches the room type filter
     */
    public boolean matchesRoomTypeFilter(String roomTypeFilter) {
        if (roomTypeFilter == null || roomTypeFilter.isEmpty()) {
            return false;
        }

        String filter = roomTypeFilter.toUpperCase();

        // Check for exact character match
        if (filter.length() == 1) {
            return this.roomType == filter.charAt(0);
        }

        // Check for full word match
        return (this.roomType == KITCHEN && filter.startsWith("K")) ||
                (this.roomType == WORK_SITE && filter.startsWith("W"));
    }

    /**
     * Gets all valid room type options
     *
     * @return List of valid room type characters
     */
    public static List<Character> getValidRoomTypes() {
        return VALID_ROOM_TYPES;
    }
}