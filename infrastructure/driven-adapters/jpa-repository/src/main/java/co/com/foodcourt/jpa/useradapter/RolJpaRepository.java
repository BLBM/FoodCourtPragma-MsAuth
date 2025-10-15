package co.com.foodcourt.jpa.useradapter;

import co.com.foodcourt.jpa.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolJpaRepository extends JpaRepository<RolEntity, Long> {

    Optional<RolEntity> findByName(String name);
}