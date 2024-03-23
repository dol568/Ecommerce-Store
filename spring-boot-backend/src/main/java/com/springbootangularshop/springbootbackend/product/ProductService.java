package com.springbootangularshop.springbootbackend.product;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Page<Product> getProducts(String name, String brand, String type, int page, int size, String[] sort);
    Product findProductById(String productId);
    List<ProductBrand> findAllProductBrands();
    List<ProductType> findAllProductTypes();
}
