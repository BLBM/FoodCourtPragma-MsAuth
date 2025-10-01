package co.com.foodcourt.jpa.useradapter;

import co.com.foodcourt.jpa.common.ErrorConstants;
import co.com.foodcourt.jpa.common.LogConstants;
import co.com.foodcourt.jpa.entity.RolEntity;
import co.com.foodcourt.jpa.entity.UserEntity;
import co.com.foodcourt.jpa.helper.AdapterOperations;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.exception.DuplicateDocumentException;
import co.com.foodcourt.model.user.exception.DuplicateEmailException;
import co.com.foodcourt.model.user.gateways.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class JPARepositoryAdapter extends AdapterOperations<User, UserEntity, String, JPARepository>
implements UserRepository
{
    private final RolJpaRepository rolJpaRepository;

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper, RolJpaRepository rolJpaRepository) {
        super(repository, mapper, d -> mapper.map(d, User.class));
        this.rolJpaRepository = rolJpaRepository;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        try {
            log.info(LogConstants.SAVE_USER.getMessage(), user.getRole());
            UserEntity entity = super.mapper.map(user, UserEntity.class);

            RolEntity roleEntity = rolJpaRepository.findByName(user.getRole().name())
                    .orElseThrow(() -> new IllegalArgumentException(LogConstants.ROLE_NOT_FOUND.getMessage() + user.getRole()));

            entity.setRole(roleEntity);

            UserEntity saved = repository.save(entity);
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
}
