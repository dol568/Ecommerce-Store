package com.springbootangularshop.springbootbackend.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.springbootangularshop.springbootbackend.cart.CartItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@JsonInclude(NON_DEFAULT)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 45, nullable = false)
    @NotBlank(message = "Username is required")
    @Length(min = 8, max = 45, message = "Username must have between 8-45 characters")
    private String username;
    @Column(length = 125, nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @Length(min = 5, max = 125, message = "Email must have between 5-125 characters")
    @Email(message = "Email needs to be a valid email")
    private String email;
    @Column(length = 64, nullable = false)
    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 64, message = "Password must have between 8-45 characters")
    private String password;

    private boolean isEnabled;
    private boolean isNonLocked;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Address> address = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<CartItem> cartItems = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addAddress(Address address) {
        this.address.add(address);
        address.setUser(this);
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setUser(this);
    }
}
