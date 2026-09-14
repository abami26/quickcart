package com.niit.quickcart.security;

import com.niit.quickcart.exception.UnauthorizedException;
import com.niit.quickcart.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Creates and validates the JWT ("login token") that proves who a request is from.
 * In a real production app this secret would come from an environment variable,
 * not be hardcoded — but for a class project this is fine.
 */
@Component
public class JwtUtil {

    private static final String SECRET = "QuickCartSuperSecretJWTSigningKeyForClassProject2026!!";
    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRY_MILLIS = 1000L * 60 * 60 * 24 * 7; // 7 days

    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRY_MILLIS);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("name", user.getName())
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Pulls the user id out of an "Authorization: Bearer <token>" header.
     * Throws UnauthorizedException (-> HTTP 401) if missing or invalid.
     */
    public Long requireUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Please log in first.");
        }
        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            throw new UnauthorizedException("Your session expired. Please log in again.");
        }
    }
}
