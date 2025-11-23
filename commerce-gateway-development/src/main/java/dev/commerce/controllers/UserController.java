package dev.commerce.controllers;

import dev.commerce.dtos.request.UserFilterRequest;
import dev.commerce.dtos.request.UserRequest;
import dev.commerce.dtos.response.UserResponse;
import dev.commerce.entitys.Users;
import dev.commerce.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@Tag(name = "User Management", description = "User management APIs")
@Validated
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create new user", description = "Register a new user account with email verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or email already exists"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> addUser(@Valid @RequestBody UserRequest request) 
            throws MessagingException, UnsupportedEncodingException {
        UUID userId = userService.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "userId", userId,
                    "message", "User created successfully. Please verify your email."
                ));
    }

    @Operation(summary = "Get All User with Pagination", description = "Retrieve a paginated list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestBody UserFilterRequest request) {
        return ResponseEntity.ok(userService.getAllUserWithFilter(request));
    }
}
