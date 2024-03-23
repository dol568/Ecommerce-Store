package com.springbootangularshop.springbootbackend.product;

import com.springbootangularshop.springbootbackend.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductBrandRepository productBrandRepository;
    private final ProductTypeRepository productTypeRepository;

    @Override
    public Page<Product> getProducts(String name, String brand, String type, int page, int size, String[] sort) {

        Sort.Direction direction = "asc".equals(sort[1]) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sorting = Sort.by(direction, sort[0]);
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), sorting);

        return this.productRepository
                .findByNameContainingAndBrandIdAndTypeId(name, brand, type, pageable);
    }

    @Override
    public Product findProductById(String productId) {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("product", productId));
    }

    @Override
    public List<ProductBrand> findAllProductBrands() {
        return this.productBrandRepository.findAll();
    }

    @Override
    public List<ProductType> findAllProductTypes() {
        return this.productTypeRepository.findAll();
    }
}
