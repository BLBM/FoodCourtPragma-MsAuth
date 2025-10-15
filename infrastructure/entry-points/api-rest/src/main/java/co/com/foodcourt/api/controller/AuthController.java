package co.com.foodcourt.api.controller;


import co.com.foodcourt.api.common.LogConstants;
import co.com.foodcourt.api.common.SwaggerConstants;
import co.com.foodcourt.api.dto.ErrorResponse;
import co.com.foodcourt.api.dto.LoginRequest;
import co.com.foodcourt.usecase.login.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;


    @Operation(
            summary = SwaggerConstants.LOGIN_SUMMARY,
            description = SwaggerConstants.LOGIN_DESCRIPTION,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "User credentials for authentication",
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful authentication - JWT token returned",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request or missing credentials",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid email or password",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unexpected internal error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping
    public String login(@RequestBody LoginRequest request){
        log.info(LogConstants.LOGIN_REQUEST.getMessage(), request.email());
        String token = loginUseCase.login(request.email(),request.password());
        log.info(LogConstants.LOGIN_SUCCESS.getMessage(), request.email());
        return token;
    }

}
