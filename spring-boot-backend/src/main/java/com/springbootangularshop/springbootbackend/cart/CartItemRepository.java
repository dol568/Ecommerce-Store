package com.springbootangularshop.springbootbackend.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findCartItemByUser_Email_AndProduct_Id(String userId, String productId);
}
