package com.springbootangularshop.springbootbackend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootangularshop.springbootbackend.user.dto.LoginDTO;
import com.springbootangularshop.springbootbackend.user.dto.RegisterDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "/api";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Check authenticateUser (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAuthenticateUserSuccess() throws Exception {
        LoginDTO loginDTO = new LoginDTO("dar@gmail.com", "123");

        this.mockMvc.perform(post(this.baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(loginDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Successful Jwt Login"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.email").value("dar@gmail.com"))
                .andExpect(jsonPath("$.data.username").value("Darek"));
    }

    @Test
    @DisplayName("Check authenticateUser bad credentials (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAuthenticateUserErrorBadCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO("dar@gmail.com", "1");

        this.mockMvc.perform(post(this.baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(loginDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    @DisplayName("Check registerUser (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testRegisterUserSuccess() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("joetalbot123");
        registerDTO.setEmail("joe123@gmail.com");
        registerDTO.setPassword("!Jjoetalbot8");

        this.mockMvc.perform(post(this.baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(registerDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("New User Created"))
                .andExpect(jsonPath("$.data.email").value("joe123@gmail.com"))
                .andExpect(jsonPath("$.data.username").value("joetalbot123"))
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    @Test
    @DisplayName("Check registerUser duplicate email (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testRegisterUserErrorDuplicateEmail() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("joetalbot123");
        registerDTO.setEmail("dar@gmail.com");
        registerDTO.setPassword("!Jjoetalbot8");

        this.mockMvc.perform(post(this.baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(registerDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("User with email 'dar@gmail.com' already exists"));
    }

    @Test
    @DisplayName("Check authenticateUser with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAuthenticateUserErrorWithInvalidInput() throws Exception {
        LoginDTO loginDTO = new LoginDTO("", "");

        this.mockMvc.perform(post(this.baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(loginDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.username").value("Email is required"))
                .andExpect(jsonPath("$.data.password").value("Password is required"));
    }

    @Test
    @DisplayName("Check registerUser with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testRegisterUserErrorWithInvalidInput() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("");
        registerDTO.setEmail("");
        registerDTO.setPassword("");

        this.mockMvc.perform(post(this.baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(registerDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.username", Matchers.is(Matchers.in(Arrays.asList("Username must have between 8-45 characters", "Username is required")))))
                .andExpect(jsonPath("$.data.email", Matchers.is(Matchers.in(Arrays.asList("Email is required", "Email must have between 5-125 characters", "Email needs to be a valid email")))))
                .andExpect(jsonPath("$.data.password", Matchers.is(Matchers.in(Arrays.asList("Password must be at least 8 characters in length.," +
                        "Password must contain at least 1 uppercase characters.,Password must contain at least 1 lowercase characters.," +
                        "Password must contain at least 1 digit characters.,Password must contain at least 1 special characters.", "Password is required")))));
    }
}