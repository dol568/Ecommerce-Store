package com.springbootangularshop.springbootbackend.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootangularshop.springbootbackend.cart.dto.CartItemDTO;
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
class CartControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;

    String baseUrl = "/api/cart";

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
    @DisplayName("Check getCart (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetCartSuccess() throws Exception {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca26002bbcc259c7e955fba");
        cartItemDTO.setQuantity(4);

        this.mockMvc.perform(post(this.baseUrl)
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
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart retrieved"))
                .andExpect(jsonPath("$.data.total").value(720))
                .andExpect(jsonPath("$.data.totalItems").value(4))
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(1)));
    }

    @Test
    @DisplayName("Check getCart empty (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetCartEmpty() throws Exception {
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart retrieved"))
                .andExpect(jsonPath("$.data.total").value(0))
                .andExpect(jsonPath("$.data.totalItems").doesNotExist())
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("Check addCartItem with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddCartItemSuccess() throws Exception {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca26002bbcc259c7e955fba");
        cartItemDTO.setQuantity(4);

        this.mockMvc.perform(post(this.baseUrl)
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
    }

    @Test
    @DisplayName("Check addCartItem with invalid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddCartItemErrorInvalidInput() throws Exception {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("");
        cartItemDTO.setQuantity(null);

        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(cartItemDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.productId").value("ProductId cannot be blank"))
                .andExpect(jsonPath("$.data.quantity").value("Quantity cannot be blank"));
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart retrieved"))
                .andExpect(jsonPath("$.data.total").value(0))
                .andExpect(jsonPath("$.data.totalItems").doesNotExist())
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("Check addCartItem with non-existent product id (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testAddCartItemErrorNonExistentProductId() throws Exception {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("123");
        cartItemDTO.setQuantity(4);

        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(cartItemDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Could not find product with Id 123"));
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart retrieved"))
                .andExpect(jsonPath("$.data.total").value(0))
                .andExpect(jsonPath("$.data.totalItems").doesNotExist())
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("Check deleteCartItem with valid input (DELETE)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testDeleteCartItemSuccess() throws Exception {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca26002bbcc259c7e955fba");
        cartItemDTO.setQuantity(4);

        this.mockMvc.perform(post(this.baseUrl)
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
        this.mockMvc.perform(delete(this.baseUrl + "/6126d0fedca26002bbcc259c7e955fba")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart Item with Product ID '6126d0fedca26002bbcc259c7e955fba' deleted"))
                .andExpect(jsonPath("$.data.total").value(0))
                .andExpect(jsonPath("$.data.totalItems").doesNotExist())
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("Check deleteCartItem with non-existent product id (DELETE)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testDeleteCartItemErrorNonExistentProductId() throws Exception {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca26002bbcc259c7e955fba");
        cartItemDTO.setQuantity(4);

        this.mockMvc.perform(post(this.baseUrl)
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
        this.mockMvc.perform(delete(this.baseUrl + "/123")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Could not find cart item with product Id 123"));
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart retrieved"))
                .andExpect(jsonPath("$.data.total").value(720))
                .andExpect(jsonPath("$.data.totalItems").value(4))
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(1)));
    }

    @Test
    @DisplayName("Check clearCart (DELETE)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testClearCartSuccess() throws Exception {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca26002bbcc259c7e955fba");
        cartItemDTO.setQuantity(4);

        this.mockMvc.perform(post(this.baseUrl)
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
        this.mockMvc.perform(delete(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart has been cleared out"))
                .andExpect(jsonPath("$.data.total").value(0))
                .andExpect(jsonPath("$.data.totalItems").doesNotExist())
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(0)));
    }
}