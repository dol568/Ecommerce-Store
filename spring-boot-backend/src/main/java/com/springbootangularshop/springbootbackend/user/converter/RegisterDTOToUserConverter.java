package com.springbootangularshop.springbootbackend.user.converter;

import com.springbootangularshop.springbootbackend.user.User;
import com.springbootangularshop.springbootbackend.user.dto.RegisterDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RegisterDTOToUserConverter implements Converter<RegisterDTO, User> {

    @Override
    public User convert(RegisterDTO source) {

        User user = new User();
        user.setUsername(source.getUsername());
        user.setEmail(source.getEmail());
        user.setPassword(source.getPassword());

        return user;
    }
}