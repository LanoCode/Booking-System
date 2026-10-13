package com.lano.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // HOTEL, BNB, GUESTHOUSE, RESORT, APARTMENT

    @Column(length = 2000)
    private String description;

    private String address;
    private String city;
    private String country;
    private String postalCode;

    private Double latitude;
    private Double longitude;

    @Column(name = "price_per_night", nullable = false)
    private Double pricePerNight;

    @Column(name = "star_rating")
    private Integer starRating;

    @Column(name = "total_rooms")
    private Integer totalRooms;

    @Column(name = "available_rooms")
    private Integer availableRooms;

    @Column(length = 1000)
    private String amenities;

    @Column(length = 2000)
    private String imageUrls;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    private String website;

    // ✅ ADD THESE MISSING FIELDS!
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_verified")
    private Boolean isVerified = false;

    @Column(name = "cancellation_policy")
    private String cancellationPolicy;

    @Column(name = "check_in_time")
    private String checkInTime;

    @Column(name = "check_out_time")
    private String checkOutTime;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "host_id")
    @JsonIgnoreProperties({"properties", "bookings", "reviews"})
    private User host;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"property", "guest"})
    private List<Booking> bookings;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"property", "user"})
    private List<Review> reviews;

    // Timestamps
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}