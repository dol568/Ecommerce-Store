package com.springbootangularshop.springbootbackend.user.converter;

import com.springbootangularshop.springbootbackend.user.Address;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AddressToAddressDTOConverter implements Converter<Address, AddressDTO> {

    @Override
    public AddressDTO convert(Address source) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setName(source.getName() != null ? source.getName() : "");
        addressDTO.setStreet(source.getStreet() != null ? source.getStreet() : "");
        addressDTO.setCity(source.getCity() != null ? source.getCity() : "");
        addressDTO.setProvince(source.getProvince() != null ? source.getProvince() : "");
        addressDTO.setPostalCode(source.getPostalCode() != null ? source.getPostalCode() : "");
        addressDTO.setCountry(source.getCountry() != null ? source.getCountry() : "");
        return addressDTO;
    }
}
