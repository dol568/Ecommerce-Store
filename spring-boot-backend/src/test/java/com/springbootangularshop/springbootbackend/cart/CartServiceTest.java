package com.springbootangularshop.springbootbackend.cart;

import com.springbootangularshop.springbootbackend.cart.dto.CartItemDTO;
import com.springbootangularshop.springbootbackend.product.Product;
import com.springbootangularshop.springbootbackend.product.ProductRepository;
import com.springbootangularshop.springbootbackend.system.exception.ObjectNotFoundException;
import com.springbootangularshop.springbootbackend.user.User;
import com.springbootangularshop.springbootbackend.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @InjectMocks
    CartServiceImpl cartService;

    User user;
    Cart cart1;
    Cart cart2;
    Product product1;
    Product product2;
    Product product3;
    CartItem cartItem1;
    CartItem cartItem2;

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

        product3 = new Product();
        product3.setId("6826d0fedca24002bbcc259c7e955fba");
        product3.setDescription("Product description 3");
        product3.setName("Green React Woolen Hat");
        product3.setPictureUrl("");
        product3.setUnitPrice(BigDecimal.valueOf(8));

        cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.addProduct(product1);
        cartItem1.setQuantity(2);
        user.addCartItem(cartItem1);

        cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.addProduct(product2);
        cartItem2.setQuantity(1);
        user.addCartItem(cartItem2);

        cart1 = new Cart();
        cart1.setId(1L);
        cart1.addCartItem(cartItem1);
        cart1.addCartItem(cartItem2);
        cart1.setTotalItems(3);
        cart1.setTotal(BigDecimal.valueOf(550));

        cart2 = new Cart();
        cart2.setId(2L);
        cart2.addCartItem(cartItem2);
        cart2.setTotalItems(1);
        cart2.setTotal(BigDecimal.valueOf(150));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetCartSuccess() {
        // Given
        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.of(cart1));

        // When
        Cart foundCart = this.cartService.getCart("joshhomme@gmail.com");

        // Then
        assertThat(foundCart.getId()).isEqualTo(1L);
        assertThat(foundCart.getCartItems().size()).isEqualTo(2);
        assertThat(foundCart.getTotal()).isEqualTo(BigDecimal.valueOf(550));
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
    }

    @Test
    void testGetCartEmpty() {
        // Given
        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.empty());

        // When
        Cart foundCart = this.cartService.getCart("joshhomme@gmail.com");

        // Then
        assertThat(foundCart.getId()).isNull();
        assertThat(foundCart.getCartItems()).isEmpty();
        assertThat(foundCart.getTotal()).isEqualTo(BigDecimal.ZERO);
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
    }

    @Test
    void testAddCartItemSuccess() {
        // Given
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6826d0fedca24002bbcc259c7e955fba");
        cartItemDTO.setQuantity(2);

        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.of(cart1));
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);
        given(this.productRepository.findById("6826d0fedca24002bbcc259c7e955fba")).willReturn(Optional.of(product3));
        given(this.cartItemRepository
                .findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6826d0fedca24002bbcc259c7e955fba"))
                .willReturn(null);

        // When
        CartItem cartItem = this.cartService.addCartItem(cartItemDTO, "joshhomme@gmail.com");

        // Then
        assertThat(cart1.getTotal()).isEqualTo(BigDecimal.valueOf(566));
        assertThat(cart1.getTotalItems()).isEqualTo(5);
        assertThat(cartItem.getQuantity()).isEqualTo(cartItemDTO.getQuantity());
        assertThat(cartItem.getProduct().getId()).isEqualTo("6826d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.productRepository, times(1)).findById("6826d0fedca24002bbcc259c7e955fba");
        verify(this.cartItemRepository, times(1)).findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6826d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(1)).save(cart1);
    }

    @Test
    void testAddCartItemAlreadyInCart() {
        // Given
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca24002bbcc259c7e955fba");
        cartItemDTO.setQuantity(3);

        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.of(cart1));
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);
        given(this.productRepository.findById("6126d0fedca24002bbcc259c7e955fba")).willReturn(Optional.of(product1));
        given(this.cartItemRepository
                .findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6126d0fedca24002bbcc259c7e955fba"))
                .willReturn(cartItem1);

        // When
        CartItem cartItem = this.cartService.addCartItem(cartItemDTO, "joshhomme@gmail.com");

        // Then
        assertThat(cart1.getTotal()).isEqualTo(BigDecimal.valueOf(750));
        assertThat(cart1.getTotalItems()).isEqualTo(4);
        assertThat(cartItem.getQuantity()).isEqualTo(cartItemDTO.getQuantity());
        assertThat(cartItem.getProduct().getId()).isEqualTo("6126d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.productRepository, times(1)).findById("6126d0fedca24002bbcc259c7e955fba");
        verify(this.cartItemRepository, times(1)).findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6126d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(1)).save(cart1);
    }

    @Test
    void testAddCartItemEmptyCart() {
        // Given
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca24002bbcc259c7e955fba");
        cartItemDTO.setQuantity(3);

        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.empty());
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);
        given(this.productRepository.findById("6126d0fedca24002bbcc259c7e955fba")).willReturn(Optional.of(product1));
        given(this.cartItemRepository
                .findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6126d0fedca24002bbcc259c7e955fba"))
                .willReturn(null);

        // When
        CartItem cartItem = this.cartService.addCartItem(cartItemDTO, "joshhomme@gmail.com");

        // Then
        assertThat(cartItem.getCart().getTotal()).isEqualTo(BigDecimal.valueOf(600));
        assertThat(cartItem.getCart().getTotalItems()).isEqualTo(3);
        assertThat(cartItem.getQuantity()).isEqualTo(cartItemDTO.getQuantity());
        assertThat(cartItem.getProduct().getId()).isEqualTo("6126d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.productRepository, times(1)).findById("6126d0fedca24002bbcc259c7e955fba");
        verify(this.cartItemRepository, times(1)).findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6126d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(1)).save(Mockito.any(Cart.class));
    }

    @Test
    void testAddCartItemUserNotFound() {
        // Given
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca24002bbcc259c7e955fba");
        cartItemDTO.setQuantity(3);

        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.of(cart1));
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(null);

        // When
        Throwable thrown = catchThrowable(() -> this.cartService.addCartItem(cartItemDTO, "joshhomme@gmail.com"));

        // Then
        assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with email 'joshhomme@gmail.com' not found");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
    }

    @Test
    void testAddCartItemProductNotFound() {
        // Given
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId("6126d0fedca24002bbcc259c7e955fba");
        cartItemDTO.setQuantity(3);

        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.of(cart1));
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);
        given(this.productRepository.findById("6126d0fedca24002bbcc259c7e955fba")).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.cartService.addCartItem(cartItemDTO, "joshhomme@gmail.com");
        });

        // Then
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.productRepository, times(1)).findById("6126d0fedca24002bbcc259c7e955fba");
    }

    @Test
    void testDeleteCartItemSuccess() {
        // Given
        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.of(cart1));
        given(this.cartItemRepository
                .findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6126d0fedca24002bbcc259c7e955fba"))
                .willReturn(cartItem1);

        // When
        this.cartService.deleteCartItem("6126d0fedca24002bbcc259c7e955fba", "joshhomme@gmail.com");

        // Then
        assertThat(cart1.getTotal()).isEqualTo(BigDecimal.valueOf(150));
        assertThat(cart1.getTotalItems()).isEqualTo(1);
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.cartItemRepository, times(1)).findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6126d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(1)).save(cart1);
    }

    @Test
    void testDeleteCartItemDeleteEmptyCart() {
        // Given
        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.of(cart2));
        given(this.cartItemRepository
                .findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6226d0fedca24002bbcc259c7e955fba"))
                .willReturn(cartItem2);
        given(this.cartRepository.findCartByCartItemsIsEmpty()).willReturn(cart2);
        doNothing().when(this.cartRepository).delete(cart2);

        // When
        this.cartService.deleteCartItem("6226d0fedca24002bbcc259c7e955fba", "joshhomme@gmail.com");

        // Then
        assertThat(cart2.getCartItems()).isEmpty();
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.cartItemRepository, times(1)).findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6226d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(1)).save(cart2);
        verify(this.cartRepository, times(1)).findCartByCartItemsIsEmpty();
        verify(this.cartRepository, times(1)).delete(cart2);
    }

    @Test
    void testDeleteCartItemCartNotFound() {
        // Given
        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.empty());
        given(this.cartItemRepository
                .findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6226d0fedca24002bbcc259c7e955fba"))
                .willReturn(null);

        // When
        Throwable thrown = catchThrowable(() -> this.cartService.deleteCartItem("6226d0fedca24002bbcc259c7e955fba", "joshhomme@gmail.com"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find cart item with product Id 6226d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.cartItemRepository, times(1)).findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6226d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(0)).save(Mockito.any(Cart.class));
    }

    @Test
    void testDeleteCartItemNotFound() {
        // Given
        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.of(cart2));
        given(this.cartItemRepository
                .findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6226d0fedca24002bbcc259c7e955fba"))
                .willReturn(null);

        // When
        Throwable thrown = catchThrowable(() -> this.cartService.deleteCartItem("6226d0fedca24002bbcc259c7e955fba", "joshhomme@gmail.com"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find cart item with product Id 6226d0fedca24002bbcc259c7e955fba");
        assertThat(cart2.getCartItems()).isNotEmpty();
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.cartItemRepository, times(1)).findCartItemByUser_Email_AndProduct_Id("joshhomme@gmail.com", "6226d0fedca24002bbcc259c7e955fba");
        verify(this.cartRepository, times(0)).save(Mockito.any(Cart.class));
    }

    @Test
    void testDeleteAllCartItemsInCartSuccess() {
        //Given
        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.of(cart2));
        doNothing().when(this.cartRepository).delete(cart2);

        // When
        this.cartService.deleteAllCartItemsInCart("joshhomme@gmail.com");

        // Then
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.cartRepository, times(1)).delete(cart2);
    }

    @Test
    void testDeleteAllCartItemsInCartEmpty() {
        //Given
        given(this.cartRepository.findCartByCartItems_User_Email("joshhomme@gmail.com")).willReturn(Optional.empty());

        // When
        this.cartService.deleteAllCartItemsInCart("joshhomme@gmail.com");

        // Then
        verify(this.cartRepository, times(1)).findCartByCartItems_User_Email("joshhomme@gmail.com");
        verify(this.cartRepository, times(0)).delete(cart2);
    }
}