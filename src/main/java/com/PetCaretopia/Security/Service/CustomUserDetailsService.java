package com.PetCaretopia.Security.Service;



import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;  // Assumed UserRepository extends JpaRepository or CrudRepository
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        // Convert User to UserDetails (Spring Security)
        return User.builder()
                .userID(user.getUserID())
                .name(user.getName())
                .userEmail(user.getUserEmail())
                .userPassword(user.getPassword())
                .userRole(user.getUserRole())  // Assumed role is an Enum
                .build();
    }

}
