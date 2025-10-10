package co.com.foodcourt.jpa.useradapter;

import co.com.foodcourt.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Optional;

public interface JPARepository extends CrudRepository<UserEntity, Long>, QueryByExampleExecutor<UserEntity> {

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.role WHERE u.userId = :userId")
    Optional<UserEntity> findByIdWithRole(@Param("userId") Long userId);
}
