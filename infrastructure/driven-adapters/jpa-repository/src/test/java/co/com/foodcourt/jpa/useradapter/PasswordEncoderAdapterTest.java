package co.com.foodcourt.jpa.useradapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderAdapterTest {

    private PasswordEncoderAdapter passwordEncoderAdapter;

    @BeforeEach
    void setUp() {
        passwordEncoderAdapter = new PasswordEncoderAdapter();
    }

    @Test
    void encode_ShouldReturnEncodedPassword() {

        String rawPassword = "myPassword123";

        String encoded = passwordEncoderAdapter.encode(rawPassword);

        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(encoded.startsWith("$2a$"));
        assertTrue(encoded.length() >= 60);
    }

    @Test
    void encode_ShouldReturnDifferentHashForSamePassword() {

        String rawPassword = "myPassword123";

        String encoded1 = passwordEncoderAdapter.encode(rawPassword);
        String encoded2 = passwordEncoderAdapter.encode(rawPassword);

        assertNotEquals(encoded1, encoded2);
    }
}