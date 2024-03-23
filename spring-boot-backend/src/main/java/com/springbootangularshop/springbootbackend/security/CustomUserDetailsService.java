package com.springbootangularshop.springbootbackend.security;

import com.springbootangularshop.springbootbackend.user.User;
import com.springbootangularshop.springbootbackend.user.UserPrincipal;
import com.springbootangularshop.springbootbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("User with email '" + email + "' not found");

        return new UserPrincipal(user);
    }
}
