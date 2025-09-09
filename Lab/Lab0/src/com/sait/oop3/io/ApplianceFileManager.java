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
 *
 * @author Your Name
 * @version 1.0
 */
public class ApplianceFileManager {

    private static final String FILENAME = "appliances.txt";
    private static final boolean DEBUG = true; // 设置为false以关闭详细调试输出

    /**
     * Gets a File object with the correct path to the resources directory
     *
     * @param filename The filename to use
     * @return File object with the correct path
     */
    private static File getFileWithPath(String filename) {
        String currentDir = System.getProperty("user.dir");
        return new File(currentDir, "src/resources/" + filename);
    }

    /**
     * Loads appliances from the default file
     *
     * @return List of loaded appliances
     */
    public static List<Appliance> loadAppliances() {
        return loadAppliances(FILENAME);
    }

    /**
     * Loads appliances from the specified file
     *
     * @param filename The filename to read from
     * @return List of loaded appliances
     */
    public static List<Appliance> loadAppliances(String filename) {
        List<Appliance> appliances = new ArrayList<>();

        // 获取正确的文件路径
        File file = getFileWithPath(filename);

        // 调试信息
        System.out.println("Looking for file at: " + file.getAbsolutePath());
        System.out.println("File exists: " + file.exists());
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        if (!file.exists()) {
            System.err.println("File not found: " + file.getAbsolutePath());
            System.out.println("Starting with empty appliance list...");
            return appliances;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) { // 使用file对象而非仅filename
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

        } catch (IOException e) {
            System.err.println("Error reading appliances file: " + e.getMessage());
            e.printStackTrace();
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
                // 可以在这里设置默认值
                // soundRating = "M"; // 默认为中等
            }

            return new Dishwasher(itemNumber, brand, quantity, wattage, color, price, feature, soundRating);

        } catch (Exception e) {
            System.err.println("Error creating Dishwasher: " + e.getMessage());
            return null;
        }
    }

    /**
     * Saves appliances to the default file
     *
     * @param appliances List of appliances to save
     * @return true if save was successful, false otherwise
     */
    public static boolean saveAppliances(List<Appliance> appliances) {
        return saveAppliances(appliances, FILENAME);
    }

    /**
     * Saves appliances to the specified file
     *
     * @param appliances List of appliances to save
     * @param filename The filename to save to
     * @return true if save was successful, false otherwise
     */
    public static boolean saveAppliances(List<Appliance> appliances, String filename) {
        // 获取正确的文件路径
        File file = getFileWithPath(filename);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) { // 使用file对象而非仅filename
            for (Appliance appliance : appliances) {
                writer.println(appliance.toFileFormat());
            }
            System.out.println("Successfully saved " + appliances.size() + " appliances to " + file.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.err.println("Error saving appliances to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if the appliances file exists
     *
     * @return true if file exists, false otherwise
     */
    public static boolean fileExists() {
        return fileExists(FILENAME);
    }

    /**
     * Checks if the specified file exists
     *
     * @param filename The filename to check
     * @return true if file exists, false otherwise
     */
    public static boolean fileExists(String filename) {
        return getFileWithPath(filename).exists(); // 使用一致的文件路径方法
    }

    /**
     * Gets the absolute path of the appliances file
     *
     * @return Absolute path of the file
     */
    public static String getFilePath() {
        return getFileWithPath(FILENAME).getAbsolutePath(); // 使用一致的文件路径方法
    }
}