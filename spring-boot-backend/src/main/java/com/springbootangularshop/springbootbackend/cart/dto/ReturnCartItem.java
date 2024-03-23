package com.springbootangularshop.springbootbackend.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnCartItem {
    private Long id;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String pictureUrl;
}
