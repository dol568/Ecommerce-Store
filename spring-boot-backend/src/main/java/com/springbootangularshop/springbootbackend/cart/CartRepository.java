package com.springbootangularshop.springbootbackend.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findCartByCartItems_User_Email(String email);
    Cart findCartByCartItemsIsEmpty();
}
