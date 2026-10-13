package com.lano.bookingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Column(name = "customer_name")
    private String customerName;

    @NotBlank(message = "Service type is required")
    @Column(name = "service_type")
    private String serviceType;

    @FutureOrPresent(message = "Booking date must be today or in the future")
    @Column(name = "booking_date")
    private LocalDate bookingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    @JsonIgnoreProperties({"bookings", "reviews", "host"})
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    @JsonIgnoreProperties({"bookings", "properties", "reviews"})
    private User guest;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date must be today or in the future")
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @FutureOrPresent(message = "Check-out date must be today or in the future")
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "number_of_guests")
    private Integer numberOfGuests = 1;

    @Column(name = "number_of_rooms")
    private Integer numberOfRooms = 1;

    @Column(name = "total_price")
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus = BookingStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(length = 500)
    private String specialRequests;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "is_reviewed")
    private Boolean isReviewed = false;

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