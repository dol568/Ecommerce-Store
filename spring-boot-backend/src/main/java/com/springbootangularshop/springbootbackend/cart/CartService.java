package com.springbootangularshop.springbootbackend.cart;


import com.springbootangularshop.springbootbackend.cart.dto.CartItemDTO;

public interface CartService {

    Cart getCart(String username);
    CartItem addCartItem(CartItemDTO cartItemDTO, String username);
    void deleteCartItem(String productId, String username);
    void deleteAllCartItemsInCart(String username);
}
