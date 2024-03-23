package com.springbootangularshop.springbootbackend.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Street cannot be blank")
    private String street;
    @NotBlank(message = "Postal Code cannot be blank")
    private String postalCode;
    @NotBlank(message = "City cannot be blank")
    private String city;
    @NotBlank(message = "Province cannot be blank")
    private String province;
    @NotBlank(message = "Country cannot be blank")
    private String country;
}
