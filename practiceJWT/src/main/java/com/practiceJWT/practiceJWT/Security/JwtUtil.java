package com.practiceJWT.practiceJWT.Security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "mySuperSecureLongKeyThatIsAtLeast32Bytes!";

    private SecretKey getSigningKeys() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Extract roles
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles) // Add roles to the token
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1-hour expiry
                .signWith(getSigningKeys())
                .compact();
    }


    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Object rolesObject = getClaims(token).get("roles");
        if (rolesObject instanceof List<?>) {
            List<String> roles = (List<String>) rolesObject;
            System.out.println("Extracted Roles: " + roles);
            return roles;
        }
        return List.of();
    }

    public String extractUsername (String token){
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token, String username){
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private Claims getClaims(String token){
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKeys())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        System.out.println("Decoded JWT Claims: " +claims);
        return claims;
    }

    private boolean isTokenExpired(String token){
        return getClaims(token).getExpiration().before(new Date());
    }

}
