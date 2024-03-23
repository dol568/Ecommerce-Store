package com.springbootangularshop.springbootbackend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootangularshop.springbootbackend.system.exception.CustomResponseEntityExceptionHandler;
import com.springbootangularshop.springbootbackend.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = ProductController.class)
@Import(CustomResponseEntityExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "/api/products";

    Product product1;
    Product product2;
    Product product3;
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

        product3 = new Product();
        product3.setId("6326d0fedca24002bbcc259c7e955fba");
        product3.setDescription("Product description 3");
        product3.setName("Core Board Speed Rush 3");
        product3.setPictureUrl("");
        product3.setUnitPrice(BigDecimal.valueOf(180));

        products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);

        ProductBrand brand1 = new ProductBrand();
        brand1.setId("b0c8bd8cf5284fcb8e0da0e2ea3c8e06");
        brand1.setName("Angular");
        brand1.addProduct(product1);
        brand1.addProduct(product3);

        ProductBrand brand2 = new ProductBrand();
        brand2.setId("6e26d0fedca24002bbcc259c7e955fba");
        brand2.setName("Spring Boot");
        brand2.addProduct(product2);

        ProductType type1 = new ProductType();
        type1.setId("56fa15ed7ee84f65a63ec6bf7602b68a");
        type1.setName("Boards");
        type1.addProduct(product1);
        type1.addProduct(product3);

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
    void testGetProductsSuccess() throws Exception {
        // Given
        String name = null;
        String brand = null;
        String type = null;
        int page = 0;
        int size = 2;
        String[] sort = {"name", "asc"};

        Page<Product> productsPage = new PageImpl<>(List.of(product1, product2));

        given(this.productService.getProducts(name, brand, type, page, size, sort)).willReturn(productsPage);

        // When and then
        this.mockMvc.perform(get(this.baseUrl)
                        .param("name", name)
                        .param("brand", brand)
                        .param("type", type)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(2))
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Products retrieved"))
                .andExpect(jsonPath("$.data.page.content", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data.page.content[0].id").value("6126d0fedca24002bbcc259c7e955fba"))
                .andExpect(jsonPath("$.data.page.content[0].name").value("Angular Speedster Board 2000"))
                .andExpect(jsonPath("$.data.page.content[1].id").value("6226d0fedca24002bbcc259c7e955fba"))
                .andExpect(jsonPath("$.data.page.content[1].name").value("Green Angular Board 3000"));
    }

    @Test
    void testGetProductByIdSuccess() throws Exception {
        // Given
        given(this.productService.findProductById("6126d0fedca24002bbcc259c7e955fba")).willReturn(this.product1);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/6126d0fedca24002bbcc259c7e955fba")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Product with id '6126d0fedca24002bbcc259c7e955fba' retrieved"))
                .andExpect(jsonPath("$.data.id").value("6126d0fedca24002bbcc259c7e955fba"))
                .andExpect(jsonPath("$.data.name").value("Angular Speedster Board 2000"))
                .andExpect(jsonPath("$.data.unitPrice").value(BigDecimal.valueOf(200)));
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        // Given
        given(this.productService.findProductById("6126d0fedca24002bbcc259c7e955fba"))
                .willThrow(new ObjectNotFoundException("product", "6126d0fedca24002bbcc259c7e955fba"));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/6126d0fedca24002bbcc259c7e955fba")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Could not find product with Id 6126d0fedca24002bbcc259c7e955fba"));
    }

    @Test
    void testGetProductBrandsSuccess() throws Exception {
        // Given
        given(this.productService.findAllProductBrands()).willReturn(this.productBrands);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/product-brands")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Product Brands retrieved"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.productTypes.size())))
                .andExpect(jsonPath("$.data[0].id").value("b0c8bd8cf5284fcb8e0da0e2ea3c8e06"))
                .andExpect(jsonPath("$.data[0].name").value("Angular"))
                .andExpect(jsonPath("$.data[1].id").value("6e26d0fedca24002bbcc259c7e955fba"))
                .andExpect(jsonPath("$.data[1].name").value("Spring Boot"));
    }

    @Test
    void testGetProductTypesSuccess() throws Exception {
        // Given
        given(this.productService.findAllProductTypes()).willReturn(this.productTypes);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/product-types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Product Types retrieved"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.productTypes.size())))
                .andExpect(jsonPath("$.data[0].id").value("56fa15ed7ee84f65a63ec6bf7602b68a"))
                .andExpect(jsonPath("$.data[0].name").value("Boards"))
                .andExpect(jsonPath("$.data[1].id").value("aeea18dbff9142e0bb7645f9e683538d"))
                .andExpect(jsonPath("$.data[1].name").value("Hats"));
    }
}