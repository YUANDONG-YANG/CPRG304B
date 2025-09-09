package com.sait.oop3.appliances;

import java.util.Arrays;
import java.util.List;

/**
 * Dishwasher class - Extends Appliance base class
 *
 * Represents a dishwasher appliance with specific attributes such as feature and sound rating.
 * Item numbers for dishwashers start with digit '4' or '5'.
 *
 * Design Patterns Implemented:
 * - Template Method: Implements appendSpecificDetails() to add dishwasher-specific information
 * - Factory Method: Implements toFileFormat() for dishwasher-specific file formatting
 *
 * File Format: ItemNumber;Brand;Quantity;Wattage;Color;Price;Feature;SoundRating
 * Example: 587065284;Kenwood;74;1010;silver;390;Clean with Steam;Qu;
 *
 * Sound Rating Options:
 * - Qt (Quietest)
 * - Qr (Quieter)
 * - Qu (Quiet)
 * - M (Moderate)
 *
 * @author Yuandong Yang
 */
public class Dishwasher extends Appliance {

    // Dishwasher-specific attributes
    private String feature;        // Special feature (e.g., "Clean with Steam")
    private String soundRating;    // Sound rating (Qt, Qr, Qu, M)

    // Valid sound rating constants
    public static final String QUIETEST = "Qt";
    public static final String QUIETER = "Qr";
    public static final String QUIET = "Qu";
    public static final String MODERATE = "M";

    // List of valid sound ratings for validation
    private static final List<String> VALID_SOUND_RATINGS = Arrays.asList(
            QUIETEST, QUIETER, QUIET, MODERATE
    );

    // Valid item number prefixes
    private static final List<Character> VALID_PREFIXES = Arrays.asList('4', '5');

    /**
     * Constructor for Dishwasher
     *
     * @param itemNumber The unique item number (must start with '4' or '5')
     * @param brand The brand name
     * @param quantity The stock quantity
     * @param wattage The power consumption in watts
     * @param color The color
     * @param price The price
     * @param feature The special feature description
     * @param soundRating The sound rating (Qt, Qr, Qu, or M)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Dishwasher(String itemNumber, String brand, int quantity, int wattage,
                      String color, double price, String feature, String soundRating) {
        // Call parent constructor
        super(itemNumber, brand, quantity, wattage, color, price);

        // Validate dishwasher-specific parameters
        validateDishwasherParameters(itemNumber, feature, soundRating);

        this.feature = feature;
        this.soundRating = soundRating;
    }

    /**
     * Validates dishwasher-specific parameters
     *
     * @param itemNumber The item number to validate
     * @param feature The feature to validate
     * @param soundRating The sound rating to validate
     * @throws IllegalArgumentException if any parameter is invalid
     */
    private void validateDishwasherParameters(String itemNumber, String feature, String soundRating) {
        // Validate item number starts with '4' or '5'
        if (itemNumber == null || itemNumber.isEmpty() ||
                !VALID_PREFIXES.contains(itemNumber.charAt(0))) {
            throw new IllegalArgumentException("Dishwasher item number must start with '4' or '5'");
        }

        // Validate feature
        if (feature == null || feature.trim().isEmpty()) {
            throw new IllegalArgumentException("Feature cannot be null or empty");
        }

        // Validate sound rating
        if (soundRating == null || !VALID_SOUND_RATINGS.contains(soundRating)) {
            throw new IllegalArgumentException(
                    "Sound rating must be one of: " + String.join(", ", VALID_SOUND_RATINGS));
        }
    }

    /**
     * Template Method implementation - adds dishwasher-specific details to string representation
     *
     * This method is called by the parent class's toString() method to append
     * dishwasher-specific information to the standard appliance information.
     *
     * @param sb StringBuilder to append dishwasher-specific details
     */
    @Override
    protected void appendSpecificDetails(StringBuilder sb) {
        sb.append("Feature: ").append(feature).append("\n");
        sb.append("Sound Rating: ").append(soundRating);
        sb.append(" (").append(getSoundRatingDescription()).append(")\n");
        sb.append("Noise Level: ").append(getNoiseLevel()).append("\n");
        sb.append("Energy Class: ").append(getEnergyClass());
    }

    /**
     * Factory Method implementation - formats dishwasher data for file storage
     *
     * Creates a semicolon-separated string in the format required for the appliances.txt file.
     * Format: ItemNumber;Brand;Quantity;Wattage;Color;Price;Feature;SoundRating
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
                feature,
                soundRating
        );
    }

    /**
     * Gets the special feature
     *
     * @return The special feature description
     */
    public String getFeature() {
        return feature;
    }

    /**
     * Gets the sound rating code
     *
     * @return The sound rating code (Qt, Qr, Qu, or M)
     */
    public String getSoundRating() {
        return soundRating;
    }

    /**
     * Gets a descriptive name for the sound rating
     *
     * @return Human-readable description of sound rating
     */
    public String getSoundRatingDescription() {
        switch (soundRating) {
            case QUIETEST: return "Quietest";
            case QUIETER: return "Quieter";
            case QUIET: return "Quiet";
            case MODERATE: return "Moderate";
            default: return "Unknown";
        }
    }

    /**
     * Gets the estimated decibel level based on sound rating
     * This is a simplified estimation for demonstration purposes
     *
     * @return Estimated decibel level description
     */
    public String getNoiseLevel() {
        switch (soundRating) {
            case QUIETEST: return "< 40 dB";
            case QUIETER: return "40-44 dB";
            case QUIET: return "45-49 dB";
            case MODERATE: return "50-54 dB";
            default: return "Unknown";
        }
    }

    /**
     * Gets energy efficiency class based on wattage
     * This is a simplified categorization for demonstration
     *
     * @return Energy efficiency class
     */
    public String getEnergyClass() {
        if (wattage <= 800) {
            return "A+++";
        } else if (wattage <= 1000) {
            return "A++";
        } else if (wattage <= 1200) {
            return "A+";
        } else if (wattage <= 1400) {
            return "A";
        } else {
            return "B";
        }
    }

    /**
     * Checks if this dishwasher is considered quiet (Qt, Qr, or Qu ratings)
     *
     * @return true if dishwasher has a quiet rating
     */
    public boolean isQuietOperation() {
        return QUIETEST.equals(soundRating) ||
                QUIETER.equals(soundRating) ||
                QUIET.equals(soundRating);
    }

    /**
     * Checks if this dishwasher is energy efficient (A+ or better)
     *
     * @return true if dishwasher is energy efficient
     */
    public boolean isEnergyEfficient() {
        String energyClass = getEnergyClass();
        return energyClass.startsWith("A+");
    }

    /**
     * Checks if this dishwasher has steam cleaning feature
     *
     * @return true if feature contains "steam" (case-insensitive)
     */
    public boolean hasSteamCleaning() {
        return feature.toLowerCase().contains("steam");
    }

    /**
     * Gets the dishwasher category based on item number prefix
     *
     * @return Category description
     */
    public String getCategory() {
        if (itemNumber != null && !itemNumber.isEmpty()) {
            char prefix = itemNumber.charAt(0);
            switch (prefix) {
                case '4': return "Built-in Dishwasher";
                case '5': return "Portable Dishwasher";
                default: return "Unknown Category";
            }
        }
        return "Unknown Category";
    }

    /**
     * Creates a detailed description for customer display
     *
     * @return Detailed customer-friendly description
     */
    public String getCustomerDescription() {
        return String.format("%s %s - %s, %s Sound Level, %s Energy Class, %s",
                brand,
                getCategory(),
                color,
                getSoundRatingDescription(),
                getEnergyClass(),
                getAvailabilityStatus()
        );
    }

    /**
     * Gets a summary of key selling points
     *
     * @return String with key features highlighted
     */
    public String getSellingPoints() {
        StringBuilder points = new StringBuilder();

        if (isQuietOperation()) {
            points.append("âœ“ Quiet Operation ");
        }

        if (isEnergyEfficient()) {
            points.append("âœ“ Energy Efficient ");
        }

        if (hasSteamCleaning()) {
            points.append("âœ“ Steam Cleaning ");
        }

        points.append("âœ“ ").append(feature);

        return points.toString();
    }

    /**
     * Compares dishwashers by sound level (quieter is better)
     *
     * @param other The other dishwasher to compare with
     * @return negative if this is quieter, positive if louder, 0 if equal
     */
    public int compareBySoundLevel(Dishwasher other) {
        // Create ranking where lower number = quieter
        int thisRank = getSoundRank(this.soundRating);
        int otherRank = getSoundRank(other.soundRating);
        return Integer.compare(thisRank, otherRank);
    }

    /**
     * Helper method to get numeric ranking for sound ratings
     *
     * @param rating The sound rating
     * @return Numeric rank (1 = quietest, 4 = loudest)
     */
    private int getSoundRank(String rating) {
        switch (rating) {
            case QUIETEST: return 1;
            case QUIETER: return 2;
            case QUIET: return 3;
            case MODERATE: return 4;
            default: return 5;
        }
    }

    /**
     * Checks if this dishwasher matches the specified sound rating filter
     *
     * @param soundRatingFilter The sound rating to filter by
     * @return true if this dishwasher matches the sound rating filter
     */
    public boolean matchesSoundRatingFilter(String soundRatingFilter) {
        return this.soundRating.equalsIgnoreCase(soundRatingFilter);
    }

    /**
     * Gets all available sound rating options
     *
     * @return List of valid sound rating codes
     */
    public static List<String> getValidSoundRatings() {
        return VALID_SOUND_RATINGS;
    }
}