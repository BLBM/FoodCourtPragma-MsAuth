package co.com.foodcourt.jpa.useradapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Test
    void shouldReturnTrueWhenPasswordsMatch() {
        String rawPassword = "mySecret123";
        String encoded = new BCryptPasswordEncoder().encode(rawPassword);

        boolean matches = passwordEncoderAdapter.matches(rawPassword, encoded);

        assertTrue(matches, "Expected matches() to return true for valid password comparison");
    }

    @Test
    void shouldReturnFalseWhenPasswordsDoNotMatch() {
        String rawPassword = "wrongPass";
        String encoded = new BCryptPasswordEncoder().encode("correctPass");

        boolean matches = passwordEncoderAdapter.matches(rawPassword, encoded);

        assertFalse(matches, "Expected matches() to return false when passwords differ");
    }

}