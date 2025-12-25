package social_media.social_media_handler.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil{

    // Helper to get a secure SecretKey object
    private SecretKey getSigningKey() {
        // Ensure your secret is at least 64 characters for HS512 security
        String jwtSecret = "SocialMediaHandlerSystemSecretKey12345678901234567890123456789012";
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        int jwtExpirationMs = 86400000;
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromToken(String token) {
        // UPDATED: Use parserBuilder() and build()
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Modern replacement
                .build()                        // Returns an immutable JwtParser
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            // UPDATED: Use parserBuilder() for validation
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log the error (optional)
            return false;
        }
    }
}