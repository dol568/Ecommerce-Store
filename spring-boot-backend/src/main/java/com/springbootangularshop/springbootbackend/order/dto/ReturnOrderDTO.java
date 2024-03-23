package com.springbootangularshop.springbootbackend.order.dto;

import com.springbootangularshop.springbootbackend.order.OrderItem;
import com.springbootangularshop.springbootbackend.order.OrderStatus;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderDTO {
    private String id;
    private Date orderDate;
    private OrderStatus orderStatus;
    private String buyerEmail;
    private BigDecimal total;
    private List<OrderItem> orderItems = new ArrayList<>();
    private AddressDTO shippingAddress;
}
