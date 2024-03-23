package com.springbootangularshop.springbootbackend.user.dto;

import com.springbootangularshop.springbootbackend.user.utils.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "Username is required")
    @Length(min = 8, max = 45, message = "Username must have between 8-45 characters")
    private String username;
    @NotBlank(message = "Email is required")
    @Length(min = 5, max = 125, message = "Email must have between 5-125 characters")
    @Email(message = "Email needs to be a valid email")
    private String email;
    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;
}
