package com.springbootangularshop.springbootbackend.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootangularshop.springbootbackend.cart.converter.CartToReturnCartConverter;
import com.springbootangularshop.springbootbackend.cart.dto.CartItemDTO;
import com.springbootangularshop.springbootbackend.cart.dto.ReturnCart;
import com.springbootangularshop.springbootbackend.cart.dto.ReturnCartItem;
import com.springbootangularshop.springbootbackend.product.Product;
import com.springbootangularshop.springbootbackend.system.exception.CustomResponseEntityExceptionHandler;
import com.springbootangularshop.springbootbackend.system.exception.ObjectNotFoundException;
import com.springbootangularshop.springbootbackend.user.User;
import org.hamcrest.Matchers;
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

import java.math.BigDecimal;
import java.security.Principal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = CartController.class)
@Import(CustomResponseEntityExceptionHandler.class)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    Principal mockPrincipal;

    @MockBean
    CartServiceImpl cartService;

    @MockBean
    CartToReturnCartConverter cartToReturnCartConverter;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "/api/cart";

    User user;
    Cart cart1;
    Product product1;
    Product product2;
    CartItem cartItem1;
    CartItem cartItem2;
    ReturnCartItem returnCartItem1;
    ReturnCartItem returnCartItem2;
    ReturnCart returnCart1;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("joshhomme568");
        user.setEmail("joshhomme@gmail.com");
        user.setPassword("!Jjoshhomme568");

        product1 = new Product();
        product1.setId("6126d0fedca24002bbcc259c7e955fba");
        product1.setDescription("Product description 1");
        product1.setName("Angular Speedster Board 2000");
        product1.setPictureUrl("");
        product1.setUnitPrice(BigDecimal.valueOf(200));

        product2 = new Product();
        product2.setId("6226d0fedca24002bbcc259c7e955fba");
        product2.setDescription("Product description 2");
        product2.setName("Green Angular Board 3000");
        product2.setPictureUrl("");
        product2.setUnitPrice(BigDecimal.valueOf(150));

        cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.addProduct(product1);
        cartItem1.setQuantity(2);
        user.addCartItem(cartItem1);

        returnCartItem1 = new ReturnCartItem();
        returnCartItem1.setId(1L);
        returnCartItem1.setProductId(product1.getId());
        returnCartItem1.setProductName(product1.getName());
        returnCartItem1.setQuantity(2);
        returnCartItem1.setUnitPrice(product1.getUnitPrice());

        cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.addProduct(product2);
        cartItem2.setQuantity(1);
        user.addCartItem(cartItem2);

        returnCartItem2 = new ReturnCartItem();
        returnCartItem2.setId(2L);
        returnCartItem2.setProductId(product2.getId());
        returnCartItem2.setProductName(product2.getName());
        returnCartItem2.setQuantity(1);
        returnCartItem2.setUnitPrice(product2.getUnitPrice());

        cart1 = new Cart();
        cart1.setId(1L);
        cart1.addCartItem(cartItem1);
        cart1.addCartItem(cartItem2);
        cart1.setTotalItems(3);
        cart1.setTotal(BigDecimal.valueOf(550));

        returnCart1 = new ReturnCart();
        returnCart1.setTotal(BigDecimal.valueOf(550));
        returnCart1.setTotalItems(3);
        returnCart1.getCartItems().add(returnCartItem1);
        returnCart1.getCartItems().add(returnCartItem2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetCartSuccess() throws Exception {
        // Given
        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");

        given(this.cartService.getCart("joshhomme@gmail.com")).willReturn(this.cart1);

        given(this.cartToReturnCartConverter.convert(this.cart1)).willReturn(returnCart1);

        // When and then
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart retrieved"))
                .andExpect(jsonPath("$.data.total").value(cart1.getTotal()))
                .andExpect(jsonPath("$.data.totalItems").value(cart1.getTotalItems()))
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(2)));
    }

    @Test
    void testGetCartEmpty() throws Exception {
        // Given
        Cart emptyCart = new Cart();
        ReturnCart returnCartEmpty = new ReturnCart();

        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");

        given(this.cartService.getCart("joshhomme@gmail.com")).willReturn(emptyCart);

        given(this.cartToReturnCartConverter.convert(emptyCart)).willReturn(returnCartEmpty);

        // When and then
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart retrieved"))
                .andExpect(jsonPath("$.data.total").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.data.totalItems").doesNotExist())
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(0)));
    }

    @Test
    void testAddCartItemSuccess() throws Exception {
        // Given
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca24002bbcc259c7e955fba");
        cartItemDTO.setQuantity(4);

        String json = this.objectMapper.writeValueAsString(cartItemDTO);

        CartItem cartItem = new CartItem();
        cartItem.setId(3L);
        cartItem.addProduct(product1);
        cartItem.setQuantity(4);

        ReturnCartItem returnCartItem = new ReturnCartItem();
        returnCartItem.setId(1L);
        returnCartItem.setProductId(product1.getId());
        returnCartItem.setProductName(product1.getName());
        returnCartItem.setQuantity(4);
        returnCartItem.setUnitPrice(product1.getUnitPrice());

        Cart cart = new Cart();
        cart.setId(1L);
        cart.addCartItem(cartItem);
        cart.setTotalItems(4);
        cart.setTotal(BigDecimal.valueOf(800));

        ReturnCart returnCart = new ReturnCart();
        returnCart.setTotal(BigDecimal.valueOf(800));
        returnCart.setTotalItems(4);
        returnCart.getCartItems().add(returnCartItem);

        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");

        given(this.cartService.addCartItem(cartItemDTO, "joshhomme@gmail.com"))
                .willReturn(cartItem);

        given(this.cartService.getCart("joshhomme@gmail.com")).willReturn(cart);

        given(this.cartToReturnCartConverter.convert(cart)).willReturn(returnCart);

        // When and then
        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("Cart Item added"))
                .andExpect(jsonPath("$.data.totalItems").value(cartItemDTO.getQuantity()))
                .andExpect(jsonPath("$.data.total").value(cart.getTotal()))
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(1)));
    }

    @Test
    void testAddCartItemInvalidInput() throws Exception {
        // Given
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("");
        cartItemDTO.setQuantity(null);

        String json = this.objectMapper.writeValueAsString(cartItemDTO);

        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");

        // When and then
        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.productId").value("ProductId cannot be blank"))
                .andExpect(jsonPath("$.data.quantity").value("Quantity cannot be blank"));
    }

    @Test
    void testAddCartItemProductNotFound() throws Exception {
        // Given
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca24002bbcc259c7e955fba");
        cartItemDTO.setQuantity(4);

        String json = this.objectMapper.writeValueAsString(cartItemDTO);

        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");

        given(this.cartService.addCartItem(cartItemDTO, "joshhomme@gmail.com"))
                .willThrow(new ObjectNotFoundException("product", cartItemDTO.getProductId()));

        // When and then
        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Could not find product with Id 6126d0fedca24002bbcc259c7e955fba"));
    }

    @Test
    void testDeleteCartItemSuccess() throws Exception {
        // Given
        Cart emptyCart = new Cart();
        ReturnCart emptyReturnCart = new ReturnCart();

        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");
        doNothing().when(this.cartService).deleteCartItem("6126d0fedca24002bbcc259c7e955fba", "joshhomme@gmail.com");

        given(this.cartService.getCart("joshhomme@gmail.com")).willReturn(emptyCart);

        given(this.cartToReturnCartConverter.convert(emptyCart)).willReturn(emptyReturnCart);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/6126d0fedca24002bbcc259c7e955fba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart Item with Product ID '6126d0fedca24002bbcc259c7e955fba' deleted"))
                .andExpect(jsonPath("$.data.total").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(0)));
    }

    @Test
    void testDeleteCartItemNotFound() throws Exception {
        // Given
        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");

        doThrow(new ObjectNotFoundException("cart item", "product", "6126d0fedca24002bbcc259c7e955fba"))
                .when(this.cartService).deleteCartItem("6126d0fedca24002bbcc259c7e955fba", "joshhomme@gmail.com");

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/6126d0fedca24002bbcc259c7e955fba")
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Could not find cart item with product Id 6126d0fedca24002bbcc259c7e955fba"));
    }

    @Test
    void testClearCartSuccess() throws Exception {
        // Given
        Cart emptyCart = new Cart();
        ReturnCart emptyReturnCart = new ReturnCart();

        given(mockPrincipal.getName()).willReturn("joshhomme@gmail.com");
        doNothing().when(this.cartService).deleteAllCartItemsInCart("joshhomme@gmail.com");

        given(this.cartService.getCart("joshhomme@gmail.com")).willReturn(emptyCart);

        given(this.cartToReturnCartConverter.convert(emptyCart)).willReturn(emptyReturnCart);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Cart has been cleared out"))
                .andExpect(jsonPath("$.data.total").value(BigDecimal.ZERO))
                .andExpect(jsonPath("$.data.cartItems", Matchers.hasSize(0)));
    }
}