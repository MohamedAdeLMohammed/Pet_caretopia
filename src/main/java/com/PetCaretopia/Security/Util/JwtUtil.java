package com.PetCaretopia.Security.Util;

import com.PetCaretopia.Configuration.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;

    public String generateToken(Long userId,String name,String age,String username,String role,String phoneNumber){
        long expirationTime =  jwtConfig.getExpiration();
       try {
            String token = Jwts.builder()
                    .setSubject(username)
                    .claim("id", userId)
                    .claim("name", name)
                    .claim("age",age)
                    .claim("role", role)
                    .claim("phoneNumber",phoneNumber)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(getSigningKey(),SignatureAlgorithm.HS512) // FIXED: Use bytes
                    .compact();


            return token;
        } catch (Exception e) {
            System.err.println("❌ Error Generating Token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public boolean validateToken(String token , UserDetails userDetails){
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtConfig.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes); // ✅ Ensures correct key size
    }
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token,java.util.function.Function<Claims,T> claimsResolver){
        final Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
    public boolean isTokenValid(String token){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();
            return !isTokenExpired(token);
        }catch (Exception e){
            return false;
        }
    }
}
