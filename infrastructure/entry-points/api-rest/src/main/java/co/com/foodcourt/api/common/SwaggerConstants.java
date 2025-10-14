package co.com.foodcourt.api.common;

import lombok.Getter;

@Getter
public class SwaggerConstants {
    public static final String TAG_USER_CONTROLLER = "Endpoints related to user management";

    public static final String CREATE_OWNER_SUMMARY = "Create a new owner user";
    public static final String CREATE_OWNER_DESCRIPTION = "Creates a new user with the role OWNER. Only users with ADMIN role can perform this operation.";

    public static final String GET_USER_BY_ID_SUMMARY = "Get user by ID";
    public static final String GET_USER_BY_ID_DESCRIPTION = "Retrieves user details by user ID.";

    public static final String LOGIN_DESCRIPTION = """
                    If the credentials are valid, a JWT token is returned that can be used
                    to access protected endpoints.""";
    public static final String LOGIN_SUMMARY = "Authenticate user and generate JWT token";


    }
