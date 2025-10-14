package co.com.foodcourt.jpa.common;

import lombok.Getter;

@Getter
public enum ErrorConstants {

    USER_DOCUMENT_KEY("document_id"),
    USER_EMAIL_KEY("email"),
    DOCUMENT_ID_EXISTS("Document ID already exists"),
    EMAIL_EXISTS("Email already exists"),
    ROL_NOT_FOUND_ENUM("Role '{}' does not match any value in the Role enum"),
    USER_NOT_FOUND("User not found with id: {}"),
    ROLE_NOT_FOUND("Role not found with id: "),
    INVALID_AUTH("Email or password Invalid");

    private final String message;

    ErrorConstants(String message) {
        this.message = message;
    }
}
