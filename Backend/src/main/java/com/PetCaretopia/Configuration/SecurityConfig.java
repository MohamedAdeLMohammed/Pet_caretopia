package com.PetCaretopia.Configuration;

import com.PetCaretopia.Chat.Service.AccountService;
import com.PetCaretopia.Security.Filter.JwtAuthFilter;
import com.PetCaretopia.Security.Service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {


    private final AccountService accountService;
    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers(
                                        "/css/**",
                                        "/js/**",
                                        "/images/**",
                                        "/webjars/**",
                                        "/auth/**",
                                        "/connect-chat/**",
                                        "/chat",
                                        "/messages/{senderId}/{receiverId}",
                                        "/swagger-ui/**",
                                        "/store/**",
                                        "/v3/api-docs/**",
                                        "/users/email/{email}"

                                ).permitAll()

                                .requestMatchers(
                                        "/social/posts/**",
                                        "/social/comments/**",
                                        "/social/reactions/**",
                                        "/social/shares/**",
                                        "/social/reports/**",
                                        "/social/notifications/**"
                                ).authenticated()

                        .requestMatchers(
                                "/cart/**",
                                "/checkout/**",
                                "/orders/**",
                                "/inventory/**",
                                "/products/**",
                                "/wishlist/**",
                                "pets/**",
                                "pet-types/**",
                                "pet-breeds/**",
                                "adoptions/**",
                                "shelters/**"
                        ).authenticated()

                                .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("frame-ancestors 'self' https://localhost:5173 https://example.com")
                        )

                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getWriter().write("{\"error\": \"" + authException.getMessage() + "\"}");
                        })
                )
                .formLogin(form -> form
                        .loginPage("/connect")
                        .loginProcessingUrl("/connect-chat/connect")
                        .defaultSuccessUrl("/connect-chat/chat_room", true)
                        .failureUrl("/connect-chat/connect?error=true")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/connect-chat/connect?logout")
                        .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                );

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder());

    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
