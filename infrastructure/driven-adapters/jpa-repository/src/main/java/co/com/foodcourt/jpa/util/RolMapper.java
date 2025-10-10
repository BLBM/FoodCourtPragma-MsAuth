package co.com.foodcourt.jpa.util;

import co.com.foodcourt.jpa.common.ErrorConstants;
import co.com.foodcourt.jpa.entity.RolEntity;
import co.com.foodcourt.model.rol.Rol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RolMapper {

    public Rol toEnum(RolEntity entity) {
        if (entity == null || entity.getName() == null) {
            return null;
        }
        try {
            return Rol.valueOf(entity.getName().toUpperCase());
        } catch (IllegalArgumentException ex) {
            log.warn(ErrorConstants.ROL_NOT_FOUND_ENUM.getMessage(), entity.getName());
            return null;
        }
    }
}
