package co.com.foodcourt.api.common;

public enum LogConstants {
    CREATE_USER_REQUEST("Request to create user: {}"),
    CREATE_USER_SUCCESS("User created successfully with id: {}"),
    TIMESTAMP("timestamp:"),
    ERROR("error:"),
    DETAILS("details:");


    private final String message;

    LogConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
