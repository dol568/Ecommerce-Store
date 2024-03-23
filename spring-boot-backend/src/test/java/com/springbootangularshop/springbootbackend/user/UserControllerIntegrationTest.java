package com.springbootangularshop.springbootbackend.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootangularshop.springbootbackend.user.dto.LoginDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    String baseUrl = "/api/users";

    @BeforeEach
    void setUp() throws Exception {
        LoginDTO loginDTO = new LoginDTO("dar@gmail.com", "123");

        ResultActions resultActions = this.mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginDTO))
                .accept(MediaType.APPLICATION_JSON));
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Check getCurrentUser (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetCurrentUserSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("User retrieved"))
                .andExpect(jsonPath("$.data.email").value("dar@gmail.com"))
                .andExpect(jsonPath("$.data.username").value("Darek"))
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    @Test
    @DisplayName("Check getAddress (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetAddressSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/address")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Address retrieved"))
                .andExpect(jsonPath("$.data.name").value("Darek"))
                .andExpect(jsonPath("$.data.street").value("Kowalskiego 12A"))
                .andExpect(jsonPath("$.data.postalCode").value("16-400"))
                .andExpect(jsonPath("$.data.city").value("Suwalki"))
                .andExpect(jsonPath("$.data.province").value("Podlaskie"))
                .andExpect(jsonPath("$.data.country").value("Polska"));
    }

    @Test
    @DisplayName("Check getAddress non-existent (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetAddressEmptyNonExistent() throws Exception {
        LoginDTO loginDTO = new LoginDTO("err@gmail.com", "123");

        ResultActions resultActions = this.mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginDTO))
                .accept(MediaType.APPLICATION_JSON));
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        this.token = "Bearer " + json.getJSONObject("data").getString("token");

        this.mockMvc.perform(get(this.baseUrl + "/address")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Address retrieved"))
                .andExpect(jsonPath("$.data.name").value(""))
                .andExpect(jsonPath("$.data.street").value(""))
                .andExpect(jsonPath("$.data.postalCode").value(""))
                .andExpect(jsonPath("$.data.city").value(""))
                .andExpect(jsonPath("$.data.province").value(""))
                .andExpect(jsonPath("$.data.country").value(""));
    }
}