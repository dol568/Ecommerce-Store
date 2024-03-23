package com.springbootangularshop.springbootbackend.product;

import com.springbootangularshop.springbootbackend.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductBrandRepository productBrandRepository;

    @Mock
    ProductTypeRepository productTypeRepository;

    @InjectMocks
    ProductServiceImpl productService;

    Product product1;
    Product product2;
    List<ProductBrand> productBrands;
    List<ProductType> productTypes;
    List<Product> products;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId("6126d0fedca24002bbcc259c7e955fba");
        product1.setDescription("Product description 1");
        product1.setName("Angular Speedster Board 2000");
        product1.setPictureUrl("");
        product1.setUnitPrice(BigDecimal.valueOf(200));

        product2 = new Product();
        product2.setId("6226d0fedca24002bbcc259c7e955fba");
        product2.setDescription("Product description 2");
        product2.setName("Green Angular Board 3000");
        product2.setPictureUrl("");
        product2.setUnitPrice(BigDecimal.valueOf(150));

        products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        ProductBrand brand1 = new ProductBrand();
        brand1.setId("b0c8bd8cf5284fcb8e0da0e2ea3c8e06");
        brand1.setName("Angular");
        brand1.addProduct(product1);

        ProductBrand brand2 = new ProductBrand();
        brand2.setId("6e26d0fedca24002bbcc259c7e955fba");
        brand2.setName("Spring Boot");
        brand2.addProduct(product2);

        ProductType type1 = new ProductType();
        type1.setId("56fa15ed7ee84f65a63ec6bf7602b68a");
        type1.setName("Boards");
        type1.addProduct(product1);

        ProductType type2 = new ProductType();
        type2.setId("aeea18dbff9142e0bb7645f9e683538d");
        type2.setName("Hats");
        type2.addProduct(product2);

        productBrands = new ArrayList<>();
        productBrands.add(brand1);
        productBrands.add(brand2);

        productTypes = new ArrayList<>();
        productTypes.add(type1);
        productTypes.add(type2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetProductsSuccess() {
        // Given
        String name = "Angular";
        String brand = "b0c8bd8cf5284fcb8e0da0e2ea3c8e06";
        String type = "56fa15ed7ee84f65a63ec6bf7602b68a";
        int page = 0;
        int size = 5;
        String[] sort = {"name", "asc"};

        Page<Product> productsPage = new PageImpl<>(this.products, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name")), this.products.size());

        given(this.productRepository.findByNameContainingAndBrandIdAndTypeId(any(String.class), any(String.class), any(String.class), any(PageRequest.class)))
                .willReturn(productsPage);

        // When
        Page<Product> actualProducts = this.productService.getProducts(name, brand, type, page, size, sort);

        // Then
        assertThat(actualProducts).isNotNull();
        assertThat(actualProducts).isEqualTo(productsPage);
        assertThat(actualProducts.getContent()).isEqualTo(this.products);
        assertThat(actualProducts.getPageable().isPaged()).isEqualTo(true);
        assertThat(actualProducts.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(actualProducts.getPageable().getPageSize()).isEqualTo(5);
        assertThat(actualProducts.getTotalElements()).isEqualTo(2);
        assertThat(actualProducts.getSort().getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void testFindProductByIdSuccess() {
        // Given
        given(this.productRepository.findById("6126d0fedca24002bbcc259c7e955fba")).willReturn(Optional.of(product1));

        // When
        Product foundProduct = this.productService.findProductById("6126d0fedca24002bbcc259c7e955fba");

        // Then
        assertThat(foundProduct.getId()).isEqualTo(product1.getId());
        assertThat(foundProduct.getDescription()).isEqualTo(product1.getDescription());
        assertThat(foundProduct.getName()).isEqualTo(product1.getName());
        assertThat(foundProduct.getPictureUrl()).isEqualTo(product1.getPictureUrl());
        assertThat(foundProduct.getUnitPrice()).isEqualTo(product1.getUnitPrice());
        verify(this.productRepository, times(1)).findById("6126d0fedca24002bbcc259c7e955fba");
    }

    @Test
    void testFindProductByIdNotFound() {
        // Given
        given(this.productRepository.findById("6126d0fedca24002bbcc259c7e955fba")).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> this.productService.findProductById("6126d0fedca24002bbcc259c7e955fba"));

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find product with Id 6126d0fedca24002bbcc259c7e955fba");
        verify(this.productRepository, times(1)).findById("6126d0fedca24002bbcc259c7e955fba");
    }

    @Test
    void testFindAllProductBrandsSuccess() {
        // Given
        given(this.productBrandRepository.findAll()).willReturn(this.productBrands);

        // When
        List<ProductBrand> actualProductBrands = this.productService.findAllProductBrands();

        //Then
        assertThat(actualProductBrands.size()).isEqualTo(this.productBrands.size());
        verify(this.productBrandRepository, times(1)).findAll();
    }

    @Test
    void testFindAllProductTypesSuccess() {
        // Given
        given(this.productTypeRepository.findAll()).willReturn(this.productTypes);

        // When
        List<ProductType> actualProductTypes = this.productService.findAllProductTypes();

        //Then
        assertThat(actualProductTypes.size()).isEqualTo(this.productTypes.size());
        verify(this.productTypeRepository, times(1)).findAll();
    }
}