package com.springbootangularshop.springbootbackend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnUserDTO {
    private String username;
    private String email;
    private String token;
}
