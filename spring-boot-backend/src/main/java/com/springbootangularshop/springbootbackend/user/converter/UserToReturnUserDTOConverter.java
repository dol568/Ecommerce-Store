package com.springbootangularshop.springbootbackend.user.converter;

import com.springbootangularshop.springbootbackend.security.TokenProvider;
import com.springbootangularshop.springbootbackend.user.User;
import com.springbootangularshop.springbootbackend.user.UserPrincipal;
import com.springbootangularshop.springbootbackend.user.dto.ReturnUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserToReturnUserDTOConverter implements Converter<User, ReturnUserDTO> {

    private final TokenProvider tokenProvider;

    @Override
    public ReturnUserDTO convert(User user) {

        String jwt = this.tokenProvider.generateToken(new UserPrincipal(user));

        ReturnUserDTO returnUserDTO = new ReturnUserDTO();
        returnUserDTO.setUsername(user.getUsername());
        returnUserDTO.setEmail(user.getEmail());
        returnUserDTO.setToken(jwt);

        return returnUserDTO;
    }
}
