package co.com.foodcourt.jpa.common;

public enum LogConstants {
    SAVE_USER("Saving user: {}"),
    USER_SAVED("User saved successfully with id: {}"),
    GET_USER("Finding user with id: {}"),
    USER_FOUND("User found with id: {}"),
    GET_USER_BY_EMAIL("Finding user with id: {}"),
    USER_FOUND_BY_EMAIL("User found with id: {}");



    private final String message;

    LogConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
