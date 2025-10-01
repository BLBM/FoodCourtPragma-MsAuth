package co.com.foodcourt.model.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DuplicateEmailExceptionTest {

    @Test
    void shouldStoreMessageCorrectly() {

        String expectedMessage = "Email already exists";

        DuplicateEmailException ex = assertThrows(
                DuplicateEmailException.class,
                () -> { throw new DuplicateEmailException(expectedMessage); }
        );

        assertEquals(expectedMessage, ex.getMessage());
    }
}
