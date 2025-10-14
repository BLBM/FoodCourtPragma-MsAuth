package co.com.foodcourt.api.controller;


import co.com.foodcourt.api.common.ErrorConstants;
import co.com.foodcourt.api.common.LogConstants;
import co.com.foodcourt.api.common.SwaggerConstants;
import co.com.foodcourt.api.dto.CreateUserRequest;
import co.com.foodcourt.api.dto.CreateUserResponse;
import co.com.foodcourt.api.dto.ErrorResponse;
import co.com.foodcourt.api.dto.GetUserByIdResponse;
import co.com.foodcourt.api.exception.UnauthorizedException;
import co.com.foodcourt.api.mapper.CreateUserMapper;
import co.com.foodcourt.api.mapper.GetUserMapper;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.usecase.createuser.CreateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "Users", description = SwaggerConstants.TAG_USER_CONTROLLER)
public class UserController {

    private  final CreateUserUseCase createUserUseCase;

    @Operation(
            summary = SwaggerConstants.CREATE_OWNER_SUMMARY,
            description = SwaggerConstants.CREATE_OWNER_DESCRIPTION,
            responses = {
                    @ApiResponse(responseCode = "201", description = "Owner user successfully created",
                            content = @Content(schema = @Schema(implementation = CreateUserResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid role",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict - duplicate email or document",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping(path = "/owner")
    public ResponseEntity<CreateUserResponse> createUser(@RequestHeader("X-User-role") String role,
                                                             @Valid  @RequestBody CreateUserRequest user) {

            if (!Rol.ADMIN.name().equalsIgnoreCase(role)) {
                throw new UnauthorizedException(ErrorConstants.INVALID_ROL_CREATE_OWNER.getMessage());
            }

            log.info(LogConstants.CREATE_USER_REQUEST.getMessage(), user.email());
            User createdUser = createUserUseCase.saveOwner(CreateUserMapper.INSTANCE.toDomain(user));
            log.info(LogConstants.CREATE_USER_SUCCESS.getMessage(), createdUser.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(CreateUserMapper.INSTANCE.toDto(createdUser));
    }

    @Operation(
            summary = SwaggerConstants.GET_USER_BY_ID_SUMMARY,
            description = SwaggerConstants.GET_USER_BY_ID_DESCRIPTION,
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found successfully",
                            content = @Content(schema = @Schema(implementation = GetUserByIdResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected internal error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<GetUserByIdResponse> getUserById(@PathVariable("userId") Long userId) {
        log.info(LogConstants.GET_USER_BY_ID_REQUEST.getMessage(),userId);
        User getUser = createUserUseCase.getUser(userId);
        log.info(LogConstants.GET_USER_BY_ID_SUCCESS.getMessage(), getUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(GetUserMapper.INSTANCE.toResponse(getUser));
    }
}
