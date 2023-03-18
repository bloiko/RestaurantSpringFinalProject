package com.restaurant.security.jwt;

import com.restaurant.database.entity.Role;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtProvider {

    private final String ROLES_KEY = "roles";

    private final String secretKey;

    private final long validityInMilliseconds;

    @Autowired
    public JwtProvider(@Value("${security.jwt.token.secret-key:secret-key-for-encryption}") String secretKey,
                       @Value("${security.jwt.token.expiration:18000000}") long validityInMilliseconds) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String username, Role role) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(ROLES_KEY, new SimpleGrantedAuthority(role.getAuthority()));
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return getTokenBody(token).getSubject();
    }

    public List<GrantedAuthority> getRoles(String token) {
        Map<String, String> roleClaims = getTokenBody(token).get(ROLES_KEY, Map.class);

        return Collections.singletonList(new SimpleGrantedAuthority(roleClaims.get("authority")));
    }

    private Claims getTokenBody(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody();
    }
}
