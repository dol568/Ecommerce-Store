package com.springbootangularshop.springbootbackend.order;

import com.springbootangularshop.springbootbackend.cart.Cart;
import com.springbootangularshop.springbootbackend.cart.CartItem;
import com.springbootangularshop.springbootbackend.cart.CartServiceImpl;
import com.springbootangularshop.springbootbackend.system.exception.CartEmptyException;
import com.springbootangularshop.springbootbackend.system.exception.ObjectNotFoundException;
import com.springbootangularshop.springbootbackend.user.Address;
import com.springbootangularshop.springbootbackend.user.User;
import com.springbootangularshop.springbootbackend.user.UserRepository;
import com.springbootangularshop.springbootbackend.user.UserService;
import com.springbootangularshop.springbootbackend.user.converter.AddressDTOToAddressConverter;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final AddressDTOToAddressConverter addressDTOToAddressConverter;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartServiceImpl cartService;
    private final UserService userService;

    @Override
    public Order createOrder(AddressDTO addressDTO, String username) {
        User user = userRepository.findByEmail(username);
        if (user == null)
            throw new UsernameNotFoundException("User with email '" + username + "' not found");

        Cart cart = this.cartService.getCart(username);
        if (cart.getCartItems().isEmpty())
            throw new CartEmptyException("You cannot place the order. Your cart is empty");

        Address address = this.addressDTOToAddressConverter.convert(addressDTO);
        Address savedAddress = this.userService.updateAddress(address, username);

        Order order = getOrder(cart, savedAddress, user);
        return this.orderRepository.save(order);
    }

    private Order getOrder(Cart cart, Address address, User user) {
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductItemId(cartItem.getProduct().getId());
            orderItem.setProductName(cartItem.getProduct().getName());
            orderItem.setPictureUrl(cartItem.getProduct().getPictureUrl());
            orderItem.setUnitPrice(cartItem.getProduct().getUnitPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            orderItems.add(orderItem);
            orderItem.setOrder(order);
        }
        order.setOrderItems(orderItems);
        order.setShippingAddress(address);
        order.setBuyerEmail(user.getEmail());
        order.setTotal(cart.getTotal());
        return order;
    }

    @Override
    public List<Order> getOrders(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null)
            throw new UsernameNotFoundException("User with email '" + username + "' not found");

        return this.orderRepository.findByBuyerEmail(user.getEmail());
    }

    @Override
    public Order getOrderById(String orderId, String username) {
        return getOrders(username)
                .stream()
                .filter(order -> order.getId().equals(orderId))
                .findAny()
                .orElseThrow(() -> new ObjectNotFoundException("order", orderId));
    }
}
