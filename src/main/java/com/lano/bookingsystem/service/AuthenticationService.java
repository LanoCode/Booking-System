package com.lano.bookingsystem.service;

import com.lano.bookingsystem.dto.AuthResponse;
import com.lano.bookingsystem.dto.LoginRequest;
import com.lano.bookingsystem.dto.RegisterRequest;
import com.lano.bookingsystem.entity.Role;
import com.lano.bookingsystem.entity.User;
import com.lano.bookingsystem.repository.UserRepository;
import com.lano.bookingsystem.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.GUEST);

        userRepository.save(user);

        // ✅ FIX: Use email consistently
        String token = jwtService.generateToken(user.getEmail());  // ← Changed to EMAIL

        return new AuthResponse(
                token,
                "Bearer",
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    public AuthResponse login(LoginRequest request) {
        try {
            System.out.println("🔐 Login attempt for email: " + request.getEmail());

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            System.out.println("✅ User found: " + user.getEmail());

            // ✅ FIX: Use email consistently
            String token = jwtService.generateToken(user.getEmail());  // ← Already using EMAIL

            System.out.println("✅ Token generated successfully");

            return new AuthResponse(
                    token,
                    "Bearer",
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name()
            );
        } catch (AuthenticationException e) {
            System.err.println("❌ Authentication failed: " + e.getMessage());
            throw new RuntimeException("Invalid email or password");
        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}