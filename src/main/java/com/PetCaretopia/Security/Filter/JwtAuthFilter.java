package com.PetCaretopia.Security.Filter;



import com.PetCaretopia.Security.Service.CustomUserDetailsService;
import com.PetCaretopia.Security.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        // Extract token from the Authorization header
        String token = request.getHeader("Authorization");

        // If token is not present or doesn't start with "Bearer", proceed with the next filter
        if (token == null || !token.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Remove the "Bearer " part to get the actual token
        token = token.substring(7);

        // Extract username (email) from the token
        String email = jwtUtil.extractUsername(token);

        // If email is null or there's already an authenticated user, skip processing
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            // Validate the token
            if (jwtUtil.validateToken(token, userDetails)) {
                // If token is valid, create an authentication token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                // Set additional details like IP address, session info, etc.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication token in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else {
                // 🚫 Invalid token — stop the request and return 401
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid or expired token\"}");
                return;
            }
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }
}
