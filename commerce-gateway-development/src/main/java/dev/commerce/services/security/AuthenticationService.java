package dev.commerce.services.security;

import dev.commerce.dtos.request.ChangePasswordRequest;
import dev.commerce.dtos.request.LoginRequest;
import dev.commerce.dtos.request.ResetPasswordRequest;
import dev.commerce.dtos.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(HttpServletRequest request);
    String logout(HttpServletRequest request);
    String forgotPassword(String email);
    String resetPassword(ResetPasswordRequest request);
    String changePassword(ChangePasswordRequest request);
}
