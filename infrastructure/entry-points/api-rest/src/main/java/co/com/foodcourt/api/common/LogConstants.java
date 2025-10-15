package co.com.foodcourt.api.common;

import lombok.Getter;

@Getter
public enum LogConstants {
    CREATE_OWNER_REQUEST("Request to create owner: {}"),
    CREATE_OWNER_SUCCESS("User owner created successfully with id: {}"),
    CREATE_EMPLOYEE_REQUEST("Request to create employee: {}"),
    CREATE_EMPLOYEE_SUCCESS("User employee created successfully with id: {}"),
    CREATE_CLIENT_REQUEST("Request to create client: {}"),
    CREATE_CLIENT_SUCCESS("User client created successfully with id: {}"),
    GET_USER_BY_ID_REQUEST("Request to get user by id: {}"),
    GET_USER_BY_ID_SUCCESS("Request to get user by id successfully: {}"),
    LOGIN_REQUEST("login request by email:  {}"),
    LOGIN_SUCCESS("login request successfully by email: {}"),
    TIMESTAMP("timestamp:"),
    ERROR("error:"),
    DETAILS("details:");


    private final String message;

    LogConstants(String message) {
        this.message = message;
    }

}
