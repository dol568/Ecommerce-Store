package com.springbootangularshop.springbootbackend.user;

import com.springbootangularshop.springbootbackend.system.exception.UsernameAlreadyExistsException;
import com.springbootangularshop.springbootbackend.user.dto.LoginDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AddressRepository addressRepository;

    public User saveUser(User newUser) {
        checkDuplicateEmail(newUser.getEmail());
        setUserDetails(newUser);
        encodePassword(newUser);
        return this.userRepository.save(newUser);
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("User with email '" + email + "' not found");
        return user;
    }

    public User authenticate(LoginDTO request) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return this.findByEmail(request.getUsername());
    }

    public Address getAddress(String email) {
        List<Address> addresses = this.addressRepository.findByUser_Email(email);

        return addresses.isEmpty()
                ? new Address()
                : addresses.get(addresses.size() - 1);
    }

    public Address updateAddress(Address address, String email) {
        User user = this.findByEmail(email);

        List<Address> existingAddresses = addressRepository.findByUser_Email(email);

        if (existingAddresses != null && !existingAddresses.isEmpty())
            for (Address existingAddress : existingAddresses)
                if (existingAddress.equals(address))
                    return this.addressRepository.save(existingAddress);

        user.addAddress(address);
        return this.addressRepository.save(address);
    }

    private void checkDuplicateEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null)
            throw new UsernameAlreadyExistsException("User with email '" + email + "' already exists");
    }

    private void encodePassword(User newUser) {
        String encodedPassword = this.passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
    }

    private void setUserDetails(User newUser) {
        newUser.setEnabled(true);
        newUser.setNonLocked(true);
    }
}