package com.springbootangularshop.springbootbackend.system.exception;

public class CartEmptyException extends RuntimeException {

    public CartEmptyException(String message) {
        super(message);
    }
}
