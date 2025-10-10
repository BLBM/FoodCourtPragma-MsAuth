package co.com.foodcourt.jpa.useradapter;

import co.com.foodcourt.jpa.common.ErrorConstants;
import co.com.foodcourt.jpa.entity.RolEntity;
import co.com.foodcourt.jpa.entity.UserEntity;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.exception.DuplicateDocumentException;
import co.com.foodcourt.model.user.exception.DuplicateEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JPARepositoryAdapterTest {

    @Mock
    private JPARepository repository;

    @Mock
    private RolJpaRepository rolJpaRepository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private JPARepositoryAdapter adapter;

    private User user;
    private UserEntity entity;
    private RolEntity rolEntity;

    @BeforeEach
    void init() {
        user = User.builder().userId(1L).firstName("Test User")
                .role(Rol.ADMIN).build();
        entity = UserEntity.builder().userId(1L).firstName("Test User").build();

        rolEntity = RolEntity.builder()
                .rolId(10L)
                .name("ADMIN")
                .build();
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
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.map(entity, User.class)).thenReturn(user);

        User result = adapter.saveUser(user);

        assertEquals(user, result);
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
        when(repository.save(entity)).thenThrow(
                new DataIntegrityViolationException("duplicate key value violates unique constraint " + ErrorConstants.USER_DOCUMENT_KEY.getMessage())
        );

        assertThrows(DuplicateDocumentException.class, () -> adapter.saveUser(user));
    }

    @Test
    void saveUser_duplicateEmail() {
        when(mapper.map(user, UserEntity.class)).thenReturn(entity);
        when(rolJpaRepository.findByName("ADMIN")).thenReturn(Optional.of(rolEntity));
        when(repository.save(entity)).thenThrow(
                new DataIntegrityViolationException("duplicate key value violates unique constraint " + ErrorConstants.USER_EMAIL_KEY.getMessage())
        );
        assertThrows(DuplicateEmailException.class, () -> adapter.saveUser(user));
    }
}
