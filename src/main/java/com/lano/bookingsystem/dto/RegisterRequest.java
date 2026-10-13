package com.lano.bookingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Registration request")
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Schema(description = "Username", example = "john_doe", required = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "User email", example = "john@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password123", required = true)
    private String password;
}