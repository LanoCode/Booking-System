package com.lano.bookingsystem.controller;

import com.lano.bookingsystem.dto.BookingRequest;
import com.lano.bookingsystem.dto.BookingResponse;
import com.lano.bookingsystem.dto.BookingUpdateDTO;
import com.lano.bookingsystem.service.BookingService;
import com.lano.bookingsystem.wrapper.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingResponse> bookings = bookingService.getAllBookings(null, null, pageable);
        return ResponseEntity.ok(new ApiResponse<>(200, "Bookings retrieved successfully", bookings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id) {
        BookingResponse booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Booking retrieved successfully", booking));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse booking = bookingService.createBooking(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Booking created successfully", booking));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingUpdateDTO dto) {
        BookingResponse booking = bookingService.updateBooking(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Booking updated successfully", booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Booking deleted successfully", null));
    }
}