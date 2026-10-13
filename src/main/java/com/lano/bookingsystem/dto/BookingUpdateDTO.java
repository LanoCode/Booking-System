package com.lano.bookingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Booking update DTO")
public class BookingUpdateDTO {
    @Schema(description = "Customer name", example = "John Doe")
    private String customerName;

    @Schema(description = "Service type", example = "Haircut")
    private String serviceType;

    @Schema(description = "Booking date", example = "2026-08-01")
    private LocalDate bookingDate;

    @Schema(description = "Check-in date", example = "2026-08-01")
    private LocalDate checkInDate;

    @Schema(description = "Check-out date", example = "2026-08-05")
    private LocalDate checkOutDate;

    @Schema(description = "Number of guests", example = "2")
    private Integer numberOfGuests;

    @Schema(description = "Number of rooms", example = "1")
    private Integer numberOfRooms;

    @Schema(description = "Special requests", example = "Extra pillow")
    private String specialRequests;
}