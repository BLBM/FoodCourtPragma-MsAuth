package co.com.foodcourt.jpa.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogConstantsTest {

    @Test
    void shouldReturnCorrectMessageForInvalidEmail() {
        assertEquals("Saving user: {}", LogConstants.SAVE_USER.getMessage());
    }
}
