package com.springbootangularshop.springbootbackend.user;

import com.springbootangularshop.springbootbackend.system.HttpResponse;
import com.springbootangularshop.springbootbackend.user.converter.RegisterDTOToUserConverter;
import com.springbootangularshop.springbootbackend.user.converter.UserToReturnUserDTOConverter;
import com.springbootangularshop.springbootbackend.user.dto.LoginDTO;
import com.springbootangularshop.springbootbackend.user.dto.RegisterDTO;
import com.springbootangularshop.springbootbackend.user.dto.ReturnUserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RegisterDTOToUserConverter registerDTOToUserConverter;
    private final UserToReturnUserDTOConverter userToReturnUserDTOConverter;

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {

        User user = this.userService.authenticate(loginDTO);

        ReturnUserDTO returnUserDTO = this.userToReturnUserDTOConverter.convert(user);

        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(returnUserDTO)
                        .message("Successful Jwt Login")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> registerUser(@Valid @RequestBody RegisterDTO registerDTO) {

        User user = this.registerDTOToUserConverter.convert(registerDTO);

        User savedUser = this.userService.saveUser(user);

        ReturnUserDTO returnUserDTO = this.userToReturnUserDTOConverter.convert(savedUser);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(uri).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(returnUserDTO)
                        .message("New User Created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }
}
