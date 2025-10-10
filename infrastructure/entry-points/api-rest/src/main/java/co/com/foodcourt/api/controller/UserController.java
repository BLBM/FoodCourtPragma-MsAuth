package co.com.foodcourt.api.controller;


import co.com.foodcourt.api.common.LogConstants;
import co.com.foodcourt.api.dto.CreateUserRequest;
import co.com.foodcourt.api.dto.CreateUserResponse;
import co.com.foodcourt.api.dto.GetUserByIdResponse;
import co.com.foodcourt.api.mapper.CreateUserMapper;
import co.com.foodcourt.api.mapper.GetUserMapper;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.usecase.createuser.CreateUserUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    private  final CreateUserUseCase createUserUseCase;

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest user) {
            log.info(LogConstants.CREATE_USER_REQUEST.getMessage(), user.email());
            User createdUser = createUserUseCase.saveOwner(CreateUserMapper.INSTANCE.toDomain(user));
            log.info(LogConstants.CREATE_USER_SUCCESS.getMessage(), createdUser.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(CreateUserMapper.INSTANCE.toDto(createdUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetUserByIdResponse> getUserById(@PathVariable("userId") Long userId) {
        log.info(LogConstants.GET_USER_BY_ID_REQUEST.getMessage(),userId);
        User getUser = createUserUseCase.getUser(userId);
        log.info(LogConstants.GET_USER_BY_ID_SUCCESS.getMessage(), getUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(GetUserMapper.INSTANCE.toResponse(getUser));
    }
}
