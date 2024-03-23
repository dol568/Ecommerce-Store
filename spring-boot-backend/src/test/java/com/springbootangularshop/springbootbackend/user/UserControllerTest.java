package com.springbootangularshop.springbootbackend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootangularshop.springbootbackend.system.exception.CustomResponseEntityExceptionHandler;
import com.springbootangularshop.springbootbackend.user.converter.AddressToAddressDTOConverter;
import com.springbootangularshop.springbootbackend.user.converter.UserToReturnUserDTOConverter;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
import com.springbootangularshop.springbootbackend.user.dto.ReturnUserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = UserController.class)
@Import(CustomResponseEntityExceptionHandler.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    Principal mockPrincipal;

    @MockBean
    UserService userService;

    @MockBean
    AddressToAddressDTOConverter addressToAddressDTOConverter;

    @MockBean
    UserToReturnUserDTOConverter userToReturnUserDTOConverter;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "/api/users";

    User user1;
    User user2;
    Role roleUser;
    ReturnUserDTO returnUserDTO;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("joshhomme568");
        user1.setEmail("joshhomme@gmail.com");
        user1.setPassword("!Jjoshhomme568");

        user2 = new User();
        user2.setId(21L);
        user2.setUsername("thomyorke568");
        user2.setEmail("thomyorke@gmail.com");
        user2.setPassword("!Jthomyorke568");

        roleUser = new Role();
        roleUser.setId(1L);
        roleUser.setName("USER");

        user1.addRole(roleUser);
        user2.addRole(roleUser);

        returnUserDTO = new ReturnUserDTO();
        returnUserDTO.setUsername("joshhomme568");
        returnUserDTO.setEmail("joshhomme@gmail.com");
        returnUserDTO.setToken("Token");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetCurrentUserSuccess() throws Exception {
        // Given
        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");

        given(this.userService.findByEmail("joshhomme@gmail.com")).willReturn(user1);

        given(this.userToReturnUserDTOConverter.convert(user1)).willReturn(returnUserDTO);

        // When and then
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User retrieved"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.data.username").value(user1.getUsername()));
    }

    @Test
    void testGetAddressSuccess() throws Exception {
        // Given
        Address address = new Address();
        address.setId(1L);
        address.setName("Josh");
        address.setStreet("271 Street");
        address.setCity("Warsaw");
        address.setProvince("MZ");
        address.setPostalCode("10-150");
        address.setCountry("Poland");

        user1.addAddress(address);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("Josh");
        addressDTO.setStreet("271 Street");
        addressDTO.setCity("Warsaw");
        addressDTO.setProvince("MZ");
        addressDTO.setPostalCode("10-150");
        addressDTO.setCountry("Poland");

        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");

        given(this.userService.getAddress("joshhomme@gmail.com")).willReturn(user1.getAddress().get(0));

        given(this.addressToAddressDTOConverter.convert(address)).willReturn(addressDTO);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/address")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Address retrieved"))
                .andExpect(jsonPath("$.data.name").value(address.getName()))
                .andExpect(jsonPath("$.data.street").value(address.getStreet()))
                .andExpect(jsonPath("$.data.postalCode").value(address.getPostalCode()))
                .andExpect(jsonPath("$.data.city").value(address.getCity()))
                .andExpect(jsonPath("$.data.province").value(address.getProvince()))
                .andExpect(jsonPath("$.data.country").value(address.getCountry()));
    }

    @Test
    void testGetAddressNotFound() throws Exception {
        // Given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("");
        addressDTO.setStreet("");
        addressDTO.setCity("");
        addressDTO.setProvince("");
        addressDTO.setPostalCode("");
        addressDTO.setCountry("");

        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");

        given(this.userService.getAddress("joshhomme@gmail.com")).willReturn(new Address());

        given(this.addressToAddressDTOConverter.convert(new Address())).willReturn(addressDTO);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/address")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Address retrieved"))
                .andExpect(jsonPath("$.data.name").value(addressDTO.getName()))
                .andExpect(jsonPath("$.data.street").value(addressDTO.getStreet()))
                .andExpect(jsonPath("$.data.postalCode").value(addressDTO.getPostalCode()))
                .andExpect(jsonPath("$.data.city").value(addressDTO.getCity()))
                .andExpect(jsonPath("$.data.province").value(addressDTO.getProvince()))
                .andExpect(jsonPath("$.data.country").value(addressDTO.getCountry()));
    }
}