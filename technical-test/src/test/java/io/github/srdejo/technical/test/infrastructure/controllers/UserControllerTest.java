package io.github.srdejo.technical.test.infrastructure.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.srdejo.technical.test.application.dtos.request.LoginRequest;
import io.github.srdejo.technical.test.application.dtos.request.PhoneRequest;
import io.github.srdejo.technical.test.application.dtos.request.SignUpRequest;
import io.github.srdejo.technical.test.domain.entities.User;
import io.github.srdejo.technical.test.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("testuser@example.com");
        request.setPassword("abcDef12"); // cumple reglas: 1 mayúscula, 2 números, 8-12 chars
        request.setName("Test User");
        request.setPhones(List.of(new PhoneRequest(12345678L, 1, "+57")));

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.active").value(true));

        // verificamos en BD
        assertTrue(userRepository.findByEmail("testuser@example.com").isPresent());
    }

    @Test
    void shouldFailIfEmailInvalid() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("bademail@domain");
        request.setPassword("abcDef12");

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].detail").value("El correo debe tener formato válido"));
    }

    @Test
    void shouldFailIfPasswordInvalid() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("valid@email.com");
        request.setPassword("password");

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].detail").value("La contraseña no cumple con el formato requerido"));
    }

    @Test
    void shouldFailIfUserAlreadyExists() throws Exception {
        // persistimos usuario inicial
        User existing = new User();
        existing.setId(UUID.randomUUID());
        existing.setEmail("dupe@example.com");
        existing.setPassword("abcDef12");
        userRepository.save(existing);

        // intentamos registrar otro con mismo correo
        SignUpRequest request = new SignUpRequest();
        request.setEmail("dupe@example.com");
        request.setPassword("abcDef12");

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error[0].detail").value("Ya existe un usuario con este email"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        // primero creamos usuario
        SignUpRequest signUp = new SignUpRequest();
        signUp.setEmail("login@example.com");
        signUp.setPassword("abcDef12");
        signUp.setName("Test User");

        mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUp)))
                .andExpect(status().isCreated());

        LoginRequest login = new LoginRequest();
        login.setEmail("login@example.com");
        login.setPassword("abcDef12");

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.lastLogin").exists());
    }


    @Test
    void shouldLoginWithTokenSuccessfully() throws Exception {
        // primero creamos usuario
        SignUpRequest signUp = new SignUpRequest();
        signUp.setEmail("login2@example.com");
        signUp.setPassword("abcDef12");
        signUp.setName("Test User");

        String loginResponse = mockMvc.perform(post("/user/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUp)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.lastLogin").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(loginResponse);
        String token = jsonNode.get("token").asText();

        mockMvc.perform(get("/user/login")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.lastLogin").exists())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldFailLoginWithoutToken() throws Exception {
        mockMvc.perform(get("/user/login")
                .header("Authorization", "Jwt "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].detail").value("Token no proporcionado o inválido"));
    }
}