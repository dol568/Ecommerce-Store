package com.springbootangularshop.springbootbackend.cart;

import com.springbootangularshop.springbootbackend.cart.dto.CartItemDTO;
import com.springbootangularshop.springbootbackend.product.Product;
import com.springbootangularshop.springbootbackend.product.ProductRepository;
import com.springbootangularshop.springbootbackend.system.exception.ObjectNotFoundException;
import com.springbootangularshop.springbootbackend.user.User;
import com.springbootangularshop.springbootbackend.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(String username) {
        return this.cartRepository.findCartByCartItems_User_Email(username)
                .orElse(new Cart());
    }

    @Override
    public CartItem addCartItem(CartItemDTO cartItemDTO, String username) {

        Cart cart = this.cartRepository.findCartByCartItems_User_Email(username)
                .orElse(new Cart());

        User user = userRepository.findByEmail(username);
        if (user == null)
            throw new UsernameNotFoundException("User with email '" + username + "' not found");

        Product product = this.productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ObjectNotFoundException("product", cartItemDTO.getProductId()));

        CartItem item = getCartItemFromCart(username, cartItemDTO.getProductId());
        if (item == null) {
            item = new CartItem(cartItemDTO.getQuantity(), user, product);
            cart.getCartItems().add(item);
        } else {
            item.setQuantity(cartItemDTO.getQuantity());
        }

        item.setCart(cart);
        cart.setTotal(getTotalPrice(cart.getCartItems()));
        cart.setTotalItems(getTotalQuantity(cart.getCartItems()));
        cartRepository.save(cart);
        return item;
    }

    @Override
    public void deleteCartItem(String productId, String username) {
        Cart cart = getCart(username);
        CartItem item = getCartItemFromCart(username, productId);
        if (item == null) {
            throw new ObjectNotFoundException("cart item", "product", productId);
        }

        cart.getCartItems().remove(item);
        cart.setTotal(getTotalPrice(cart.getCartItems()));
        cart.setTotalItems(getTotalQuantity(cart.getCartItems()));
        this.cartRepository.save(cart);

        Cart foundCart = this.cartRepository.findCartByCartItemsIsEmpty();
        if (foundCart != null) {
            this.cartRepository.delete(foundCart);
        }
    }

    @Override
    public void deleteAllCartItemsInCart(String username) {
        Cart cart = getCart(username);
        cart.getCartItems().clear();
        this.cartRepository.delete(cart);
    }

    private BigDecimal getTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(cartItem -> cartItem.getProduct().getUnitPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int getTotalQuantity(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    private CartItem getCartItemFromCart(String username, String productId) {
        return this.cartItemRepository.findCartItemByUser_Email_AndProduct_Id(username, productId);
    }
}
