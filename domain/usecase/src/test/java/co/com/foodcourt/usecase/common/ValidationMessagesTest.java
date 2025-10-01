package co.com.foodcourt.usecase.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationMessagesTest {
    @Test
    void shouldReturnCorrectMessageForInvalidEmail() {
        assertEquals("Invalid email format", ValidationMessages.INVALID_EMAIL.getMessage());
    }
}
