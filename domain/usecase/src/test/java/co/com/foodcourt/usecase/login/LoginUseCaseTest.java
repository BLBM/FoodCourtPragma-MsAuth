package co.com.foodcourt.usecase.login;


import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.exception.AuthException;
import co.com.foodcourt.model.user.gateways.PasswordEncoderRepository;
import co.com.foodcourt.model.user.gateways.TokenProvider;
import co.com.foodcourt.model.user.gateways.UserRepository;
import co.com.foodcourt.usecase.common.ValidationMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private PasswordEncoderRepository passwordEncoderRepository;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .userId(1L)
                .email("user@test.com")
                .password("encodedPassword")
                .role(Rol.ADMIN)
                .build();
    }

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        // Arrange
        String email = "user@test.com";
        String password = "plainPassword";
        String fakeToken = "jwt-token-12345";

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoderRepository.matches(password, user.getPassword())).thenReturn(true);
        when(tokenProvider.generateToken(user.getUserId(), user.getRole().name(), user.getEmail()))
                .thenReturn(fakeToken);


        String token = loginUseCase.login(email, password);


        assertNotNull(token);
        assertEquals(fakeToken, token);

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoderRepository, times(1)).matches(password, user.getPassword());
        verify(tokenProvider, times(1))
                .generateToken(user.getUserId(), user.getRole().name(), user.getEmail());
    }


    @Test
    void shouldThrowAuthExceptionWhenPasswordDoesNotMatch() {
        String email = "user@test.com";
        String password = "wrongPassword";

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoderRepository.matches(password, user.getPassword())).thenReturn(false);

        AuthException exception = assertThrows(
                AuthException.class,
                () -> loginUseCase.login(email, password)
        );

        assertEquals(ValidationMessages.INVALID_AUTH.getMessage(), exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoderRepository, times(1)).matches(password, user.getPassword());
        verify(tokenProvider, never()).generateToken(anyLong(), anyString(), anyString());
    }


    @Test
    void shouldThrowAuthExceptionWhenUserNotFound() {
        String email = "unknown@test.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenThrow(new AuthException(ValidationMessages.INVALID_AUTH.getMessage()));

        AuthException exception = assertThrows(
                AuthException.class,
                () -> loginUseCase.login(email, password)
        );

        assertEquals(ValidationMessages.INVALID_AUTH.getMessage(), exception.getMessage());

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoderRepository, never()).matches(anyString(), anyString());
        verify(tokenProvider, never()).generateToken(anyLong(), anyString(), anyString());
    }
}
