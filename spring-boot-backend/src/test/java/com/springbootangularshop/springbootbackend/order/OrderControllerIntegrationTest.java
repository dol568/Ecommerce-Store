package com.springbootangularshop.springbootbackend.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootangularshop.springbootbackend.cart.dto.CartItemDTO;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
import com.springbootangularshop.springbootbackend.user.dto.LoginDTO;
import org.hamcrest.Matchers;
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
class OrderControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    String baseUrl = "/api/order";
    String baseUrlCart = "/api/cart";

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
    @DisplayName("Check getOrders (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetOrdersSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Orders retrieved"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(1)));
    }

    @Test
    @DisplayName("Check getOrderById (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetOrderByIdSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/c7dc2294-e712-4399-91b0-aca5632105e4")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Order with id 'c7dc2294-e712-4399-91b0-aca5632105e4' retrieved"))
                .andExpect(jsonPath("$.data.id").value("c7dc2294-e712-4399-91b0-aca5632105e4"))
                .andExpect(jsonPath("$.data.total").value(8))
                .andExpect(jsonPath("$.data.orderStatus").value("PENDING"))
                .andExpect(jsonPath("$.data.shippingAddress.name").value("Darek"));
    }

    @Test
    @DisplayName("Check getOrderById with non-existent id (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetOrderByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/175044538947445964")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Could not find order with Id 175044538947445964"));
    }

    @Test
    @DisplayName("Check createOrder with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testCreateOrderSuccess() throws Exception {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca26002bbcc259c7e955fba");
        cartItemDTO.setQuantity(4);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("John");
        addressDTO.setStreet("150 Street");
        addressDTO.setPostalCode("08-000");
        addressDTO.setCity("Wroclaw");
        addressDTO.setProvince("MLP");
        addressDTO.setCountry("Poland");

        this.mockMvc.perform(post(this.baseUrlCart)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(cartItemDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("Cart Item added"))
                .andExpect(jsonPath("$.data.total").value(720))
                .andExpect(jsonPath("$.data.totalItems").value(4))
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(1)));
        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(addressDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("New Order created"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.buyerEmail").value("dar@gmail.com"))
                .andExpect(jsonPath("$.data.total").value(720))
                .andExpect(jsonPath("$.data.shippingAddress.name").value("John"));
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Orders retrieved"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("Check createOrder with empty cart (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testCreateOrderErrorEmptyCart() throws Exception {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("John");
        addressDTO.setStreet("150 Street");
        addressDTO.setPostalCode("08-000");
        addressDTO.setCity("Wroclaw");
        addressDTO.setProvince("MLP");
        addressDTO.setCountry("Poland");

        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(addressDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("You cannot place the order. Your cart is empty"));
    }

    @Test
    @DisplayName("Check createOrder with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testCreateOrderErrorInvalidInput() throws Exception {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("");
        addressDTO.setStreet("");
        addressDTO.setPostalCode("");
        addressDTO.setCity("");
        addressDTO.setProvince("");
        addressDTO.setCountry("");

        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(addressDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.name").value("Name cannot be blank"))
                .andExpect(jsonPath("$.data.street").value("Street cannot be blank"))
                .andExpect(jsonPath("$.data.postalCode").value("Postal Code cannot be blank"))
                .andExpect(jsonPath("$.data.city").value("City cannot be blank"))
                .andExpect(jsonPath("$.data.province").value("Province cannot be blank"))
                .andExpect(jsonPath("$.data.country").value("Country cannot be blank"));
    }
}