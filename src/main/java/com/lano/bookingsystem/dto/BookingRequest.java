package com.lano.bookingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Booking request DTO")
public class BookingRequest {
    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;

    @Schema(description = "Service type", example = "Haircut")
    private String serviceType;

    @Schema(description = "Booking date", example = "2026-08-01")
    private LocalDate bookingDate;

    @NotNull(message = "Property ID is required")
    @Schema(description = "Property ID", example = "1", required = true)
    private Long propertyId;

    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in must be today or in the future")
    @Schema(description = "Check-in date", example = "2026-08-01", required = true)
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @FutureOrPresent(message = "Check-out must be today or in the future")
    @Schema(description = "Check-out date", example = "2026-08-05", required = true)
    private LocalDate checkOutDate;

    @NotNull(message = "Number of guests is required")
    @Schema(description = "Number of guests", example = "2", required = true)
    private Integer numberOfGuests = 1;

    @Schema(description = "Number of rooms", example = "1")
    private Integer numberOfRooms = 1;

    @Schema(description = "Special requests", example = "Extra pillow")
    private String specialRequests;

    @Schema(description = "Payment method", example = "CREDIT_CARD")
    private String paymentMethod;
}