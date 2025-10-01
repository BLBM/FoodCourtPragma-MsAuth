package co.com.foodcourt.jpa.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorConstantsTest {

    @Test
    void shouldReturnCorrectMessageForInvalidEmail() {
        assertEquals("users_email_key", ErrorConstants.USER_EMAIL_KEY.getMessage());
    }
}
