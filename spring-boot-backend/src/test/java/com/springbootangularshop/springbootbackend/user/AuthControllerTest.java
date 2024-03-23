package com.springbootangularshop.springbootbackend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootangularshop.springbootbackend.system.exception.CustomResponseEntityExceptionHandler;
import com.springbootangularshop.springbootbackend.system.exception.UsernameAlreadyExistsException;
import com.springbootangularshop.springbootbackend.user.converter.RegisterDTOToUserConverter;
import com.springbootangularshop.springbootbackend.user.converter.UserToReturnUserDTOConverter;
import com.springbootangularshop.springbootbackend.user.dto.LoginDTO;
import com.springbootangularshop.springbootbackend.user.dto.RegisterDTO;
import com.springbootangularshop.springbootbackend.user.dto.ReturnUserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = AuthController.class)
@Import(CustomResponseEntityExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    RegisterDTOToUserConverter registerDTOToUserConverter;

    @MockBean
    UserToReturnUserDTOConverter userToReturnUserDTOConverter;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "/api";

    User user;
    Role roleUser;
    ReturnUserDTO returnUserDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("joshhomme568");
        user.setEmail("joshhomme@gmail.com");
        user.setPassword("!Jjoshhomme568");

        roleUser = new Role();
        roleUser.setId(1L);
        roleUser.setName("USER");

        user.addRole(roleUser);

        returnUserDTO = new ReturnUserDTO();
        returnUserDTO.setUsername("joshhomme568");
        returnUserDTO.setEmail("joshhomme@gmail.com");
        returnUserDTO.setToken("Token");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAuthenticateUserSuccess() throws Exception {
        // Given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("joshhomme@gmail.com");
        loginDTO.setPassword("!Jjoshhomme568");

        String json = this.objectMapper.writeValueAsString(loginDTO);

        given(this.userService.authenticate(Mockito.any(LoginDTO.class))).willReturn(user);

        given(this.userToReturnUserDTOConverter.convert(user)).willReturn(returnUserDTO);

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Successful Jwt Login"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.email").value(loginDTO.getUsername()))
                .andExpect(jsonPath("$.data.username").value(user.getUsername()));
    }

    @Test
    void testAuthenticateUserNotFound() throws Exception {
        // Given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("joshhomme@gmail.com");
        loginDTO.setPassword("568");

        String json = this.objectMapper.writeValueAsString(loginDTO);

        given(this.userService.authenticate(Mockito.any(LoginDTO.class)))
                .willThrow(new UsernameNotFoundException("Username or password is incorrect."));

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.message").value("Username or password is incorrect."));
    }

    @Test
    void testAuthenticateUserInvalidInput() throws Exception {
        // Given
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("");
        loginDTO.setPassword("");

        String json = this.objectMapper.writeValueAsString(loginDTO);

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.password").value("Password is required"))
                .andExpect(jsonPath("$.data.username").value("Email is required"));
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("joetalbot123");
        registerDTO.setEmail("joe123@gmail.com");
        registerDTO.setPassword("!Jjoetalbot8");

        User regUser = new User();
        regUser.setUsername("joetalbot123");
        regUser.setEmail("joe123@gmail.com");
        regUser.setPassword("!Jjoetalbot8");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("joetalbot123");
        savedUser.setEmail("joe123@gmail.com");
        savedUser.setPassword("!Jjoetalbot8");

        ReturnUserDTO returnUser = new ReturnUserDTO();
        returnUser.setUsername("joetalbot123");
        returnUser.setEmail("joe123@gmail.com");
        returnUser.setToken("Token");

        String json = this.objectMapper.writeValueAsString(registerDTO);

        given(this.registerDTOToUserConverter.convert(Mockito.any(RegisterDTO.class))).willReturn(regUser);

        given(this.userService.saveUser(regUser)).willReturn(savedUser);

        given(this.userToReturnUserDTOConverter.convert(savedUser)).willReturn(returnUser);

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("New User Created"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.email").value(registerDTO.getEmail()))
                .andExpect(jsonPath("$.data.username").value(registerDTO.getUsername()));
    }

    @Test
    void testRegisterUserEmailAlreadyExists() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("joetalbot123");
        registerDTO.setEmail("joe123@gmail.com");
        registerDTO.setPassword("!Jjoetalbot8");

        User regUser = new User();
        regUser.setUsername("joetalbot123");
        regUser.setEmail("joe123@gmail.com");
        regUser.setPassword("!Jjoetalbot8");

        String json = this.objectMapper.writeValueAsString(registerDTO);

        given(this.registerDTOToUserConverter.convert(Mockito.any(RegisterDTO.class))).willReturn(regUser);

        given(this.userService.saveUser(regUser))
                .willThrow(new UsernameAlreadyExistsException("User with email 'joe123@gmail.com' already exists"));

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("User with email 'joe123@gmail.com' already exists"));
    }

    @Test
    void testRegisterUserInvalidInput() throws Exception {
        // Given
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("");
        registerDTO.setEmail("");
        registerDTO.setPassword("");

        String json = this.objectMapper.writeValueAsString(registerDTO);

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
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