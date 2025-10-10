package co.com.foodcourt.api.dto;

public record GetUserByIdResponse (
        String firstName,
        String lastName,
        String email,
        String phone,
        String role
){


}
