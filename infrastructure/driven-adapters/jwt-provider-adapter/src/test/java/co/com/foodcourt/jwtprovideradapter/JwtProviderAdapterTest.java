package co.com.foodcourt.jwtprovideradapter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderAdapterTest {

    private JwtProviderAdapter jwtProviderAdapter;
    private String secret;
    private long expirationMillis;

    @BeforeEach
    void setUp() {
        secret = "my-super-secure-jwt-secret-key-1234567890";
        expirationMillis = 3600000; // 1 hora
        jwtProviderAdapter = new JwtProviderAdapter(secret, expirationMillis);
    }


    @Test
    void shouldGenerateTokenWithCorrectClaims() {
        // Arrange
        Long userId = 1L;
        String role = "ADMIN";
        String email = "user@test.com";

        String token = jwtProviderAdapter.generateToken(userId, role, email);

        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3, "Token JWT debe tener 3 partes");

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(String.valueOf(userId), claims.getSubject());
        assertEquals(role, claims.get("role"));
        assertEquals(email, claims.get("email"));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(new Date()), "La fecha de expiración debe ser futura");
    }

    @Test
    void shouldSetExpirationCorrectly() {
        Long userId = 5L;
        String role = "USER";
        String email = "test@correo.com";

        long start = System.currentTimeMillis();
        String token = jwtProviderAdapter.generateToken(userId, role, email);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        long issuedAt = claims.getIssuedAt().getTime();
        long expiration = claims.getExpiration().getTime();

         long delta = Math.abs((issuedAt + expirationMillis) - expiration);
        assertTrue(delta < 2000, "La expiración debe ser cercana a issuedAt + expirationMillis");
    }

}
