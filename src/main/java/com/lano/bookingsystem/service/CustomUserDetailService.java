package com.lano.bookingsystem.service;

import com.lano.bookingsystem.entity.User;
import com.lano.bookingsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 Loading user by email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        System.out.println("✅ User found: " + user.getEmail());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())  // ← Use email as username
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }
}