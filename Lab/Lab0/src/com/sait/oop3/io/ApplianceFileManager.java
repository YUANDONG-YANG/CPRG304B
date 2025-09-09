package com.sait.oop3.io;

import com.sait.oop3.appliances.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File manager class responsible for reading and writing appliance data
 *
 * This class handles all file I/O operations for the appliance management system,
 * including loading appliances from file and saving them back to file.
 * Uses dynamic resource loading with classpath-first approach and filesystem fallback.
 *
 * @author Your Name
 * @version 2.0
 */
public class ApplianceFileManager {

    private static final String FILENAME = "appliances.txt";
    private static final boolean DEBUG = true;

    // Possible filesystem paths to search for the file
    private static final String[] POSSIBLE_PATHS = {
            "Lab/Lab0/src/resources/",
            ""
    };

    /**
     * Loads appliances using dynamic resource detection
     * First tries classpath, then falls back to filesystem search
     *
     * @return List of loaded appliances
     */
    public static List<Appliance> loadAppliances() {
        try (InputStream is = getResourceStream()) {
            return parseAppliances(is);
        } catch (IOException e) {
            System.err.println("Error loading appliances: " + e.getMessage());
            if (DEBUG) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }
    }

    /**
     * Gets input stream for the appliances file
     * Priority: 1) Classpath resource, 2) Filesystem search
     *
     * @return InputStream for the appliances file
     * @throws IOException if file cannot be found or opened
     */
    private static InputStream getResourceStream() throws IOException {
        // First try classpath resource
        InputStream is = ApplianceFileManager.class
                .getClassLoader().getResourceAsStream(FILENAME);

        if (is != null) {
            if (DEBUG) {
                System.out.println("Found " + FILENAME + " in classpath");
            }
            return is;
        }

        if (DEBUG) {
            System.out.println(FILENAME + " not found in classpath, searching filesystem...");
        }

        // Fallback to filesystem search
        return getFileStream();
    }

    /**
     * Searches filesystem for the appliances file
     *
     * @return FileInputStream for the found file
     * @throws IOException if file cannot be found
     */
    private static InputStream getFileStream() throws IOException {
        File file = findAppliancesFile();
        return new FileInputStream(file);
    }

    /**
     * Searches for appliances file in known locations
     *
     * @return File object for the appliances file
     * @throws IOException if file cannot be found in any location
     */
    private static File findAppliancesFile() throws IOException {
        String currentDir = System.getProperty("user.dir");

        if (DEBUG) {
            System.out.println("Searching for " + FILENAME + " from: " + currentDir);
        }

        for (String path : POSSIBLE_PATHS) {
            File file = new File(currentDir, path + FILENAME);
            if (DEBUG) {
                System.out.println("Checking: " + file.getAbsolutePath());
            }

            if (file.exists() && file.canRead()) {
                if (DEBUG) {
                    System.out.println("Found appliances file at: " + file.getAbsolutePath());
                }
                return file;
            }
        }

        throw new IOException("Could not find " + FILENAME + " in any expected location. Searched paths: " +
                String.join(", ", POSSIBLE_PATHS));
    }

    /**
     * Parses appliances from an input stream
     *
     * @param inputStream The input stream to read from
     * @return List of parsed appliances
     * @throws IOException if reading fails
     */
    private static List<Appliance> parseAppliances(InputStream inputStream) throws IOException {
        List<Appliance> appliances = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int lineNumber = 0;
            int successCount = 0;
            int errorCount = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                // 跳过空行
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (DEBUG) {
                    System.out.println("Reading line " + lineNumber + ": " + line);
                }

                try {
                    Appliance appliance = parseApplianceFromLine(line);
                    if (appliance != null) {
                        appliances.add(appliance);
                        successCount++;
                        if (DEBUG) {
                            System.out.println("Successfully created: " + appliance.getBrand() +
                                    " " + appliance.getApplianceType());
                        }
                    } else {
                        errorCount++;
                        System.err.println("Failed to create appliance from line " + lineNumber);
                    }
                } catch (Exception e) {
                    errorCount++;
                    System.err.println("Error parsing line " + lineNumber + ": " + e.getMessage());
                }
            }

            System.out.println("Loading complete:");
            System.out.println("- Successfully loaded: " + successCount + " appliances");
            System.out.println("- Errors encountered: " + errorCount + " lines");
            System.out.println("- Total appliances in memory: " + appliances.size());

        }

        return appliances;
    }

    /**
     * Parses a single line from the file and creates the appropriate appliance object
     *
     * @param line Line from the file
     * @return Created appliance object or null if parsing fails
     */
    private static Appliance parseApplianceFromLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(";");
        if (DEBUG) {
            System.out.println("Split into " + parts.length + " parts");
        }

        if (parts.length < 8) {
            System.err.println("Invalid line format (need at least 8 fields): " + line);
            return null;
        }

        try {
            // 提取共同属性
            String itemNumber = parts[0].trim();
            String brand = parts[1].trim();
            int quantity = Integer.parseInt(parts[2].trim());
            int wattage = Integer.parseInt(parts[3].trim());
            String color = parts[4].trim();
            double price = Double.parseDouble(parts[5].trim());

            // 从第一位数字确定电器类型
            if (itemNumber.isEmpty()) {
                System.err.println("Empty item number in line: " + line);
                return null;
            }

            char firstDigit = itemNumber.charAt(0);
            if (DEBUG) {
                System.out.println("Creating appliance type: " + firstDigit + " for item: " + itemNumber);
            }

            switch (firstDigit) {
                case '1': // 冰箱
                    return createRefrigerator(parts, itemNumber, brand, quantity, wattage, color, price, line);

                case '2': // 吸尘器
                    return createVacuum(parts, itemNumber, brand, quantity, wattage, color, price, line);

                case '3': // 微波炉
                    return createMicrowave(parts, itemNumber, brand, quantity, wattage, color, price, line);

                case '4':
                case '5': // 洗碗机
                    return createDishwasher(parts, itemNumber, brand, quantity, wattage, color, price, line);

                default:
                    System.err.println("Unknown appliance type '" + firstDigit + "' for item: " + itemNumber);
                    return null;
            }

        } catch (NumberFormatException e) {
            System.err.println("Error parsing numeric values in line: " + line + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error parsing line: " + line + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a Refrigerator object
     */
    private static Refrigerator createRefrigerator(String[] parts, String itemNumber,
                                                   String brand, int quantity, int wattage, String color, double price, String originalLine) {

        if (parts.length < 9) {
            System.err.println("Refrigerator needs 9 fields, got " + parts.length + ": " + originalLine);
            return null;
        }

        try {
            int numberOfDoors = Integer.parseInt(parts[6].trim());
            int height = Integer.parseInt(parts[7].trim());
            int width = Integer.parseInt(parts[8].trim());

            return new Refrigerator(itemNumber, brand, quantity, wattage, color, price,
                    numberOfDoors, height, width);

        } catch (Exception e) {
            System.err.println("Error creating Refrigerator: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a Vacuum object
     */
    private static Vacuum createVacuum(String[] parts, String itemNumber,
                                       String brand, int quantity, int wattage, String color, double price, String originalLine) {

        if (parts.length < 8) {
            System.err.println("Vacuum needs 8 fields, got " + parts.length + ": " + originalLine);
            return null;
        }

        try {
            String grade = parts[6].trim();
            int batteryVoltage = Integer.parseInt(parts[7].trim());

            return new Vacuum(itemNumber, brand, quantity, wattage, color, price, grade, batteryVoltage);

        } catch (Exception e) {
            System.err.println("Error creating Vacuum: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a Microwave object
     */
    private static Microwave createMicrowave(String[] parts, String itemNumber,
                                             String brand, int quantity, int wattage, String color, double price, String originalLine) {

        if (parts.length < 8) {
            System.err.println("Microwave needs 8 fields, got " + parts.length + ": " + originalLine);
            return null;
        }

        try {
            double capacity = Double.parseDouble(parts[6].trim());
            String roomTypeStr = parts[7].trim();

            if (roomTypeStr.isEmpty()) {
                System.err.println("Empty room type for microwave: " + originalLine);
                return null;
            }

            char roomType = roomTypeStr.charAt(0);

            return new Microwave(itemNumber, brand, quantity, wattage, color, price, capacity, roomType);

        } catch (Exception e) {
            System.err.println("Error creating Microwave: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a Dishwasher object
     */
    private static Dishwasher createDishwasher(String[] parts, String itemNumber,
                                               String brand, int quantity, int wattage, String color, double price, String originalLine) {

        if (parts.length < 8) {
            System.err.println("Dishwasher needs 8 fields, got " + parts.length + ": " + originalLine);
            return null;
        }

        try {
            String feature = parts[6].trim();
            String soundRating = parts[7].trim();

            // 处理可能因缺少分号导致声音评级为空的情况
            if (soundRating.isEmpty() && parts.length == 8) {
                System.err.println("Warning: Empty sound rating for dishwasher, line might be missing final semicolon: " + originalLine);
            }

            return new Dishwasher(itemNumber, brand, quantity, wattage, color, price, feature, soundRating);

        } catch (Exception e) {
            System.err.println("Error creating Dishwasher: " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves appliances to file (uses first writable location found)
     *
     * @param appliances List of appliances to save
     * @return true if save was successful, false otherwise
     */
    public static boolean saveAppliances(List<Appliance> appliances) {
        try {
            File outputFile = findWritableLocation();
            return saveAppliancesToFile(appliances, outputFile);
        } catch (IOException e) {
            System.err.println("Error finding writable location for save: " + e.getMessage());
            return false;
        }
    }

    /**
     * Finds a writable location for saving the file
     *
     * @return File object for writable location
     * @throws IOException if no writable location found
     */
    private static File findWritableLocation() throws IOException {
        String currentDir = System.getProperty("user.dir");

        for (String path : POSSIBLE_PATHS) {
            File dir = new File(currentDir, path);
            File file = new File(dir, FILENAME);

            // Check if directory exists or can be created
            if (dir.exists() || dir.mkdirs()) {
                // Check if we can write to this location
                if (dir.canWrite()) {
                    if (DEBUG) {
                        System.out.println("Will save to: " + file.getAbsolutePath());
                    }
                    return file;
                }
            }
        }

        // Fallback to current directory
        File fallbackFile = new File(currentDir, FILENAME);
        if (DEBUG) {
            System.out.println("Using fallback save location: " + fallbackFile.getAbsolutePath());
        }
        return fallbackFile;
    }

    /**
     * Saves appliances to the specified file
     * Only saves appliances with quantity > 0 (removes sold out items)
     * This overwrites the original file with updated data
     *
     * @param appliances List of appliances to save
     * @param file File to save to
     * @return true if save was successful, false otherwise
     */
    private static boolean saveAppliancesToFile(List<Appliance> appliances, File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            int savedCount = 0;
            int removedCount = 0;

            for (Appliance appliance : appliances) {
                if (appliance.getQuantity() > 0) {
                    // Only save appliances with stock > 0
                    writer.println(appliance.toFileFormat());
                    savedCount++;
                } else {
                    // Count sold out items that won't be saved
                    removedCount++;
                    if (DEBUG) {
                        System.out.println("Removing sold out item: " + appliance.getItemNumber() + " - " + appliance.getBrand());
                    }
                }
            }

            System.out.println("Successfully saved " + savedCount + " appliances to " + file.getAbsolutePath());
            if (removedCount > 0) {
                System.out.println("Removed " + removedCount + " sold out appliances from file.");
            }

            if (DEBUG) {
                System.out.println("Original file has been overwritten with updated data.");
            }

            return true;

        } catch (IOException e) {
            System.err.println("Error saving appliances to file: " + e.getMessage());
            if (DEBUG) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Checks if the appliances file exists in any known location
     *
     * @return true if file exists, false otherwise
     */
    public static boolean fileExists() {
        try {
            // Check classpath first
            InputStream is = ApplianceFileManager.class
                    .getClassLoader().getResourceAsStream(FILENAME);
            if (is != null) {
                is.close();
                return true;
            }

            // Check filesystem
            findAppliancesFile();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Gets the path where the appliances file was found or would be saved
     *
     * @return Path of the file
     */
    public static String getFilePath() {
        try {
            // Try to find existing file first
            File file = findAppliancesFile();
            return file.getAbsolutePath();
        } catch (IOException e) {
            // Return potential save location
            try {
                File file = findWritableLocation();
                return file.getAbsolutePath();
            } catch (IOException ex) {
                return new File(System.getProperty("user.dir"), FILENAME).getAbsolutePath();
            }
        }
    }
}