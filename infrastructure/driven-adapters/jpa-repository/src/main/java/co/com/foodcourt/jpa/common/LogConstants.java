package co.com.foodcourt.jpa.common;

public enum LogConstants {
    SAVE_USER("Saving user: {}"),
    USER_SAVED("User saved successfully with id: {}"),
    ROLE_NOT_FOUND("Role not found with id: ");

    private final String message;

    LogConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
