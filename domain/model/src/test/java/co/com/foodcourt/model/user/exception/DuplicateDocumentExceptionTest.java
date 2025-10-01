package co.com.foodcourt.model.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DuplicateDocumentExceptionTest {


    @Test
    void shouldStoreMessageCorrectly() {
    String expectedMessage = "Document already exists";

    DuplicateDocumentException ex = assertThrows(
            DuplicateDocumentException.class,
            () -> { throw new DuplicateDocumentException(expectedMessage); }
    );

    assertEquals(expectedMessage, ex.getMessage());
    }
}
