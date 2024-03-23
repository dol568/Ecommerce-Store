package com.springbootangularshop.springbootbackend.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootangularshop.springbootbackend.order.converter.OrderToReturnOrderDTOConverter;
import com.springbootangularshop.springbootbackend.order.converter.OrdersToReturnOrdersDTOConverter;
import com.springbootangularshop.springbootbackend.order.dto.ReturnOrderDTO;
import com.springbootangularshop.springbootbackend.system.exception.CartEmptyException;
import com.springbootangularshop.springbootbackend.system.exception.CustomResponseEntityExceptionHandler;
import com.springbootangularshop.springbootbackend.system.exception.ObjectNotFoundException;
import com.springbootangularshop.springbootbackend.user.Address;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = OrderController.class)
@Import(CustomResponseEntityExceptionHandler.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderServiceImpl orderService;

    @MockBean
    OrderToReturnOrderDTOConverter orderToReturnOrderDTOConverter;

    @MockBean
    OrdersToReturnOrdersDTOConverter ordersToReturnOrdersDTOConverter;

    @MockBean
    Principal mockPrincipal;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "/api/order";

    ReturnOrderDTO returnOrderDTO1;
    ReturnOrderDTO returnOrderDTO2;
    Order order1;
    Order order2;

    List<ReturnOrderDTO> ordersDTO;
    List<Order> orders;

    @BeforeEach
    void setUp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        this.ordersDTO = new ArrayList<>();
        this.orders = new ArrayList<>();

        order1 = new Order();
        order1.setId("1749824029966483456");
        calendar.add(Calendar.MONTH, -5);
        order1.setOrderDate(calendar.getTime());
        order1.setOrderStatus(OrderStatus.PENDING);
        order1.setBuyerEmail("joshhomme@gmail.com");
        order1.setTotal(BigDecimal.valueOf(399.98));

        returnOrderDTO1 = new ReturnOrderDTO();
        returnOrderDTO1.setId("1749824029966483456");
        returnOrderDTO1.setOrderDate(calendar.getTime());
        returnOrderDTO1.setOrderStatus(OrderStatus.PENDING);
        returnOrderDTO1.setBuyerEmail("joshhomme@gmail.com");
        returnOrderDTO1.setTotal(BigDecimal.valueOf(399.98));

        order2 = new Order();
        order2.setId("1749824029966483433");
        calendar.add(Calendar.MONTH, 10);
        order2.setOrderDate(calendar.getTime());
        order2.setOrderStatus(OrderStatus.PENDING);
        order2.setBuyerEmail("joshhomme@gmail.com");
        order2.setTotal(BigDecimal.valueOf(30));

        returnOrderDTO2 = new ReturnOrderDTO();
        returnOrderDTO2.setId("1749824029966483433");
        returnOrderDTO2.setOrderDate(calendar.getTime());
        returnOrderDTO2.setOrderStatus(OrderStatus.PENDING);
        returnOrderDTO2.setBuyerEmail("joshhomme@gmail.com");
        returnOrderDTO2.setTotal(BigDecimal.valueOf(30));

        orders.add(order1);
        orders.add(order2);
        ordersDTO.add(returnOrderDTO1);
        ordersDTO.add(returnOrderDTO2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetOrdersSuccess() throws Exception {
        // Given
        given(mockPrincipal.getName()).willReturn("me");
        given(this.orderService.getOrders(Mockito.any(String.class))).willReturn(this.orders);
        given(this.ordersToReturnOrdersDTOConverter.convert(this.orders)).willReturn(this.ordersDTO);

        // When and then
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Orders retrieved"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.orders.size())))
                .andExpect(jsonPath("$.data[0].id").value("1749824029966483456"))
                .andExpect(jsonPath("$.data[0].total").value(BigDecimal.valueOf(399.98)))
                .andExpect(jsonPath("$.data[1].id").value("1749824029966483433"))
                .andExpect(jsonPath("$.data[1].total").value(BigDecimal.valueOf(30)));
    }

    @Test
    void testGetOrderByIdSuccess() throws Exception {
        // Given
        given(mockPrincipal.getName()).willReturn("me");
        given(this.orderService.getOrderById(eq("1749824029966483456"), Mockito.any(String.class))).willReturn(this.order1);
        given(this.orderToReturnOrderDTOConverter.convert(this.order1)).willReturn(this.returnOrderDTO1);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/1749824029966483456")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Order with id '1749824029966483456' retrieved"))
                .andExpect(jsonPath("$.data.id").value("1749824029966483456"))
                .andExpect(jsonPath("$.data.total").value(this.order1.getTotal()));
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        // Given
        given(mockPrincipal.getName()).willReturn("me");
        given(this.orderService.getOrderById(eq("1749824029966483456"), Mockito.any(String.class)))
                .willThrow(new ObjectNotFoundException("order", "1749824029966483456"));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/1749824029966483456")
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Could not find order with Id 1749824029966483456"));
    }

    @Test
    void testCreateOrderSuccess() throws Exception {
        // Given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("John");
        addressDTO.setStreet("150 Street");
        addressDTO.setPostalCode("08-000");
        addressDTO.setCity("Wroclaw");
        addressDTO.setProvince("MLP");
        addressDTO.setCountry("Poland");

        String json = this.objectMapper.writeValueAsString(addressDTO);

        Address orderAddress = new Address();
        orderAddress.setId(2L);
        orderAddress.setName("John");
        orderAddress.setStreet("150 Street");
        orderAddress.setPostalCode("08-000");
        orderAddress.setCity("Wroclaw");
        orderAddress.setProvince("MLP");
        orderAddress.setCountry("Poland");

        order1.setShippingAddress(orderAddress);
        returnOrderDTO1.setShippingAddress(addressDTO);

        given(mockPrincipal.getName()).willReturn("me");
        given(this.orderService.createOrder(Mockito.any(AddressDTO.class), Mockito.any(String.class)))
                .willReturn(order1);
        given(this.orderToReturnOrderDTOConverter.convert(order1)).willReturn(returnOrderDTO1);

        // When and then
        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.message").value("New Order created"))
                .andExpect(jsonPath("$.data.id").value("1749824029966483456"))
                .andExpect(jsonPath("$.data.buyerEmail").value(order1.getBuyerEmail()))
                .andExpect(jsonPath("$.data.total").value(order1.getTotal()))
                .andExpect(jsonPath("$.data.shippingAddress.name").value(addressDTO.getName()));
    }

    @Test
    void testCreateOrderCartEmpty() throws Exception {
        // Given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("John");
        addressDTO.setStreet("150 Street");
        addressDTO.setPostalCode("08-000");
        addressDTO.setCity("Wroclaw");
        addressDTO.setProvince("MLP");
        addressDTO.setCountry("Poland");

        String json = this.objectMapper.writeValueAsString(addressDTO);

        given(mockPrincipal.getName()).willReturn("me");
        given(this.orderService.createOrder(Mockito.any(AddressDTO.class), Mockito.any(String.class)))
                .willThrow(new CartEmptyException("You cannot place the order. Your cart is empty"));

        // When and then
        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("You cannot place the order. Your cart is empty"));
    }

    @Test
    void testCreateOrderAddressInvalidInput() throws Exception {
        // Given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName("");
        addressDTO.setStreet("");
        addressDTO.setPostalCode("08-000");
        addressDTO.setCity("Wroclaw");
        addressDTO.setProvince("MLP");
        addressDTO.setCountry("Poland");

        String json = this.objectMapper.writeValueAsString(addressDTO);

        given(mockPrincipal.getName()).willReturn("me");

        // When and then
        this.mockMvc.perform(post(this.baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.name").value("Name cannot be blank"))
                .andExpect(jsonPath("$.data.street").value("Street cannot be blank"));
    }
}