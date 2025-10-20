package co.com.foodcourt.jpa.useradapter;

import co.com.foodcourt.jpa.common.ErrorConstants;
import co.com.foodcourt.jpa.common.LogConstants;
import co.com.foodcourt.jpa.entity.RolEntity;
import co.com.foodcourt.jpa.entity.UserEntity;
import co.com.foodcourt.jpa.helper.AdapterOperations;
import co.com.foodcourt.jpa.util.RolMapper;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.exception.AuthException;
import co.com.foodcourt.model.user.exception.DuplicateDocumentException;
import co.com.foodcourt.model.user.exception.DuplicateEmailException;
import co.com.foodcourt.model.user.exception.UserNotFoundException;
import co.com.foodcourt.model.user.gateways.PasswordEncoderRepository;
import co.com.foodcourt.model.user.gateways.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class JPARepositoryAdapter extends AdapterOperations<User, UserEntity, Long, JPARepository>
implements UserRepository
{
    private final RolJpaRepository rolJpaRepository;
    private final PasswordEncoderRepository passwordEncoderRepository;
    private final RolMapper rolMapper;

    public JPARepositoryAdapter(JPARepository repository,
                                ObjectMapper mapper,
                                RolJpaRepository rolJpaRepository,
                                PasswordEncoderRepository passwordEncoderRepository,
                                RolMapper rolMapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.rolJpaRepository = rolJpaRepository;
        this.passwordEncoderRepository = passwordEncoderRepository;
        this.rolMapper = rolMapper;
    }

    @Override
    public User saveUser(User user) {
        try {
            log.info(LogConstants.SAVE_USER.getMessage(), user.getEmail());
            UserEntity userEntity= super.mapper.map(user, UserEntity.class);

            RolEntity roleEntity = rolJpaRepository.findByName(user.getRole().name())
                    .orElseThrow(() -> new IllegalArgumentException(ErrorConstants.ROLE_NOT_FOUND.getMessage() + user.getRole()));

            userEntity.setRole(roleEntity);

            userEntity.setPassword(passwordEncoderRepository.encode(user.getPassword()));
            UserEntity saved = repository.save(userEntity);

            log.info(LogConstants.USER_SAVED.getMessage(), saved.getUserId());
            return toEntity(saved);

        } catch (DataIntegrityViolationException ex) {
            String message = ex.getMostSpecificCause().getMessage();
            if (message.contains(ErrorConstants.USER_DOCUMENT_KEY.getMessage())) {
                throw new DuplicateDocumentException(ErrorConstants.DOCUMENT_ID_EXISTS.getMessage());
            } else if (message.contains(ErrorConstants.USER_EMAIL_KEY.getMessage())) {
                throw new DuplicateEmailException(ErrorConstants.EMAIL_EXISTS.getMessage());
            }
            throw ex;
        }
    }

    @Override
    public User findByIdWithRole(Long userId) {
        log.info(LogConstants.GET_USER.getMessage(), userId);
        UserEntity entity = repository.findByIdWithRole(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_NOT_FOUND.getMessage() + userId));
        log.info(LogConstants.USER_FOUND.getMessage(), userId);
        User user = toEntity(entity);
        user.setRole(rolMapper.toEnum(entity.getRole()));
        return user;
    }

    @Override
    public User findByEmail(String email) {
        log.info(LogConstants.GET_USER_BY_EMAIL.getMessage(), email);
        UserEntity userEntity = repository.findByEmail(email).orElseThrow(()->new AuthException(ErrorConstants.INVALID_AUTH.getMessage()));
        log.info(LogConstants.USER_FOUND_BY_EMAIL.getMessage(), userEntity.getUserId());
        User user = toEntity(userEntity);
        user.setRole(rolMapper.toEnum(userEntity.getRole()));
        return user;
    }
}
