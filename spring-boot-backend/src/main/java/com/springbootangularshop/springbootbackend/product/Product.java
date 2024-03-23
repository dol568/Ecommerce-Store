package com.springbootangularshop.springbootbackend.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.springbootangularshop.springbootbackend.cart.CartItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter @Setter
@JsonInclude(NON_DEFAULT)
public class Product {

    @Id
    private String id;
    @Column(unique = true)
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private String pictureUrl;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private ProductBrand brand;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", nullable = false)
    private ProductType type;


    @OneToOne(mappedBy = "product")
    @JsonIgnore
    private CartItem cartItem;
}