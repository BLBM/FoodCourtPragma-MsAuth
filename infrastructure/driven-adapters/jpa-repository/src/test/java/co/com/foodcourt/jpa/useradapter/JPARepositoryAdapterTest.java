package co.com.foodcourt.jpa.useradapter;

import co.com.foodcourt.jpa.common.ErrorConstants;
import co.com.foodcourt.jpa.entity.RolEntity;
import co.com.foodcourt.jpa.entity.UserEntity;
import co.com.foodcourt.jpa.util.RolMapper;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.exception.AuthException;
import co.com.foodcourt.model.user.exception.DuplicateDocumentException;
import co.com.foodcourt.model.user.exception.DuplicateEmailException;
import co.com.foodcourt.model.user.exception.UserNotFoundException;
import co.com.foodcourt.model.user.gateways.PasswordEncoderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JPARepositoryAdapterTest {

    @Mock
    private JPARepository repository;

    @Mock
    private RolJpaRepository rolJpaRepository;

    @Mock
    private PasswordEncoderRepository passwordEncoderRepository;

    @Mock
    private ObjectMapper mapper;

    @Spy
    private RolMapper rolMapper;

    @InjectMocks
    private JPARepositoryAdapter adapter;

    private User user;
    private UserEntity entity;
    private RolEntity rolEntity;
    private String encodedPassword;

    @BeforeEach
    void init() {
        user = User.builder()
                .userId(1L)
                .firstName("Test User")
                .password("plainPassword")
                .role(Rol.ADMIN).build();

        entity = UserEntity.builder().userId(1L).firstName("Test User").build();

        rolEntity = RolEntity.builder()
                .rolId(10L)
                .name("ADMIN")
                .build();

        encodedPassword = "$2a$10$encodedPassword";

    }

    @Test
    void testSave() {
        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(entity, User.class)).thenReturn(user);

        User result = adapter.save(user);

        assertEquals(user, result);
    }


    @Test
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        User result = adapter.findById(1L);

        assertEquals(user, result);
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        List<User> result = adapter.findAll();

        assertEquals(List.of(user), result);
    }

    @Test
    void testFindByExample() {
        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(repository.findAll(org.mockito.ArgumentMatchers.any())).thenReturn(List.of(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        List<User> result = adapter.findByExample(user);

        assertEquals(List.of(user), result);
    }


    @Test
    void saveUser_success() {
        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(rolJpaRepository.findByName("ADMIN")).thenReturn(Optional.of(rolEntity));
        when(passwordEncoderRepository.encode("plainPassword")).thenReturn(encodedPassword);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(entity, User.class)).thenReturn(user);

        User result = adapter.saveUser(user);

        assertEquals(user, result);

        verify(passwordEncoderRepository, times(1)).encode("plainPassword");
    }

    @Test
    void saveUser_roleNotFound() {
        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(rolJpaRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> adapter.saveUser(user));
    }

    @Test
    void saveUser_duplicateDocument() {
        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(rolJpaRepository.findByName("ADMIN")).thenReturn(Optional.of(rolEntity));
        when(passwordEncoderRepository.encode("plainPassword")).thenReturn(encodedPassword);
        when(repository.save(entity)).thenThrow(
                new DataIntegrityViolationException("duplicate key value violates unique constraint " + ErrorConstants.USER_DOCUMENT_KEY.getMessage())
        );

        assertThrows(DuplicateDocumentException.class, () -> adapter.saveUser(user));
        verify(passwordEncoderRepository, times(1)).encode("plainPassword");
    }

    @Test
    void saveUser_duplicateEmail() {
        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(rolJpaRepository.findByName("ADMIN")).thenReturn(Optional.of(rolEntity));
        when(passwordEncoderRepository.encode("plainPassword")).thenReturn(encodedPassword);
        when(repository.save(entity)).thenThrow(
                new DataIntegrityViolationException("duplicate key value violates unique constraint " + ErrorConstants.USER_EMAIL_KEY.getMessage())
        );
        assertThrows(DuplicateEmailException.class, () -> adapter.saveUser(user));
        verify(passwordEncoderRepository, times(1)).encode("plainPassword");
    }

    // TEST FOR HU2 GET USER BY ID

    @Test
    void shouldPassWhenUserFound(){
        rolEntity.setName("OWNER");
        entity.setRole(rolEntity);
        user.setRole(Rol.OWNER);
        long userId= 1L;

        when(repository.findByIdWithRole(userId)).thenReturn(Optional.of(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        User result = adapter.findByIdWithRole(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(Rol.OWNER, result.getRole());

        verify(repository, times(1)).findByIdWithRole(userId);
        verify(mapper, times(1)).map(entity, User.class);
        verify(rolMapper, times(1)).toEnum(rolEntity);

    }


    @Test
    void  shouldShowErrorWhenNotFound(){
        long userId= 99L;
        when(repository.findByIdWithRole(userId)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                ()->adapter.findByIdWithRole(userId)
        );

        assertTrue(ex.getMessage().contains(String.valueOf(userId)));

        verify(repository, times(1)).findByIdWithRole(userId);
        verify(rolMapper, never()).toEnum(any());
    }


    @Test
    void findByIdWithRole_WhenRoleNameIsInvalid_ShouldReturnUserWithNullRole() {
        Long userId = 1L;
        rolEntity.setName("INVALID_ROLE");
        entity.setRole(rolEntity);

        when(repository.findByIdWithRole(userId)).thenReturn(Optional.of(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        User result = adapter.findByIdWithRole(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertNull(result.getRole());

        verify(repository, times(1)).findByIdWithRole(userId);
        verify(mapper, times(1)).map(entity, User.class);
        verify(rolMapper, times(1)).toEnum(rolEntity);
    }

    /// ///////////////////////// TEST HU5 LOGIN ///////////////////////

    @Test
    void shouldFindUserByEmailSuccessfully() {
        String email = "owner@test.com";
        long userId = 1L;
        rolEntity.setName("OWNER");
        entity.setRole(rolEntity);
        user.setRole(Rol.OWNER);
        user.setEmail(email);
        entity.setUserId(userId);
        entity.setEmail(email);

        when(repository.findByEmail(email)).thenReturn(Optional.of(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);
        when(rolMapper.toEnum(rolEntity)).thenReturn(Rol.OWNER);

        User result = adapter.findByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(Rol.OWNER, result.getRole());
        assertEquals(userId, result.getUserId());

        verify(repository, times(1)).findByEmail(email);
        verify(mapper, times(1)).map(entity, User.class);
        verify(rolMapper, times(1)).toEnum(rolEntity);
    }



    @Test
    void shouldThrowAuthExceptionWhenUserNotFound() {
        String email = "unknown@test.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        AuthException exception = assertThrows(
                AuthException.class,
                () -> adapter.findByEmail(email)
        );

        assertEquals("Email or password Invalid", exception.getMessage());
        verify(repository, times(1)).findByEmail(email);
        verify(rolMapper, never()).toEnum(any());
    }

}
