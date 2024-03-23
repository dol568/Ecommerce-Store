package com.springbootangularshop.springbootbackend.order.converter;

import com.springbootangularshop.springbootbackend.order.Order;
import com.springbootangularshop.springbootbackend.order.dto.ReturnOrderDTO;
import com.springbootangularshop.springbootbackend.user.converter.AddressToAddressDTOConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderToReturnOrderDTOConverter implements Converter<Order, ReturnOrderDTO> {

    private final AddressToAddressDTOConverter addressToAddressDTOConverter;

    public OrderToReturnOrderDTOConverter(AddressToAddressDTOConverter addressToAddressDTOConverter) {
        this.addressToAddressDTOConverter = addressToAddressDTOConverter;
    }

    @Override
    public ReturnOrderDTO convert(Order source) {
        ReturnOrderDTO returnOrderDTO = new ReturnOrderDTO();
        returnOrderDTO.setId(source.getId());
        returnOrderDTO.setOrderStatus(source.getOrderStatus());
        returnOrderDTO.setBuyerEmail(source.getBuyerEmail());
        returnOrderDTO.setTotal(source.getTotal());
        returnOrderDTO.setOrderDate(source.getOrderDate());
        returnOrderDTO.setOrderItems(source.getOrderItems());
        returnOrderDTO.setShippingAddress(source.getShippingAddress() != null
                ? this.addressToAddressDTOConverter.convert(source.getShippingAddress())
                : null);
        return returnOrderDTO;
    }
}
