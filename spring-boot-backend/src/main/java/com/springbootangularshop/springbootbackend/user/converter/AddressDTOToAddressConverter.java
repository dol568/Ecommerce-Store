package com.springbootangularshop.springbootbackend.user.converter;

import com.springbootangularshop.springbootbackend.user.Address;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressDTOToAddressConverter implements Converter<AddressDTO, Address> {

    @Override
    public Address convert(AddressDTO source) {
        Address address = new Address();
        address.setName(source.getName());
        address.setStreet(source.getStreet());
        address.setCity(source.getCity());
        address.setProvince(source.getProvince());
        address.setPostalCode(source.getPostalCode());
        address.setCountry(source.getCountry());
        return address;
    }
}
