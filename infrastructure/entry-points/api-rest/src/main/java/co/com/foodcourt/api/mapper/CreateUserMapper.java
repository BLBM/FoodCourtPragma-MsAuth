package co.com.foodcourt.api.mapper;

import co.com.foodcourt.api.dto.CreateUserRequest;
import co.com.foodcourt.api.dto.CreateUserResponse;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateUserMapper {

    CreateUserMapper INSTANCE = Mappers.getMapper(CreateUserMapper.class);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    @Mapping(target = "birthDate", source = "birthDate")
    User toDomain(CreateUserRequest createUserRequest);

    CreateUserResponse toDto(User user);

    @Named("stringToRole")
    default Rol stringToRole(String role) {
        return role == null ? null : Rol.valueOf(role.toUpperCase());
    }
}
