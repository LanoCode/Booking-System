package com.lano.bookingsystem.config;

import com.lano.bookingsystem.entity.Role;
import com.lano.bookingsystem.entity.User;
import com.lano.bookingsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminUserSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserSeeder.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdminUser();
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  🚀 Booking System Application Started Successfully!");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("  📧 Admin Email: admin@booking.com");
        System.out.println("  🔑 Admin Password: Admin123!");
        System.out.println("  🌐 Swagger UI: http://localhost:8081/swagger-ui/index.html");
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    private void seedAdminUser() {
        String adminEmail = "admin@booking.com";

        try {
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("Admin123!"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
                logger.info("✅ Admin user seeded successfully!");
            } else {
                logger.info("ℹ️ Admin user already exists, skipping seed.");
            }
        } catch (Exception e) {
            logger.error("❌ Failed to seed admin user: {}", e.getMessage());
        }
    }
}