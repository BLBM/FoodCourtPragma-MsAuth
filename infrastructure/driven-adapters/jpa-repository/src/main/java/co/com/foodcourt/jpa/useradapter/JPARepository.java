package co.com.foodcourt.jpa.useradapter;

import co.com.foodcourt.jpa.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface JPARepository extends CrudRepository<UserEntity, String>, QueryByExampleExecutor<UserEntity> {
}
