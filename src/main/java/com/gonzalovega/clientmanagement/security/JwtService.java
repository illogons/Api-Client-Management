package com.gonzalovega.clientmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    /**
     * Extracts the user ID from the given JWT token.
     *
     * @param token the JWT token from which to extract the user ID
     * @return the user ID extracted from the token, or null if extraction fails
     */
    public Integer extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Integer.class));
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token from which to extract the username
     * @return the username extracted from the token, or null if extraction fails
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using the provided claims resolver function.
     *
     * @param token          the JWT token from which to extract the claim
     * @param claimsResolver a function that takes Claims and returns the desired claim value
     * @param <T>            the type of the claim to be extracted
     * @return the extracted claim value, or null if extraction fails
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token from which to extract claims
     * @return the Claims object containing all claims extracted from the token
     * @throws RuntimeException if parsing the token fails
     */
    private Claims extractAllClaims(String token) {
        try {

            int i = token.lastIndexOf('.');
            String withoutSignature = token.substring(0, i + 1);

            return Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(withoutSignature)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT token: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token to check for expiration
     * @return true if the token is expired or if an error occurs during parsing, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}