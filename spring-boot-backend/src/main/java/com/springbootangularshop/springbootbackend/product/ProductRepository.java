package com.springbootangularshop.springbootbackend.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p FROM Product p WHERE " +
            "(:name is null or p.name LIKE %:name%) " +
            "and (:brand is null or p.brand.id = :brand) " +
            "and (:type is null or p.type.id = :type)")
    Page<Product> findByNameContainingAndBrandIdAndTypeId(String name, String brand, String type, Pageable pageable);
}
