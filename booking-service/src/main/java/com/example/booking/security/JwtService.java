package com.example.booking.security;

import com.example.booking.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    public static final String ID = "id";
    public static final String ROLE = "role";
    private final JwtProperties properties;
    private final Key key;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String username, User.Role role) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + properties.getExpirationMillis());

        return Jwts.builder()
                .setSubject(username)
                .claim(ID, userId)
                .claim(ROLE, role)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
