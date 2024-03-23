package com.springbootangularshop.springbootbackend.order;

import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;

import java.util.List;

public interface OrderService {

    Order createOrder(AddressDTO addressDTO, String username);

    List<Order> getOrders(String username);

    Order getOrderById(String orderId, String username);
}
