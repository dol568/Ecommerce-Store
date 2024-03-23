package com.springbootangularshop.springbootbackend.cart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class CartItemDTO {

    @NotBlank(message = "ProductId cannot be blank")
    private String productId;
    @NotNull(message = "Quantity cannot be blank")
    private Integer quantity;
}
