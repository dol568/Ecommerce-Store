package com.springbootangularshop.springbootbackend.user;

import com.springbootangularshop.springbootbackend.system.exception.UsernameAlreadyExistsException;
import com.springbootangularshop.springbootbackend.user.dto.LoginDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AddressRepository addressRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    User user;
    Address address1;
    Address address2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("joshhomme568");
        user.setEmail("joshhomme@gmail.com");
        user.setPassword("!Jjoshhomme568");
        user.setNonLocked(true);
        user.setEnabled(true);

        address1 = new Address();
        address1.setId(1L);
        address1.setName("Josh");
        address1.setStreet("271 Street");
        address1.setCity("Warsaw");
        address1.setProvince("MZ");
        address1.setPostalCode("10-150");
        address1.setCountry("Poland");

        address2 = new Address();
        address2.setId(2L);
        address2.setName("Josh");
        address2.setStreet("275 Street");
        address2.setCity("Suwalki");
        address2.setProvince("PB");
        address2.setPostalCode("16-400");
        address2.setCountry("Poland");

        Role roleUser = new Role();
        roleUser.setId(1L);
        roleUser.setName("USER");

        user.addRole(roleUser);
        user.addAddress(address1);
        user.addAddress(address2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testSaveUserSuccess() {
        // Given
        User newUser = new User();
        newUser.setUsername("thomyorke568");
        newUser.setEmail("thomyorke@gmail.com");
        newUser.setPassword("!Tthomyorke568");

        given(this.userRepository.findByEmail("thomyorke@gmail.com")).willReturn(null);
        given(this.passwordEncoder.encode(newUser.getPassword())).willReturn("Encoded password");
        given(this.userRepository.save(newUser)).willReturn(newUser);

        // When
        User savedUser = this.userService.saveUser(newUser);

        //Then
        assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(savedUser.isEnabled()).isEqualTo(true);
        assertThat(savedUser.isNonLocked()).isEqualTo(true);
        verify(this.userRepository, times(1)).findByEmail(newUser.getEmail());
        verify(this.passwordEncoder, times(1)).encode("!Tthomyorke568");
        verify(this.userRepository, times(1)).save(newUser);
    }

    @Test
    void testSaveUserAlreadyExists() {
        // Given
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);

        // When
        Throwable thrown = catchThrowable(() -> this.userService.saveUser(user));

        // Then
        assertThat(thrown)
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessage("User with email 'joshhomme@gmail.com' already exists");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
    }

    @Test
    void testFindByEmailSuccess() {
        // Given
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);

        // When
        User foundUser = this.userService.findByEmail("joshhomme@gmail.com");

        // Then
        assertThat(foundUser.getId()).isEqualTo(user.getId());
        assertThat(foundUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(foundUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(foundUser.isNonLocked()).isEqualTo(true);
        assertThat(foundUser.isEnabled()).isEqualTo(true);
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
    }

    @Test
    void testFindByEmailNotFound() {
        // Given
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(null);

        // When
        Throwable thrown = catchThrowable(() -> this.userService.findByEmail("joshhomme@gmail.com"));

        // Then
        assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with email 'joshhomme@gmail.com' not found");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
    }

    @Test
    void testAuthenticateSuccess() {
        // Given
        LoginDTO loginDTO = new LoginDTO("joshhomme@gmail.com", "!Jjoshhomme568");
        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);

        // When
        User authUser = this.userService.authenticate(loginDTO);

        // Then
        assertThat(authUser.getId()).isEqualTo(user.getId());
        assertThat(authUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(authUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(authUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(authUser.isNonLocked()).isEqualTo(true);
        assertThat(authUser.isEnabled()).isEqualTo(true);
        assertThat(authUser.getAddress().size()).isEqualTo(2);
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken("joshhomme@gmail.com", "!Jjoshhomme568"));
    }

    @Test
    void testAuthenticateUserNotFound() {
        // Given
        LoginDTO loginDTO = new LoginDTO("joshhomme@gmail.com", "!Jjoshhomme568");
        given(this.userRepository.findByEmail("joshhomme@gmail.com"))
                .willThrow(new UsernameNotFoundException("User with email 'joshhomme@gmail.com' not found"));

        // When
        Throwable thrown = catchThrowable(() -> this.userService.authenticate(loginDTO));

        // Then
        assertThat(thrown)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with email 'joshhomme@gmail.com' not found");
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
    }

    @Test
    void testGetAddressSuccess() {
        // Given
        given(this.addressRepository.findByUser_Email("joshhomme@gmail.com"))
                .willReturn(user.getAddress());

        // When
        Address foundAddress = this.userService.getAddress("joshhomme@gmail.com");

        // Then
        assertThat(foundAddress.getId()).isEqualTo(address2.getId());
        assertThat(foundAddress.getName()).isEqualTo(address2.getName());
        assertThat(foundAddress.getStreet()).isEqualTo(address2.getStreet());
        assertThat(foundAddress.getCity()).isEqualTo(address2.getCity());
        assertThat(foundAddress.getProvince()).isEqualTo(address2.getProvince());
        assertThat(foundAddress.getPostalCode()).isEqualTo(address2.getPostalCode());
        assertThat(foundAddress.getCountry()).isEqualTo(address2.getCountry());
        verify(this.addressRepository, times(1)).findByUser_Email("joshhomme@gmail.com");
    }

    @Test
    void testGetAddressListEmpty() {
        // Given
        given(this.addressRepository.findByUser_Email("joshhomme@gmail.com"))
                .willReturn(new ArrayList<>());

        // When
        Address foundAddress = this.userService.getAddress("joshhomme@gmail.com");

        // Then
        assertThat(foundAddress.getId()).isEqualTo(null);
        assertThat(foundAddress.getName()).isEqualTo(null);
        assertThat(foundAddress.getStreet()).isEqualTo(null);
        assertThat(foundAddress.getCity()).isEqualTo(null);
        assertThat(foundAddress.getProvince()).isEqualTo(null);
        assertThat(foundAddress.getPostalCode()).isEqualTo(null);
        assertThat(foundAddress.getCountry()).isEqualTo(null);
        verify(this.addressRepository, times(1)).findByUser_Email("joshhomme@gmail.com");
    }

    @Test
    void testUpdateAddressNewAddress() {
        // Given
        Address newAddress = new Address();
        newAddress.setName("updated name");
        newAddress.setStreet("275 Street");
        newAddress.setCity("updated city");
        newAddress.setProvince("PB");
        newAddress.setPostalCode("16-400");
        newAddress.setCountry("updated country");

        Address savedAddress = new Address();
        savedAddress.setId(3L);
        savedAddress.setName("updated name");
        savedAddress.setStreet("275 Street");
        savedAddress.setCity("updated city");
        savedAddress.setProvince("PB");
        savedAddress.setPostalCode("16-400");
        savedAddress.setCountry("updated country");

        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);

        given(this.addressRepository.findByUser_Email("joshhomme@gmail.com"))
                .willReturn(user.getAddress());

        given(this.addressRepository.save(Mockito.any(Address.class))).willReturn(savedAddress);

        // When
        Address returnAddress = this.userService.updateAddress(newAddress, "joshhomme@gmail.com");

        //Then
        assertThat(user.getAddress().size()).isEqualTo(3);
        assertThat(user.getAddress()).contains(returnAddress);
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.addressRepository, times(1)).findByUser_Email("joshhomme@gmail.com");
        verify(this.addressRepository, times(1)).save(newAddress);
    }

    @Test
    void testUpdateAddressExistingAddress() {
        // Given
        Address newAddress = new Address();
        newAddress.setName("Josh");
        newAddress.setStreet("271 Street");
        newAddress.setCity("Warsaw");
        newAddress.setProvince("MZ");
        newAddress.setPostalCode("10-150");
        newAddress.setCountry("Poland");

        given(this.userRepository.findByEmail("joshhomme@gmail.com")).willReturn(user);

        given(this.addressRepository.findByUser_Email("joshhomme@gmail.com"))
                .willReturn(user.getAddress());

        given(this.addressRepository.save(Mockito.any(Address.class))).willReturn(address1);

        // When
        Address savedAddress = this.userService.updateAddress(newAddress, "joshhomme@gmail.com");

        // Then
        assertThat(user.getAddress().size()).isEqualTo(2);
        assertThat(user.getAddress()).contains(savedAddress);
        verify(this.userRepository, times(1)).findByEmail("joshhomme@gmail.com");
        verify(this.addressRepository, times(1)).findByUser_Email("joshhomme@gmail.com");
        verify(this.addressRepository, times(1)).save(newAddress);
    }
}