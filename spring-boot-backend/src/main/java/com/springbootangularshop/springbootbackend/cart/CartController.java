package com.springbootangularshop.springbootbackend.cart;

import com.springbootangularshop.springbootbackend.cart.converter.CartToReturnCartConverter;
import com.springbootangularshop.springbootbackend.cart.dto.CartItemDTO;
import com.springbootangularshop.springbootbackend.cart.dto.ReturnCart;
import com.springbootangularshop.springbootbackend.system.HttpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;
    private final CartToReturnCartConverter cartToReturnCartConverter;

    @GetMapping("")
    public ResponseEntity<HttpResponse> getCart(Principal principal) {
        Cart cart = this.cartService.getCart(principal.getName());

        ReturnCart returnCart = this.cartToReturnCartConverter.convert(cart);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(returnCart)
                        .message("Cart retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<HttpResponse> addCartItem(@Valid @RequestBody CartItemDTO cartItemdto, Principal principal) {

        CartItem cartItem = cartService.addCartItem(cartItemdto, principal.getName());

        Cart cart = this.cartService.getCart(principal.getName());

        ReturnCart returnCart = this.cartToReturnCartConverter.convert(cart);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cartItem.getId()).toUri();

        return ResponseEntity.created(location).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(returnCart)
                        .message("Cart Item added")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<HttpResponse> deleteCartItem(@PathVariable String productId, Principal principal) {
        this.cartService.deleteCartItem(productId, principal.getName());

        Cart cart = this.cartService.getCart(principal.getName());

        ReturnCart returnCart = this.cartToReturnCartConverter.convert(cart);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(returnCart)
                        .message("Cart Item with Product ID '" + productId + "' deleted")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @DeleteMapping("")
    public ResponseEntity<HttpResponse> clearCart(Principal principal) {
        this.cartService.deleteAllCartItemsInCart(principal.getName());

        Cart cart = this.cartService.getCart(principal.getName());

        ReturnCart returnCart = this.cartToReturnCartConverter.convert(cart);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(returnCart)
                        .message("Cart has been cleared out")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
