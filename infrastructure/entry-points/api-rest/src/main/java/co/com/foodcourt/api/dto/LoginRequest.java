package co.com.foodcourt.api.dto;

public record LoginRequest(
        String email,
        String password
) {
}
