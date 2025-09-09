package com.sait.oop3.appliances;

import java.util.Arrays;
import java.util.List;

/**
 * Vacuum class - Extends Appliance base class
 * Updated to match Lab 0 expected output format
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

    /**
     * Constructor for Vacuum
     */
    public Vacuum(String itemNumber, String brand, int quantity, int wattage,
                  String color, double price, String grade, int batteryVoltage) {
        super(itemNumber, brand, quantity, wattage, color, price);
        validateVacuumParameters(itemNumber, grade, batteryVoltage);
        this.grade = grade;
        this.batteryVoltage = batteryVoltage;
    }

    /**
     * Validates vacuum-specific parameters
     */
    private void validateVacuumParameters(String itemNumber, String grade, int batteryVoltage) {
        if (itemNumber == null || itemNumber.isEmpty() || itemNumber.charAt(0) != '2') {
            throw new IllegalArgumentException("Vacuum item number must start with '2'");
        }
        if (grade == null || grade.trim().isEmpty()) {
            throw new IllegalArgumentException("Grade cannot be null or empty");
        }
        if (!VALID_VOLTAGES.contains(batteryVoltage)) {
            throw new IllegalArgumentException(
                    "Battery voltage must be " + LOW_VOLTAGE + "V or " + HIGH_VOLTAGE + "V");
        }
    }

    /**
     * Template Method implementation - adds vacuum-specific details to string representation
     * Updated to match expected output format from Lab 0
     */
    @Override
    protected void appendSpecificDetails(StringBuilder sb) {
        sb.append("Grade: ").append(grade).append("\n");
        sb.append("Battery voltage: ").append(getVoltageDescription());
    }

    /**
     * Gets voltage description matching expected output format
     * @return "Low" for 18V, "High" for 24V
     */
    public String getVoltageDescription() {
        return batteryVoltage == LOW_VOLTAGE ? "Low" : "High";
    }

    /**
     * Factory Method implementation - formats vacuum data for file storage
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

    // Getter methods
    public String getGrade() {
        return grade;
    }

    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    public boolean isHighVoltage() {
        return batteryVoltage == HIGH_VOLTAGE;
    }

    public boolean isLowVoltage() {
        return batteryVoltage == LOW_VOLTAGE;
    }

    /**
     * Checks if this vacuum matches the specified voltage filter
     */
    public boolean matchesVoltageFilter(int voltageFilter) {
        return this.batteryVoltage == voltageFilter;
    }

    /**
     * Gets all valid voltage options
     */
    public static List<Integer> getValidVoltages() {
        return VALID_VOLTAGES;
    }
}