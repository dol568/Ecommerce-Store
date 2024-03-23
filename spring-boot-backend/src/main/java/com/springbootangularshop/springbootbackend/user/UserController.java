package com.springbootangularshop.springbootbackend.user;

import com.springbootangularshop.springbootbackend.system.HttpResponse;
import com.springbootangularshop.springbootbackend.user.converter.AddressToAddressDTOConverter;
import com.springbootangularshop.springbootbackend.user.converter.UserToReturnUserDTOConverter;
import com.springbootangularshop.springbootbackend.user.dto.AddressDTO;
import com.springbootangularshop.springbootbackend.user.dto.ReturnUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressToAddressDTOConverter addressToAddressDTOConverter;
    private final UserToReturnUserDTOConverter userToReturnUserDTOConverter;

    @GetMapping("")
    public ResponseEntity<HttpResponse> getCurrentUser(Principal principal) {

        User user = this.userService.findByEmail(principal.getName());

        ReturnUserDTO returnUserDTO = this.userToReturnUserDTOConverter.convert(user);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(returnUserDTO)
                        .message("User retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @GetMapping("/address")
    public ResponseEntity<HttpResponse> getAddress(Principal principal) {

        Address address = this.userService.getAddress(principal.getName());

        AddressDTO addressDTO = this.addressToAddressDTOConverter.convert(address);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(addressDTO)
                        .message("Address retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
}
