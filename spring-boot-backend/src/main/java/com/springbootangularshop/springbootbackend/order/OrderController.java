package com.springbootangularshop.springbootbackend.order;

import com.springbootangularshop.springbootbackend.order.converter.OrderToReturnOrderDTOConverter;
import com.springbootangularshop.springbootbackend.order.converter.OrdersToReturnOrdersDTOConverter;
import com.springbootangularshop.springbootbackend.order.dto.ReturnOrderDTO;
import com.springbootangularshop.springbootbackend.system.HttpResponse;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;
    private final OrdersToReturnOrdersDTOConverter ordersToReturnOrdersDTOConverter;
    private final OrderToReturnOrderDTOConverter orderToReturnOrderDTOConverter;

    @GetMapping("")
    public ResponseEntity<HttpResponse> getOrders(Principal principal) {

        List<Order> orders = this.orderService.getOrders(principal.getName());

        List<ReturnOrderDTO> ordersDTO = this.ordersToReturnOrdersDTOConverter.convert(orders);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(ordersDTO)
                        .message("Orders retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<HttpResponse> getOrderById(@PathVariable String orderId, Principal principal) {

        Order order = this.orderService.getOrderById(orderId, principal.getName());

        ReturnOrderDTO returnOrderDTO = this.orderToReturnOrderDTOConverter.convert(order);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(returnOrderDTO)
                        .message("Order with id '" + orderId + "' retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<HttpResponse> createOrder(@Valid @RequestBody AddressDTO addressDTO, Principal principal) {
        Order order = this.orderService.createOrder(addressDTO, principal.getName());
        ReturnOrderDTO returnOrderDTO = this.orderToReturnOrderDTOConverter.convert(order);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id").buildAndExpand(order.getId()).toUri();

        return ResponseEntity.created(location).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(returnOrderDTO)
                        .message("New Order created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }
}
