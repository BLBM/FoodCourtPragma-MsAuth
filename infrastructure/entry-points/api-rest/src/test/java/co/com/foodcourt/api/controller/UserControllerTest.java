package co.com.foodcourt.api.controller;


import co.com.foodcourt.api.dto.CreateUserRequest;
import co.com.foodcourt.api.global_exception_handler.GlobalExceptionHandler;
import co.com.foodcourt.model.rol.Rol;
import co.com.foodcourt.model.user.User;
import co.com.foodcourt.model.user.exception.DuplicateDocumentException;
import co.com.foodcourt.model.user.exception.DuplicateEmailException;
import co.com.foodcourt.usecase.createuser.CreateUserUseCase;
import co.com.foodcourt.usecase.exception.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class, GlobalExceptionHandler.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    private CreateUserRequest request;
    private User domainUser;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void init() {
        request = new CreateUserRequest(
                "John",
                "Smith",
                123456789L,
                "+573155544545",
                LocalDate.of(2000,5,1),
                "Smith@email.com",
                "1234");

        domainUser = User.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Smith")
                .documentId(123456789L)
                .phone("+573155544545")
                .email("Smith@email.com")
                .birthDate(LocalDate.of(2000,5,1))
                .password("1234")
                .role(Rol.ADMIN)
                .build();

    }


    @Test
    void shouldCreateUserAndReturn201() throws Exception {
        when(createUserUseCase.saveOwner(any(User.class))).thenReturn(domainUser);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("Smith@email.com"));
    }

    @Test
    void shouldHandleValidationExceptionFromHandler() throws Exception {
        when(createUserUseCase.saveOwner(any(User.class)))
                .thenThrow(new ValidationException("Custom business validation failed"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details:").value("Custom business validation failed"));
    }

    @Test
    void shouldHandleDuplicateEmailExceptionFromHandler() throws Exception {
        when(createUserUseCase.saveOwner(any(User.class)))
                .thenThrow(new DuplicateEmailException("Email already exists"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details: ").value("Email already exists"));
    }


    @Test
    void shouldHandleDuplicateDocumentExceptionFromHandler() throws Exception {
        when(createUserUseCase.saveOwner(any(User.class)))
                .thenThrow(new DuplicateDocumentException("Document already exists"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.details: ").value("Document already exists"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        when(createUserUseCase.saveOwner(any(User.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error: ").value("Unexpected error"));
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() throws Exception {
        String invalidRequest = """
        {
          "firstName": "",
          "lastName": "Smith",
          "documentId": 123456789,
          "phone": "+573155544545",
          "birthDate": "2000-05-01",
          "email": "bad-email",
          "password": "1234"
        }
        """;

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['error:'].firstName").value("First name is required"))
                .andExpect(jsonPath("$['timestamp:']").exists());
    }




}
