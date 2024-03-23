package com.springbootangularshop.springbootbackend.cart.converter;

import com.springbootangularshop.springbootbackend.cart.Cart;
import com.springbootangularshop.springbootbackend.cart.dto.ReturnCart;
import com.springbootangularshop.springbootbackend.cart.dto.ReturnCartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartToReturnCartConverter implements Converter<Cart, ReturnCart> {

    private final CartItemToReturnCartItemConverter cartItemToReturnCartItemConverter;

    @Override
    public ReturnCart convert(Cart source) {

        List<ReturnCartItem> returnCartItems = source.getCartItems().stream()
                .map(this.cartItemToReturnCartItemConverter::convert)
                .toList();

        ReturnCart returnCart = new ReturnCart();
        returnCart.setTotal(source.getTotal());
        returnCart.setTotalItems(source.getTotalItems());
        returnCart.setCartItems(returnCartItems);

        return returnCart;
    }
}
