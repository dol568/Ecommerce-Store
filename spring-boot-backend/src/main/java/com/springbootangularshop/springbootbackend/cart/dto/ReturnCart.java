package com.springbootangularshop.springbootbackend.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnCart {
    private BigDecimal total = BigDecimal.ZERO;
    private Integer totalItems;
    private List<ReturnCartItem> cartItems = new ArrayList<>();
}
