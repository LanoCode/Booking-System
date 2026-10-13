package com.lano.bookingsystem.security;

import com.lano.bookingsystem.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService userDetailService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   CustomUserDetailService userDetailService) {
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // ✅ Log for debugging
        System.out.println("🔐 Auth Header: " + (authHeader != null ? "Present" : "Missing"));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No valid auth header found");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        System.out.println("🔑 JWT Token: " + jwt.substring(0, Math.min(jwt.length(), 30)) + "...");

        try {
            // ✅ Extract email from token
            String email = jwtService.extractUsername(jwt);
            System.out.println("📧 Extracted email: " + email);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // ✅ Load user by email
                UserDetails userDetails = userDetailService.loadUserByUsername(email);
                System.out.println("👤 User details loaded: " + userDetails.getUsername());
                System.out.println("🎭 Authorities: " + userDetails.getAuthorities());

                // ✅ Validate token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    System.out.println("✅ Token is valid");

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("✅ Authentication set in SecurityContext");
                } else {
                    System.out.println("❌ Token is invalid or expired");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error processing JWT: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}