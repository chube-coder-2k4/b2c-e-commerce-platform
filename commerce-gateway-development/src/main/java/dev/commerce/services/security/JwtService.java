package dev.commerce.services.security;

import dev.commerce.dtos.common.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);
    String extractUsername(String token, TokenType tokenType);
    String generateRefreshToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails, TokenType tokenType);
    String generateResetPasswordToken(UserDetails userDetails);
    Date extractExpiration(String token, TokenType tokenType);
}
