package dev.commerce.services.security.impl;

import dev.commerce.dtos.common.TokenType;
import dev.commerce.dtos.request.ChangePasswordRequest;
import dev.commerce.dtos.request.LoginRequest;
import dev.commerce.dtos.request.ResetPasswordRequest;
import dev.commerce.dtos.response.LoginResponse;
import dev.commerce.entitys.RefreshToken;
import dev.commerce.entitys.Users;
import dev.commerce.exception.InvalidDataException;
import dev.commerce.exception.UserNotFoundException;
import dev.commerce.repositories.jpa.UserRepository;
import dev.commerce.services.RefreshTokenService;
import dev.commerce.services.UserService;
import dev.commerce.services.security.AuthenticationService;
import dev.commerce.services.security.JwtService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final RefreshTokenService tokenService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UserNotFoundException("User not found"));
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    @Override
    public LoginResponse refreshToken(HttpServletRequest request) {
        String token = request.getHeader("x-refresh-token");
        if (StringUtils.isBlank(token)) {
            throw new InvalidDataException("Invalid refresh token must be not blank");
        }
        final String username = jwtService.extractUsername(token, TokenType.REFRESH);
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!jwtService.isTokenValid(token, user, TokenType.REFRESH)) {
            throw new InvalidDataException("Invalid refresh token");

        }
        String accessToken = jwtService.generateAccessToken(user);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .userId(user.getId())
                .build();
    }

    @Override
    public String logout(HttpServletRequest request) {
        String refresh = request.getHeader("x-refresh-token");
        if (StringUtils.isBlank(refresh)) {
            throw new InvalidDataException("Invalid refresh token must be not blank");
        }
        final String username = jwtService.extractUsername(refresh, TokenType.REFRESH);
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!jwtService.isTokenValid(refresh, user, TokenType.REFRESH)) {
            throw new InvalidDataException("Invalid refresh token");
        }
        tokenService.deleteByUserId(user);
        return "Logout successful";
    }

    @Override
    public String forgotPassword(String email) {
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(!user.isEnabled()) {
            throw new InvalidDataException("User account is not activated");
        }
        
        // Generate reset password token
        String resetToken = jwtService.generateResetPasswordToken(user);
        
        // Save reset token to database (not refresh token)
        tokenService.saveRefreshToken(RefreshToken.builder()
                .token(resetToken)
                .usersId(user)
                .expDate(jwtService.extractExpiration(resetToken, TokenType.RESET_PASSWORD)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build());
        
        // TODO: Send email with reset link
        log.info("Password reset token generated for user: {}", email);
        log.info("Reset token: {}", resetToken);
        
        return "Password reset instructions have been sent to your email";
    }

    @Override
    public String resetPassword(ResetPasswordRequest request) {
        // Validate passwords match
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("New password and confirm password do not match");
        }
        
        // Validate token and get user
        final String username = jwtService.extractUsername(request.getToken(), TokenType.RESET_PASSWORD);
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        if (!jwtService.isTokenValid(request.getToken(), user, TokenType.RESET_PASSWORD)) {
            throw new InvalidDataException("Invalid or expired reset password token");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        // Delete used reset token
        tokenService.deleteByUserId(user);
        
        log.info("Password reset successful for user: {}", username);
        return "Password has been reset successfully";
    }

    @Override
    public String changePassword(ChangePasswordRequest request) {
        Users user = isValidUser(request.getSecretKey());
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return "Change password successful";
    }

    private Users isValidUser(String secretKey) {
        final String username = jwtService.extractUsername(secretKey, TokenType.RESET_PASSWORD);
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!jwtService.isTokenValid(secretKey, user, TokenType.RESET_PASSWORD)) {
            throw new InvalidDataException("Invalid secret key");
        }
        return user;
    }


}
