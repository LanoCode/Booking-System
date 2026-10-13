package com.lano.bookingsystem.dto;

import com.lano.bookingsystem.entity.BookingStatus;
import com.lano.bookingsystem.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Booking response DTO")
public class BookingResponse {
    @Schema(description = "Booking ID", example = "1")
    private Long id;

    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;

    @Schema(description = "Service type", example = "Haircut")
    private String serviceType;

    @Schema(description = "Booking date", example = "2026-08-01")
    private LocalDate bookingDate;

    // New fields
    @Schema(description = "Property ID", example = "1")
    private Long propertyId;

    @Schema(description = "Property name", example = "Grand Hotel")
    private String propertyName;

    @Schema(description = "Property type", example = "HOTEL")
    private String propertyType;

    @Schema(description = "Property image URL")
    private String propertyImage;

    @Schema(description = "Guest ID", example = "1")
    private Long guestId;

    @Schema(description = "Guest name", example = "John Doe")
    private String guestName;

    @Schema(description = "Guest email", example = "john@example.com")
    private String guestEmail;

    @Schema(description = "Check-in date", example = "2026-08-01")
    private LocalDate checkInDate;

    @Schema(description = "Check-out date", example = "2026-08-05")
    private LocalDate checkOutDate;

    @Schema(description = "Number of guests", example = "2")
    private Integer numberOfGuests;

    @Schema(description = "Number of rooms", example = "1")
    private Integer numberOfRooms;

    @Schema(description = "Total price", example = "500.00")
    private Double totalPrice;

    @Schema(description = "Booking status", example = "CONFIRMED")
    private BookingStatus bookingStatus;

    @Schema(description = "Payment status", example = "PAID")
    private PaymentStatus paymentStatus;

    @Schema(description = "Special requests")
    private String specialRequests;

    @Schema(description = "Is reviewed", example = "false")
    private Boolean isReviewed;

    @Schema(description = "Created at timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at timestamp")
    private LocalDateTime updatedAt;
}