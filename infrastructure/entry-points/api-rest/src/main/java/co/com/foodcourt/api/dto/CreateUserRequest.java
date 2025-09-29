package co.com.foodcourt.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;


public record CreateUserRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotNull(message = "Document ID is required")
        Long documentId,

        @NotBlank(message = "Phone is required")
        String phone,

        @NotNull(message = "Birth date is required")
        LocalDate birthDate,

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Role is required")
        String role
) { }
