package com.springbootangularshop.springbootbackend.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "/api/products";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Check getProducts default params (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetProductsSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Products retrieved"))
                .andExpect(jsonPath("$.data.page.content", Matchers.hasSize(10)))
                .andExpect(jsonPath("$.data.page.totalElements").value(18))
                .andExpect(jsonPath("$.data.page.sort.sorted").value(true));
    }

    @Test
    @DisplayName("Check getProducts with name, brand, type params (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetProductsWithNameBrandTypeParams() throws Exception {
        this.mockMvc.perform(get(this.baseUrl)
                        .param("name", "ang")
                        .param("brand", "b0c8bd8cf5284fcb8e0da0e2ea3c8e06")
                        .param("type", "aeea18dbff9142e0bb7645f9e683538d")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Products retrieved"))
                .andExpect(jsonPath("$.data.page.content", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.data.page.totalElements").value(1))
                .andExpect(jsonPath("$.data.page.sort.sorted").value(true));
    }

    @Test
    @DisplayName("Check getProducts with page params (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetProductsWithPageParams() throws Exception {
        this.mockMvc.perform(get(this.baseUrl)
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "unitPrice,desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Products retrieved"))
                .andExpect(jsonPath("$.data.page.content", Matchers.hasSize(5)))
                .andExpect(jsonPath("$.data.page.totalElements").value(18))
                .andExpect(jsonPath("$.data.page.sort.sorted").value(true))
                .andExpect(jsonPath("$.data.page.content[0].id").value("6426d0fedca24002bbcc259c7e955fba"))
                .andExpect(jsonPath("$.data.page.content[0].name").value("Net Core Super Board"))
                .andExpect(jsonPath("$.data.page.content[0].unitPrice").value(300.0));
    }

    @Test
    @DisplayName("Check getProductById (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetProductByIdSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/6126d0fedca26002bbcc259c7e955fba")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Product with id '6126d0fedca26002bbcc259c7e955fba' retrieved"))
                .andExpect(jsonPath("$.data.id").value("6126d0fedca26002bbcc259c7e955fba"))
                .andExpect(jsonPath("$.data.name").value("Angular Blue Boots"))
                .andExpect(jsonPath("$.data.unitPrice").value(180.0));
    }

    @Test
    @DisplayName("Check getProductById with non-existent id (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetProductByIdNotFound() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Could not find product with Id 1"));
    }

    @Test
    @DisplayName("Check getProductBrands (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetProductBrandsSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/product-brands")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Product Brands retrieved"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(6)));
    }

    @Test
    @DisplayName("Check getProductTypes (GET)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void testGetProductTypesSuccess() throws Exception {
        this.mockMvc.perform(get(this.baseUrl + "/product-types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Product Types retrieved"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }
}