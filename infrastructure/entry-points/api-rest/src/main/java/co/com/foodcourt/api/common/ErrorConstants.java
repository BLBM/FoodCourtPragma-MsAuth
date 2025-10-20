package co.com.foodcourt.api.common;

import lombok.Getter;

@Getter
public enum ErrorConstants {
    INVALID_ROL_CREATE_OWNER("Only admin can create owners"),
    INVALID_ROL_CREATE_EMPLOYEE("Only Owners can create employee");

    private final String message;

    ErrorConstants(String message) {
        this.message = message;
    }

}
