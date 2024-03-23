package com.springbootangularshop.springbootbackend.product;

import com.springbootangularshop.springbootbackend.system.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Map.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<HttpResponse> getProducts(@RequestParam Optional<String> name,
                                                    @RequestParam Optional<String> brand,
                                                    @RequestParam Optional<String> type,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "name,asc") String[] sort) {

        Page<Product> products = this.productService.getProducts(name.orElse(null), brand.orElse(null),
                type.orElse(null), page, size, sort);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(of("page", products))
                        .message("Products retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<HttpResponse> getProductById(@PathVariable String productId) {

        Product product = this.productService.findProductById(productId);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(product)
                        .message("Product with id '" + productId + "' retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/product-brands")
    public ResponseEntity<HttpResponse> getProductBrands() {

        List<ProductBrand> productBrands = this.productService.findAllProductBrands();

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(productBrands)
                        .message("Product Brands retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/product-types")
    public ResponseEntity<HttpResponse> getProductTypes() {

        List<ProductType> productTypes = this.productService.findAllProductTypes();

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(productTypes)
                        .message("Product Types retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
