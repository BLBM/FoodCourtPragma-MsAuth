package co.com.foodcourt.jpa.common;

public enum ErrorConstants {

    USER_DOCUMENT_KEY("users_document_id_key"),
    USER_EMAIL_KEY("users_email_key"),
    DOCUMENT_ID_EXISTS("Document ID already exists"),
    EMAIL_EXISTS("Email already exists"),
    ROL_NOT_FOUND_ENUM("Role '{}' does not match any value in the Role enum"),
    USER_NOT_FOUND("User not found with id: {}"),
    ROLE_NOT_FOUND("Role not found with id: ");

    private final String message;

    ErrorConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
