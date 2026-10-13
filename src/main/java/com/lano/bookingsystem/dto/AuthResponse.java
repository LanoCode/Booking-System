package com.lano.bookingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response")
public class AuthResponse {
    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Token type", example = "Bearer")
    private String type;

    @Schema(description = "Username", example = "admin")
    private String username;

    @Schema(description = "User email", example = "admin@booking.com")
    private String email;

    @Schema(description = "User role", example = "ADMIN")
    private String role;
}