package com.springbootangularshop.springbootbackend.order;

import com.springbootangularshop.springbootbackend.cart.Cart;
import com.springbootangularshop.springbootbackend.cart.CartItem;
import com.springbootangularshop.springbootbackend.cart.CartServiceImpl;
import com.springbootangularshop.springbootbackend.product.Product;
import com.springbootangularshop.springbootbackend.system.exception.CartEmptyException;
import com.springbootangularshop.springbootbackend.system.exception.ObjectNotFoundException;
import com.springbootangularshop.springbootbackend.user.Address;
import com.springbootangularshop.springbootbackend.user.User;
import com.springbootangularshop.springbootbackend.user.UserRepository;
import com.springbootangularshop.springbootbackend.user.UserServiceImpl;
import com.springbootangularshop.springbootbackend.user.converter.AddressDTOToAddressConverter;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    AddressDTOToAddressConverter addressDTOToAddressConverter;

    @Mock
    OrderRepository orderRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CartServiceImpl cartService;

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    OrderServiceImpl orderService;

    User user;
    Address address;
    Order order1;
    Order order2;
    OrderItem orderItem1;
    OrderItem orderItem2;
    List<Order> orders;
    Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("joshhomme568");
        user.setEmail("joshhomme@gmail.com");
        user.setPassword("!Jjoshhomme568");

        address = new Address();
        address.setId(1L);
        address.setName("Jonas");
        address.setStreet("100 Street");
        address.setPostalCode("17-000");
        address.setCity("Poznan");
        address.setProvince("WLKP");
        address.setCountry("Poland");
        user.addAddress(address);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        order1 = new Order();
        order1.setId("1749824029966483456");
        calendar.add(Calendar.MONTH, -5);
        order1.setOrderDate(calendar.getTime());
        order1.setOrderStatus(OrderStatus.PENDING);
        order1.setBuyerEmail(user.getEmail());
        order1.setTotal(BigDecimal.valueOf(399.98));

        orderItem1 = new OrderItem();
        orderItem1.setId(1L);
        orderItem1.setProductItemId("6196d0fedca24002bbcc259c7e955fba");
        orderItem1.setProductName("Core Purple Boots");
        orderItem1.setPictureUrl("");
        orderItem1.setUnitPrice(BigDecimal.valueOf(199.98));
        orderItem1.setQuantity(2);

        order1.addOrderItem(orderItem1);
        address.addOrder(order1);

        order2 = new Order();
        order2.setId("1749824029966483433");
        calendar.add(Calendar.MONTH, 10);
        order2.setOrderDate(calendar.getTime());
        order2.setOrderStatus(OrderStatus.PENDING);
        order2.setBuyerEmail(user.getEmail());
        order2.setTotal(BigDecimal.valueOf(30));

        orderItem2 = new OrderItem();
        orderItem2.setId(2L);
        orderItem2.setProductItemId("6196d0fedca24002bbcc259c7e955fgh");
        orderItem2.setProductName("Spring Blue Hatts");
        orderItem2.setPictureUrl("");
        orderItem2.setUnitPrice(BigDecimal.valueOf(10));
        orderItem2.setQuantity(3);

        order2.addOrderItem(orderItem2);
        address.addOrder(order2);

        orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        Product product = new Product();
        product.setId("6226d0fedca24002bbcc259c7e955fba");
        product.setDescription("Product description 2");
        product.setName("Green Angular Board 3000");
        product.setPictureUrl("");
        product.setUnitPrice(BigDecimal.valueOf(150));

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.addProduct(product);
        cartItem.setQuantity(1);
        user.addCartItem(cartItem);

        cart = new Cart();
        cart.setId(2L);
        cart.addCartItem(cartItem);
        cart.setTotalItems(1);
        cart.setTotal(BigDecimal.valueOf(150));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateOrderSuccess() {
        // Given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("John");
        addressDTO.setStreet("150 Street");
        addressDTO.setPostalCode("08-000");
        addressDTO.setCity("Wroclaw");
        addressDTO.setProvince("MLP");
        addressDTO.setCountry("Poland");

        Address orderAddress = new Address();
        orderAddress.setId(2L);
        orderAddress.setName("John");
        orderAddress.setStreet("150 Street");
        orderAddress.setPostalCode("08-000");
        orderAddress.setCity("Wroclaw");
        orderAddress.setProvince("MLP");
        orderAddress.setCountry("Poland");

        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);
        given(this.cartService.getCart("joshhomme@gmail.com")).willReturn(cart);
        given(this.addressDTOToAddressConverter.convert(addressDTO)).willReturn(orderAddress);
        given(this.userService.updateAddress(orderAddress, "joshhomme@gmail.com")).willReturn(orderAddress);

        Order mockOrder = new Order();
        mockOrder.setId("123456L");
        mockOrder.setOrderStatus(OrderStatus.PENDING);
        mockOrder.setTotal(BigDecimal.valueOf(150));
        mockOrder.setShippingAddress(orderAddress);
        mockOrder.setBuyerEmail("joshhomme@gmail.com");

        given(this.orderRepository.save(Mockito.any(Order.class))).willReturn(mockOrder);

        // When
        Order order = this.orderService.createOrder(addressDTO, "joshhomme@gmail.com");

        // Then
        assertThat(order.getId()).isEqualTo("123456L");
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getTotal()).isEqualTo(BigDecimal.valueOf(150));
        assertThat(order.getShippingAddress()).isEqualTo(orderAddress);
        assertThat(order.getBuyerEmail()).isEqualTo("joshhomme@gmail.com");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.cartService, times(1)).getCart("joshhomme@gmail.com");
        verify(this.userService, times(1)).updateAddress(orderAddress, "joshhomme@gmail.com");
        verify(this.orderRepository, times(1)).save(Mockito.any(Order.class));
    }

    @Test
    void testCreateOrderUserNotFound() {
        // Given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("John");
        addressDTO.setStreet("150 Street");
        addressDTO.setPostalCode("08-000");
        addressDTO.setCity("Wroclaw");
        addressDTO.setProvince("MLP");
        addressDTO.setCountry("Poland");

        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(null);

        // When
        Throwable thrown = catchThrowable(() -> this.orderService.createOrder(addressDTO, "joshhomme@gmail.com"));

        // Then
        assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with email 'joshhomme@gmail.com' not found");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
    }

    @Test
    void testCreateOrderEmptyCart() {
        // Given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("John");
        addressDTO.setStreet("150 Street");
        addressDTO.setPostalCode("08-000");
        addressDTO.setCity("Wroclaw");
        addressDTO.setProvince("MLP");
        addressDTO.setCountry("Poland");

        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);
        given(this.cartService.getCart("joshhomme@gmail.com")).willReturn(new Cart());

        // When
        Throwable thrown = catchThrowable(() -> this.orderService.createOrder(addressDTO, "joshhomme@gmail.com"));

        // Then
        assertThat(thrown)
                .isInstanceOf(CartEmptyException.class)
                .hasMessage("You cannot place the order. Your cart is empty");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.cartService, times(1)).getCart("joshhomme@gmail.com");
    }

    @Test
    void testGetOrdersSuccess() {
        // Given
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);
        given(this.orderRepository.findByBuyerEmail("joshhomme@gmail.com")).willReturn(this.orders);

        // When
        List<Order> actualOrders = this.orderService.getOrders("joshhomme@gmail.com");

        //Then
        assertThat(actualOrders.size()).isEqualTo(this.orders.size());
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.orderRepository, times(1)).findByBuyerEmail("joshhomme@gmail.com");
    }

    @Test
    void testGetOrdersUserNotFound() {
        // Given
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(null);

        // When
        Throwable thrown = catchThrowable(() -> this.orderService.getOrders("joshhomme@gmail.com"));

        // Then
        assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with email 'joshhomme@gmail.com' not found");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
    }

    @Test
    void testGetOrderByIdSuccess() {
        // Given
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);
        given(this.orderRepository.findByBuyerEmail("joshhomme@gmail.com")).willReturn(this.orders);

        // When
        Order foundOrder = this.orderService.getOrderById("1749824029966483456", "joshhomme@gmail.com");

        //Then
        assertThat(foundOrder.getId()).isEqualTo("1749824029966483456");
        assertThat(foundOrder.getTotal()).isEqualTo(BigDecimal.valueOf(399.98));
        assertThat(foundOrder.getOrderItems().size()).isEqualTo(1);
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.orderRepository, times(1)).findByBuyerEmail("joshhomme@gmail.com");
    }

    @Test
    void testGetOrderByIdUserNotFound() {
        // Given
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(null);

        // When
        Throwable thrown = catchThrowable(() -> this.orderService.getOrderById("1749824029966483456", "joshhomme@gmail.com"));

        // Then
        assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with email 'joshhomme@gmail.com' not found");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
    }

    @Test
    void testGetOrderByIdOrderNotFound() {
        // Given
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);
        given(this.orderRepository.findByBuyerEmail("joshhomme@gmail.com")).willReturn(this.orders);

        // When
        Throwable thrown = catchThrowable(() -> this.orderService.getOrderById("29966483456", "joshhomme@gmail.com"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find order with Id 29966483456");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.orderRepository, times(1)).findByBuyerEmail("joshhomme@gmail.com");
    }
}