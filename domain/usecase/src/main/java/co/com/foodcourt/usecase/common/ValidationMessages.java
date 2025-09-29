package co.com.foodcourt.usecase.common;

public enum ValidationMessages {
    INVALID_EMAIL("Invalid email format"),
    INVALID_PHONE("Invalid phone number. It must be up to 13 digits and may start with +"),
    INVALID_DOCUMENT("Invalid document ID. Only numeric values are allowed"),
    INVALID_USER("User object is null"),
    INVALID_ROLE("Invalid user role"),
    INVALID_BIRTHDATE("User must be at least 18 years old");

    private final String message;

    ValidationMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
