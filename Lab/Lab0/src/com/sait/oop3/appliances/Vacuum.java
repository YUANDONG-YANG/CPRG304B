package com.sait.oop3.appliances;

import java.util.Arrays;
import java.util.List;

/**
 * Vacuum class - Extends Appliance base class
 *
 * Represents a vacuum cleaner appliance with specific attributes such as grade and battery voltage.
 * Item numbers for vacuums start with digit '2'.
 *
 * Design Patterns Implemented:
 * - Template Method: Implements appendSpecificDetails() to add vacuum-specific information
 * - Factory Method: Implements toFileFormat() for vacuum-specific file formatting
 *
 * File Format: ItemNumber;Brand;Quantity;Wattage;Color;Price;Grade;BatteryVoltage
 * Example: 263788832;Hoover;59;600;black;750;Residential;18;
 *
 * Battery Voltage Options:
 * - 18V (Low voltage - suitable for light cleaning)
 * - 24V (High voltage - suitable for heavy-duty cleaning)
 *
 * @author Yuandong Yang
 */
public class Vacuum extends Appliance {

    // Vacuum-specific attributes
    private String grade;           // Grade type (e.g., "Residential", "Commercial")
    private int batteryVoltage;     // Battery voltage (18V or 24V)

    // Battery voltage constants
    public static final int LOW_VOLTAGE = 18;
    public static final int HIGH_VOLTAGE = 24;

    // Valid battery voltages for validation
    private static final List<Integer> VALID_VOLTAGES = Arrays.asList(LOW_VOLTAGE, HIGH_VOLTAGE);

    // Common grade categories
    public static final String RESIDENTIAL = "Residential";
    public static final String COMMERCIAL = "Commercial";
    public static final String INDUSTRIAL = "Industrial";
    public static final String PROFESSIONAL = "Professional";

    // Power classification thresholds
    private static final int LIGHT_DUTY_MAX_WATTAGE = 400;
    private static final int MEDIUM_DUTY_MAX_WATTAGE = 800;
    private static final int HEAVY_DUTY_MIN_WATTAGE = 800;

    /**
     * Constructor for Vacuum
     *
     * @param itemNumber The unique item number (must start with '2')
     * @param brand The brand name
     * @param quantity The stock quantity
     * @param wattage The power consumption in watts
     * @param color The color
     * @param price The price
     * @param grade The grade type (e.g., "Residential", "Commercial")
     * @param batteryVoltage The battery voltage (18V or 24V)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Vacuum(String itemNumber, String brand, int quantity, int wattage,
                  String color, double price, String grade, int batteryVoltage) {
        // Call parent constructor
        super(itemNumber, brand, quantity, wattage, color, price);

        // Validate vacuum-specific parameters
        validateVacuumParameters(itemNumber, grade, batteryVoltage);

        this.grade = grade;
        this.batteryVoltage = batteryVoltage;
    }

    /**
     * Validates vacuum-specific parameters
     *
     * @param itemNumber The item number to validate
     * @param grade The grade to validate
     * @param batteryVoltage The battery voltage to validate
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validateVacuumParameters(String itemNumber, String grade, int batteryVoltage) {
        // Validate item number starts with '2'
        if (itemNumber == null || itemNumber.isEmpty() || itemNumber.charAt(0) != '2') {
            throw new IllegalArgumentException("Vacuum item number must start with '2'");
        }

        // Validate grade
        if (grade == null || grade.trim().isEmpty()) {
            throw new IllegalArgumentException("Grade cannot be null or empty");
        }

        // Validate battery voltage
        if (!VALID_VOLTAGES.contains(batteryVoltage)) {
            throw new IllegalArgumentException(
                    "Battery voltage must be " + LOW_VOLTAGE + "V or " + HIGH_VOLTAGE + "V");
        }
    }

    /**
     * Template Method implementation - adds vacuum-specific details to string representation
     *
     * This method is called by the parent class's toString() method to append
     * vacuum-specific information to the standard appliance information.
     *
     * @param sb StringBuilder to append vacuum-specific details
     */
    @Override
    protected void appendSpecificDetails(StringBuilder sb) {
        sb.append("Grade: ").append(grade).append("\n");
        sb.append("Battery Voltage: ").append(batteryVoltage).append("V");
        sb.append(" (").append(getVoltageDescription()).append(")\n");
        sb.append("Duty Classification: ").append(getDutyClassification()).append("\n");
        sb.append("Runtime: ").append(getEstimatedRuntime()).append(" minutes\n");
        sb.append("Suitable Areas: ").append(getSuitableAreas());
    }

    /**
     * Factory Method implementation - formats vacuum data for file storage
     *
     * Creates a semicolon-separated string in the format required for the appliances.txt file.
     * Format: ItemNumber;Brand;Quantity;Wattage;Color;Price;Grade;BatteryVoltage
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
                grade,
                String.valueOf(batteryVoltage)
        );
    }

    /**
     * Gets the grade classification
     *
     * @return The grade classification
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Gets the battery voltage
     *
     * @return The battery voltage in volts
     */
    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    /**
     * Gets a descriptive name for the voltage level
     *
     * @return Human-readable description of voltage level
     */
    public String getVoltageDescription() {
        switch (batteryVoltage) {
            case LOW_VOLTAGE: return "Low Power - Light Cleaning";
            case HIGH_VOLTAGE: return "High Power - Heavy Duty";
            default: return "Unknown Voltage";
        }
    }

    /**
     * Gets the duty classification based on wattage and grade
     *
     * @return Duty classification description
     */
    public String getDutyClassification() {
        if (isCommercialGrade()) {
            if (wattage >= HEAVY_DUTY_MIN_WATTAGE) {
                return "Heavy Duty Commercial";
            } else {
                return "Standard Commercial";
            }
        } else {
            if (wattage <= LIGHT_DUTY_MAX_WATTAGE) {
                return "Light Duty Residential";
            } else if (wattage <= MEDIUM_DUTY_MAX_WATTAGE) {
                return "Medium Duty Residential";
            } else {
                return "Heavy Duty Residential";
            }
        }
    }

    /**
     * Estimates battery runtime based on voltage and wattage
     * This is a simplified calculation for demonstration purposes
     *
     * @return Estimated runtime in minutes
     */
    public int getEstimatedRuntime() {
        // Higher voltage = longer runtime, higher wattage = shorter runtime
        // Base calculation: voltage factor / wattage factor
        double voltageFactor = batteryVoltage == HIGH_VOLTAGE ? 1.5 : 1.0;
        double wattageImpact = wattage / 100.0;

        int baseRuntime = 30; // Base 30 minutes
        int estimatedRuntime = (int) (baseRuntime * voltageFactor / (wattageImpact / 6.0));

        // Ensure reasonable bounds
        return Math.max(15, Math.min(estimatedRuntime, 120));
    }

    /**
     * Gets suitable cleaning areas based on grade and specifications
     *
     * @return Description of suitable cleaning areas
     */
    public String getSuitableAreas() {
        StringBuilder areas = new StringBuilder();

        if (isResidential()) {
            areas.append("Home, Apartments");
            if (batteryVoltage == HIGH_VOLTAGE) {
                areas.append(", Large Homes");
            }
        } else if (isCommercialGrade()) {
            areas.append("Offices, Retail Spaces");
            if (wattage >= HEAVY_DUTY_MIN_WATTAGE) {
                areas.append(", Warehouses, Industrial Areas");
            }
        }

        // Add specific area types based on voltage
        if (batteryVoltage == LOW_VOLTAGE) {
            areas.append(", Quick Cleanups");
        } else {
            areas.append(", Deep Cleaning, Pet Hair Removal");
        }

        return areas.toString();
    }

    /**
     * Checks if this is a residential grade vacuum
     *
     * @return true if grade indicates residential use
     */
    public boolean isResidential() {
        return grade.equalsIgnoreCase(RESIDENTIAL);
    }

    /**
     * Checks if this is a commercial grade vacuum
     *
     * @return true if grade indicates commercial/professional use
     */
    public boolean isCommercialGrade() {
        String lowerGrade = grade.toLowerCase();
        return lowerGrade.contains("commercial") ||
                lowerGrade.contains("professional") ||
                lowerGrade.contains("industrial");
    }

    /**
     * Checks if this vacuum has high voltage battery
     *
     * @return true if battery voltage is 24V
     */
    public boolean isHighVoltage() {
        return batteryVoltage == HIGH_VOLTAGE;
    }

    /**
     * Checks if this vacuum has low voltage battery
     *
     * @return true if battery voltage is 18V
     */
    public boolean isLowVoltage() {
        return batteryVoltage == LOW_VOLTAGE;
    }

    /**
     * Checks if this vacuum is suitable for pet owners
     * High voltage and adequate wattage for pet hair removal
     *
     * @return true if suitable for pet hair cleaning
     */
    public boolean isPetFriendly() {
        return batteryVoltage == HIGH_VOLTAGE && wattage >= 500;
    }

    /**
     * Checks if this vacuum is portable/cordless
     * Based on having battery voltage specification
     *
     * @return true if this is a cordless vacuum
     */
    public boolean isCordless() {
        // All vacuums with battery voltage are cordless
        return true;
    }

    /**
     * Gets power efficiency rating based on wattage and voltage
     *
     * @return Power efficiency description
     */
    public String getPowerEfficiency() {
        double efficiency = (double) batteryVoltage / wattage * 100;

        if (efficiency >= 4.0) {
            return "Excellent Efficiency";
        } else if (efficiency >= 3.0) {
            return "Good Efficiency";
        } else if (efficiency >= 2.0) {
            return "Average Efficiency";
        } else {
            return "Power Hungry";
        }
    }

    /**
     * Creates a detailed description for customer display
     *
     * @return Detailed customer-friendly description
     */
    public String getCustomerDescription() {
        return String.format("%s %s %dV Cordless Vacuum - %dW, %s, %s Runtime, %s",
                brand,
                grade,
                batteryVoltage,
                wattage,
                color,
                getEstimatedRuntime() + " min",
                getAvailabilityStatus()
        );
    }

    /**
     * Gets maintenance recommendations based on grade and usage
     *
     * @return Maintenance recommendation string
     */
    public String getMaintenanceRecommendation() {
        StringBuilder recommendation = new StringBuilder();

        if (isCommercialGrade()) {
            recommendation.append("Professional maintenance every 3-6 months");
            if (wattage >= HEAVY_DUTY_MIN_WATTAGE) {
                recommendation.append(", daily filter checks recommended");
            }
        } else {
            recommendation.append("Clean filters monthly, empty dust bin after each use");
        }

        if (batteryVoltage == HIGH_VOLTAGE) {
            recommendation.append(", charge battery fully between uses");
        }

        return recommendation.toString();
    }

    /**
     * Compares vacuums by battery voltage
     *
     * @param other The other vacuum to compare with
     * @return negative if this has lower voltage, positive if higher, 0 if equal
     */
    public int compareByVoltage(Vacuum other) {
        return Integer.compare(this.batteryVoltage, other.batteryVoltage);
    }

    /**
     * Compares vacuums by power efficiency
     *
     * @param other The other vacuum to compare with
     * @return negative if this is more efficient, positive if less efficient, 0 if equal
     */
    public int compareByEfficiency(Vacuum other) {
        double thisEfficiency = (double) this.batteryVoltage / this.wattage;
        double otherEfficiency = (double) other.batteryVoltage / other.wattage;
        return Double.compare(otherEfficiency, thisEfficiency); // Higher efficiency is better
    }

    /**
     * Compares vacuums by runtime
     *
     * @param other The other vacuum to compare with
     * @return negative if this has shorter runtime, positive if longer, 0 if equal
     */
    public int compareByRuntime(Vacuum other) {
        return Integer.compare(this.getEstimatedRuntime(), other.getEstimatedRuntime());
    }

    /**
     * Checks if this vacuum matches the specified voltage filter
     *
     * @param voltageFilter The voltage to filter by
     * @return true if this vacuum matches the voltage filter
     */
    public boolean matchesVoltageFilter(int voltageFilter) {
        return this.batteryVoltage == voltageFilter;
    }

    /**
     * Checks if this vacuum matches the specified grade filter
     *
     * @param gradeFilter The grade to filter by (case-insensitive)
     * @return true if this vacuum matches the grade filter
     */
    public boolean matchesGradeFilter(String gradeFilter) {
        return gradeFilter != null && this.grade.equalsIgnoreCase(gradeFilter);
    }

    /**
     * Gets all valid voltage options
     *
     * @return List of valid battery voltages
     */
    public static List<Integer> getValidVoltages() {
        return VALID_VOLTAGES;
    }

    /**
     * Gets recommended accessories based on vacuum specifications
     *
     * @return List of recommended accessories
     */
    public String getRecommendedAccessories() {
        StringBuilder accessories = new StringBuilder();
        accessories.append("Standard brush set");

        if (isPetFriendly()) {
            accessories.append(", Pet hair brush, Upholstery tool");
        }

        if (isCommercialGrade()) {
            accessories.append(", Extended warranty, Professional filters");
        }

        if (batteryVoltage == HIGH_VOLTAGE) {
            accessories.append(", Extra battery pack, Fast charger");
        }

        accessories.append(", Crevice tool, Dusting brush");

        return accessories.toString();
    }
}