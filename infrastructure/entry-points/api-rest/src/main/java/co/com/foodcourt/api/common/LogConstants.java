package co.com.foodcourt.api.common;

import lombok.Getter;

@Getter
public enum LogConstants {
    CREATE_USER_REQUEST("Request to create user: {}"),
    CREATE_USER_SUCCESS("User created successfully with id: {}"),
    GET_USER_BY_ID_REQUEST("Request to get user by id: {}"),
    GET_USER_BY_ID_SUCCESS("Request to get user by id: {}"),
    TIMESTAMP("timestamp:"),
    ERROR("error:"),
    DETAILS("details:");


    private final String message;

    LogConstants(String message) {
        this.message = message;
    }

}
