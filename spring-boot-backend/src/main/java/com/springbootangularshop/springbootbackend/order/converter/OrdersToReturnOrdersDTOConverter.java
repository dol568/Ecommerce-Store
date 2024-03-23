package com.springbootangularshop.springbootbackend.order.converter;

import com.springbootangularshop.springbootbackend.order.Order;
import com.springbootangularshop.springbootbackend.order.dto.ReturnOrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrdersToReturnOrdersDTOConverter implements Converter<List<Order>, List<ReturnOrderDTO>> {

    private final OrderToReturnOrderDTOConverter orderToReturnOrderDTOConverter;

    @Override
    public List<ReturnOrderDTO> convert(List<Order> source) {
        return source.stream()
                .map(this.orderToReturnOrderDTOConverter::convert)
                .toList();
    }
}
