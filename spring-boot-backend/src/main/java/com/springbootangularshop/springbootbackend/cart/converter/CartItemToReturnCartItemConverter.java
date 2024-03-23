package com.springbootangularshop.springbootbackend.cart.converter;

import com.springbootangularshop.springbootbackend.cart.CartItem;
import com.springbootangularshop.springbootbackend.cart.dto.ReturnCartItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CartItemToReturnCartItemConverter implements Converter<CartItem, ReturnCartItem> {

    @Override
    public ReturnCartItem convert(CartItem source) {
        ReturnCartItem returnCartItem = new ReturnCartItem();
        returnCartItem.setId(source.getId());
        returnCartItem.setProductId(source.getProduct().getId());
        returnCartItem.setProductName(source.getProduct().getName());
        returnCartItem.setQuantity(source.getQuantity());
        returnCartItem.setUnitPrice(source.getProduct().getUnitPrice());
        returnCartItem.setPictureUrl(source.getProduct().getPictureUrl());
        return returnCartItem;
    }
}
