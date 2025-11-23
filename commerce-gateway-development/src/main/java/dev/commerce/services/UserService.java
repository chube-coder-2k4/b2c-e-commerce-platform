package dev.commerce.services;

import dev.commerce.dtos.request.UserFilterRequest;
import dev.commerce.dtos.request.UserRequest;
import dev.commerce.dtos.request.UserUpdateRequest;
import dev.commerce.dtos.response.UserResponse;
import dev.commerce.entitys.Users;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {

    UUID saveUser(UserRequest request) throws MessagingException, UnsupportedEncodingException;

    UUID updateUser(UUID userId, UserUpdateRequest request);

    String confirmUser(String email, String verifyCode);

    void deleteUser(UUID userId);

    Users findByEmail(String email);

    Users findById(UUID userId);

    Users findByUsername(String username);

    Page<UserResponse> getAllUserWithFilter(UserFilterRequest filterRequest);

}
