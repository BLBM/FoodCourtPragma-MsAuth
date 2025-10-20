package co.com.foodcourt.usecase.createuser;

import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.exception.UserNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
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
                .role(Rol.ADMIN)
                .build();
    }

    @Test
    void shouldAssignOwnerRoleAndSaveUser() {
        when(userRepository.saveUser(any(User.class)))
                .thenReturn(user.toBuilder().userId(1L).role(Rol.OWNER).build());

        User result = createUserUseCase.saveOwner(user);

        assertEquals(Rol.OWNER, result.getRole());
        assertEquals(1L, result.getUserId());
        verify(userRepository).saveUser(argThat(u -> u.getRole() == Rol.OWNER));
    }


    @Test
    void shouldThrowValidationExceptionForInvalidEmail() {
        user.setEmail("bad-email");
        assertValidationFails(() -> createUserUseCase.saveOwner(user));
    }

    @Test
    void shouldThrowValidationExceptionForInvalidAge() {
        user.setBirthDate(LocalDate.of(2025, 1, 1));
        assertValidationFails(() -> createUserUseCase.saveOwner(user));
    }

    @Test
    void shouldThrowValidationExceptionForNullDocumentId() {
        user.setDocumentId(null);
        assertValidationFails(() -> createUserUseCase.saveOwner(user));
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

    @Test
    void shouldThrowValidationExceptionWhenUserIsNull() {
        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> createUserUseCase.saveOwner(null)
        );
        assertEquals(ValidationMessages.INVALID_USER.getMessage(), ex.getMessage());
        verify(userRepository, never()).saveUser(any());
    }

    private void assertValidationFails(Runnable action) {
        assertThrows(ValidationException.class, action::run);
        verify(userRepository, never()).saveUser(any());
    }


    /// //////////// TEST FEATURE HU2 GET USER BY ID //////////////


    @Test
    void getUser_WhenUserNotFound_ShouldThrowException() {
        Long userId = 999L;
        when(userRepository.findByIdWithRole(userId))
                .thenThrow(new UserNotFoundException("User not found with id: " + userId));

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> createUserUseCase.getUser(userId)
        );

        assertTrue(exception.getMessage().contains(String.valueOf(userId)));
        verify(userRepository, times(1)).findByIdWithRole(userId);
    }

    @Test
    void getUser_ShouldDelegateToRepository() {
        Long userId = 1L;
        when(userRepository.findByIdWithRole(userId)).thenReturn(user);

        User result = createUserUseCase.getUser(userId);

        assertSame(user, result);
        verify(userRepository, times(1)).findByIdWithRole(userId);
        verifyNoMoreInteractions(userRepository);
    }

    /// ///////////////////// FEATURE HU 6//////////////

    @Test
    void shouldAssignEmployeeRoleAndSaveUser() {
        when(userRepository.saveUser(any(User.class)))
                .thenReturn(user.toBuilder().userId(2L).role(Rol.EMPLOYEE).build());

        User result = createUserUseCase.saveEmployee(user);

        assertEquals(Rol.EMPLOYEE, result.getRole());
        verify(userRepository).saveUser(argThat(u -> u.getRole() == Rol.EMPLOYEE));
    }

    /// /////////////// FEATURE HU 7/////////////////////

    @Test
    void shouldAssignClientRoleAndSaveUser() {
        when(userRepository.saveUser(any(User.class)))
                .thenReturn(user.toBuilder().userId(2L).role(Rol.CLIENT).build());

        User result = createUserUseCase.saveClient(user);

        assertEquals(Rol.CLIENT, result.getRole());
        verify(userRepository).saveUser(argThat(u -> u.getRole() == Rol.CLIENT));
    }

}
