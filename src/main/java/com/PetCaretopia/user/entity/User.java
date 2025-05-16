package com.PetCaretopia.user.entity;

import com.PetCaretopia.order.entity.Cart;
import com.PetCaretopia.order.entity.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Builder
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @NotBlank(message = "Username is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String userEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role userRole;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String userAddress;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    @Column(unique = true)
    private String userPhoneNumber;

    private String userProfileImage;

    @Enumerated(EnumType.STRING)
    private Gender userGender;

    @Column(length = 1000)
    private String userDetails; // Additional info

    private LocalDate userCreationalDate = LocalDate.now();
    private LocalDateTime userLastLoginDate;

    private String billingAddress;
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private Status userStatus = Status.ACTIVE;

    // ✅ One-to-One Relationships with role-specific entities
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PetOwner petOwner;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ServiceProvider serviceProvider;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ShelterAccount shelterAccount;

    // ✅ Existing relations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Order> orders;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Cart cart;

    // ✅ Enum Definitions
    public enum Role {
        ADMIN, SERVICE_PROVIDER, PET_OWNER, USER, SHELTER
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum Status {
        ACTIVE, INACTIVE, BANNED
    }

    // ✅ Spring Security Implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
