package co.com.foodcourt.usecase.util;

import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.usecase.common.ValidationMessages;
import co.com.foodcourt.usecase.exception.ValidationException;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ValidateUser {


    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{1,13}$");
    private static final Pattern DOCUMENT_PATTERN = Pattern.compile("^\\d+$");
    private static final Integer ADULT_AGE = 18;


    public static void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException(ValidationMessages.INVALID_EMAIL.getMessage());
        }
    }

    public static void validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException(ValidationMessages.INVALID_PHONE.getMessage());
        }
    }

    public static void validateDocumentId(Long documentId) {
        if (documentId == null || !DOCUMENT_PATTERN.matcher(documentId.toString()).matches()) {
            throw new ValidationException(ValidationMessages.INVALID_DOCUMENT.getMessage());
        }
    }

    public static void validateRole(Rol role) {
        if (role == null) {
            throw new ValidationException(ValidationMessages.INVALID_ROLE.getMessage());
        }
    }

    public static void validateBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new ValidationException(ValidationMessages.INVALID_BIRTHDATE.getMessage());
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < ADULT_AGE ) {
            throw new ValidationException(ValidationMessages.INVALID_BIRTHDATE.getMessage());
        }
    }


    public static void validateUser(User user) {
        if (user == null) {
            throw new ValidationException(ValidationMessages.INVALID_USER.getMessage());
        }
        validateEmail(user.getEmail());
        validatePhone(user.getPhone());
        validateDocumentId(user.getDocumentId());
        validateRole(user.getRole());
        validateBirthDate(user.getBirthDate());
    }
}
