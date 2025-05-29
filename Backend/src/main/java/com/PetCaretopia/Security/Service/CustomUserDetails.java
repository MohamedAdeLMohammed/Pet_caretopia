package com.PetCaretopia.Security.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final String role;

    public static CustomUserDetails fromUser(com.PetCaretopia.user.entity.User user) {
        return new CustomUserDetails(
                user.getUserID(),
                user.getUserEmail(),
                user.getUserPassword(),
                user.getUserRole().name()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + role); // SimpleAuthority
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
