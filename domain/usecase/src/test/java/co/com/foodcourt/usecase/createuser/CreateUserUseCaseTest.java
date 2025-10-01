package co.com.foodcourt.usecase.createuser;

import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.gateways.UserRepository;
import co.com.foodcourt.usecase.common.ValidationMessages;
import co.com.foodcourt.usecase.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("Laura")
                .lastName("Martínez")
                .documentId(123456789L)
                .phone("+573001112233")
                .email("laura@example.com")
                .birthDate(LocalDate.of(2000, 1, 1))
                .password("secret123")
                .build();
    }

    @Test
    void shouldAssignOwnerRoleAndSaveUser() {
        User savedUser = user.toBuilder().userId("1").role(Rol.OWNER).build();
        when(userRepository.saveUser(any(User.class))).thenReturn(savedUser);
        User result = createUserUseCase.saveOwner(user);
        assertEquals(Rol.OWNER, result.getRole());
        assertEquals(1L, result.getUserId());
        verify(userRepository, times(1)).saveUser(user);
    }

    @Test
    void shouldThrowValidationExceptionForInvalidEmail() {
        user.setEmail("bad-email");
        assertThrows(ValidationException.class, () -> createUserUseCase.saveOwner(user));
        verify(userRepository, never()).saveUser(any());
    }


    @Test
    void shouldThrowValidationExceptionForInvalidAge() {
        user.setBirthDate(LocalDate.of(2025, 1, 1));
        assertThrows(ValidationException.class, () -> createUserUseCase.saveOwner(user));
        verify(userRepository, never()).saveUser(any());
    }

    @Test
    void shouldThrowValidationExceptionForNullDocumentId() {
        user.setDocumentId(null);
        assertThrows(ValidationException.class, () -> createUserUseCase.saveOwner(user));
        verify(userRepository, never()).saveUser(any());
    }


    @Test
    void shouldThrowValidationExceptionWithCorrectMessageForPhone() {
        user.setPhone("abc123");
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> createUserUseCase.saveOwner(user)
        );
        assertEquals(ValidationMessages.INVALID_PHONE.getMessage(), ex.getMessage());
    }

}
