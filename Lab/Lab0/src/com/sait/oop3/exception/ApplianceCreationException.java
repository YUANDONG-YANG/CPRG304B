package com.sait.oop3.exception;

/**
 * Custom exception class for appliance creation errors
 *
 * This exception is thrown when there are issues creating appliance objects
 * from file data, such as invalid formats, missing data, or parsing errors.
 *
 * Design Pattern: Exception Handling Pattern
 * - Provides specific exception type for appliance creation failures
 * - Supports both simple messages and chained exceptions
 * - Enhances error tracking and debugging capabilities
 *
 * @author Yuandong Yang
 */
public class ApplianceCreationException extends Exception {

    private static final long serialVersionUID = 1L;

    // Additional context information
    private String inputLine;
    private String applianceType;

    /**
     * Constructs a new ApplianceCreationException with the specified detail message
     *
     * @param message The detail message explaining the exception
     */
    public ApplianceCreationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ApplianceCreationException with the specified detail message and cause
     *
     * @param message The detail message explaining the exception
     * @param cause The cause of this exception (can be null)
     */
    public ApplianceCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ApplianceCreationException with detailed context information
     *
     * @param message The detail message explaining the exception
     * @param inputLine The original input line that caused the error
     * @param applianceType The type of appliance being created
     */
    public ApplianceCreationException(String message, String inputLine, String applianceType) {
        super(message);
        this.inputLine = inputLine;
        this.applianceType = applianceType;
    }

    /**
     * Constructs a new ApplianceCreationException with detailed context and cause
     *
     * @param message The detail message explaining the exception
     * @param inputLine The original input line that caused the error
     * @param applianceType The type of appliance being created
     * @param cause The cause of this exception
     */
    public ApplianceCreationException(String message, String inputLine, String applianceType, Throwable cause) {
        super(message, cause);
        this.inputLine = inputLine;
        this.applianceType = applianceType;
    }

    /**
     * Gets the input line that caused the exception
     *
     * @return The input line, or null if not specified
     */
    public String getInputLine() {
        return inputLine;
    }

    /**
     * Gets the appliance type that was being created
     *
     * @return The appliance type, or null if not specified
     */
    public String getApplianceType() {
        return applianceType;
    }

    /**
     * Creates a detailed error message including context information
     *
     * @return Enhanced error message with context
     */
    @Override
    public String getMessage() {
        StringBuilder message = new StringBuilder(super.getMessage());

        if (applianceType != null) {
            message.append(" [Appliance Type: ").append(applianceType).append("]");
        }

        if (inputLine != null) {
            message.append(" [Input: ").append(inputLine).append("]");
        }

        return message.toString();
    }
}