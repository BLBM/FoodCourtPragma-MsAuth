package co.com.foodcourt.api.controller;

import co.com.foodcourt.api.dto.LoginRequest;
import co.com.foodcourt.api.global_exception_handler.GlobalExceptionHandler;
import co.com.foodcourt.model.user.exception.AuthException;
import co.com.foodcourt.usecase.login.LoginUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {AuthController.class, GlobalExceptionHandler.class})
public class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginUseCase loginUseCase;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("user@test.com", "password123");
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        String fakeToken = "jwt-token-12345";

        when(loginUseCase.login(anyString(), anyString())).thenReturn(fakeToken);

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(fakeToken));
    }


    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedErrorOccurs() throws Exception {
        when(loginUseCase.login(anyString(), anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error:").value("Unexpected error"));
    }
}
