package dev.commerce.dtos.request;

import dev.commerce.dtos.common.LoginType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

//        private UUID id;
//        private String fullName;
//        private String email;
//        private String password;
//        private String phone;
//        private boolean isVerify = false;
//        private boolean isActive = true;
//        private boolean isLocked = false;
//        private LoginType provider = LoginType.LOCAL;
//        private String address;
//        private Set<Role> roles = new HashSet<>();




}
