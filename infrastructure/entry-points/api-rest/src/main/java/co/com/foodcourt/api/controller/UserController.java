package co.com.foodcourt.api.controller;


import co.com.foodcourt.api.dto.CreateUserRequest;
import co.com.foodcourt.api.dto.CreateUserResponse;
import co.com.foodcourt.api.mapper.CreateUserMapper;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.usecase.createuser.CreateUserUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@RequiredArgsConstructor
public class UserController {

    private  final CreateUserUseCase createUserUseCase;

    @PostMapping(value = "/createUser")
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest user) {
        User createdUser = createUserUseCase.saveOwner(CreateUserMapper.INSTANCE.toDomain(user));
        return ResponseEntity.status(201).body(CreateUserMapper.INSTANCE.toDto(createdUser));

    }
}
