package com.lano.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.GUEST;

    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"host", "bookings", "reviews"})
    private List<Property> properties;

    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"guest", "property"})
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user", "property"})
    private List<Review> reviews;

    // ... rest of your fields
}