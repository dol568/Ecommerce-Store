package com.springbootangularshop.springbootbackend.user;

import com.springbootangularshop.springbootbackend.user.dto.LoginDTO;

public interface UserService {

    User saveUser(User newUser);

    User findByEmail(String email);

    User authenticate(LoginDTO request);

    Address getAddress(String email);

    Address updateAddress(Address address, String email);
}
