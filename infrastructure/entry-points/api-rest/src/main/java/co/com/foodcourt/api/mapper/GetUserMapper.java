package co.com.foodcourt.api.mapper;

import co.com.foodcourt.api.dto.GetUserByIdResponse;
import co.com.foodcourt.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface GetUserMapper {

    GetUserMapper INSTANCE = Mappers.getMapper(GetUserMapper.class);

    GetUserByIdResponse toResponse(User user);
}
