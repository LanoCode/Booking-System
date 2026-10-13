package com.lano.bookingsystem.service;

import com.lano.bookingsystem.dto.BookingRequest;
import com.lano.bookingsystem.dto.BookingResponse;
import com.lano.bookingsystem.dto.BookingUpdateDTO;
import com.lano.bookingsystem.exception.NotFoundException;
import com.lano.bookingsystem.entity.Booking;
import com.lano.bookingsystem.entity.BookingStatus;
import com.lano.bookingsystem.entity.PaymentStatus;
import com.lano.bookingsystem.entity.Property;
import com.lano.bookingsystem.entity.User;
import com.lano.bookingsystem.repository.BookingRepository;
import com.lano.bookingsystem.repository.PropertyRepository;
import com.lano.bookingsystem.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
                          PropertyRepository propertyRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User guest = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Property property = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new NotFoundException("Property not found"));

        if (property.getAvailableRooms() < request.getNumberOfRooms()) {
            throw new RuntimeException("Not enough rooms available. Available: " + property.getAvailableRooms());
        }

        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        if (nights <= 0) {
            throw new RuntimeException("Check-out date must be after check-in date");
        }
        Double totalPrice = property.getPricePerNight() * nights * request.getNumberOfRooms();

        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setGuest(guest);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setNumberOfRooms(request.getNumberOfRooms());
        booking.setTotalPrice(totalPrice);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setSpecialRequests(request.getSpecialRequests());
        booking.setPaymentMethod(request.getPaymentMethod());

        booking.setCustomerName(guest.getFirstName() + " " + guest.getLastName());
        booking.setServiceType(property.getType());
        booking.setBookingDate(request.getCheckInDate());

        Booking saved = bookingRepository.save(booking);

        property.setAvailableRooms(property.getAvailableRooms() - request.getNumberOfRooms());
        propertyRepository.save(property);

        return mapToResponse(saved);
    }

    public Page<BookingResponse> getAllBookings(String serviceType, LocalDate bookingDate, Pageable pageable) {
        Page<Booking> bookings;

        if (serviceType != null && bookingDate != null) {
            bookings = bookingRepository.findByServiceTypeAndBookingDate(serviceType, bookingDate, pageable);
        } else if (serviceType != null) {
            bookings = bookingRepository.findByServiceType(serviceType, pageable);
        } else if (bookingDate != null) {
            bookings = bookingRepository.findByBookingDate(bookingDate, pageable);
        } else {
            bookings = bookingRepository.findAll(pageable);
        }

        return bookings.map(this::mapToResponse);
    }

    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));
        return mapToResponse(booking);
    }

    public Page<BookingResponse> getMyBookings(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User guest = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Page<Booking> bookings = bookingRepository.findByGuest(guest, pageable);
        return bookings.map(this::mapToResponse);
    }

    public Page<BookingResponse> getHostBookings(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User host = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Page<Booking> bookings = bookingRepository.findByProperty_Host(host, pageable);
        return bookings.map(this::mapToResponse);
    }

    @Transactional
    public BookingResponse updateBooking(Long id, BookingUpdateDTO dto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Cannot update booking with status: " + booking.getBookingStatus());
        }

        if (dto.getCheckInDate() != null) {
            booking.setCheckInDate(dto.getCheckInDate());
            booking.setBookingDate(dto.getCheckInDate());
        }
        if (dto.getCheckOutDate() != null) {
            booking.setCheckOutDate(dto.getCheckOutDate());
        }
        if (dto.getNumberOfGuests() != null) {
            booking.setNumberOfGuests(dto.getNumberOfGuests());
        }
        if (dto.getNumberOfRooms() != null) {
            Property property = booking.getProperty();
            int diff = dto.getNumberOfRooms() - booking.getNumberOfRooms();
            if (diff > 0 && property.getAvailableRooms() < diff) {
                throw new RuntimeException("Not enough rooms available");
            }
            if (diff > 0) {
                property.setAvailableRooms(property.getAvailableRooms() - diff);
                propertyRepository.save(property);
            }
            booking.setNumberOfRooms(dto.getNumberOfRooms());
        }
        if (dto.getSpecialRequests() != null) {
            booking.setSpecialRequests(dto.getSpecialRequests());
        }

        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        booking.setTotalPrice(booking.getProperty().getPricePerNight() * nights * booking.getNumberOfRooms());

        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    @Transactional
    public BookingResponse cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel a completed booking");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInDateTime = booking.getCheckInDate().atStartOfDay();
        if (now.isAfter(checkInDateTime.minusHours(24))) {
            throw new RuntimeException("Cancellation is only allowed 24 hours before check-in");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason("Cancelled by guest");

        Property property = booking.getProperty();
        property.setAvailableRooms(property.getAvailableRooms() + booking.getNumberOfRooms());
        propertyRepository.save(property);

        Booking cancelled = bookingRepository.save(booking);
        return mapToResponse(cancelled);
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));

        if (booking.getBookingStatus() != BookingStatus.CANCELLED) {
            Property property = booking.getProperty();
            property.setAvailableRooms(property.getAvailableRooms() + booking.getNumberOfRooms());
            propertyRepository.save(property);
        }

        bookingRepository.deleteById(id);
    }

    @Transactional
    public BookingResponse confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Only pending bookings can be confirmed");
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        Booking confirmed = bookingRepository.save(booking);
        return mapToResponse(confirmed);
    }

    @Transactional
    public BookingResponse completeBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Only confirmed bookings can be completed");
        }

        if (booking.getCheckOutDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Cannot complete booking before check-out date");
        }

        booking.setBookingStatus(BookingStatus.COMPLETED);
        Booking completed = bookingRepository.save(booking);
        return mapToResponse(completed);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();

        response.setId(booking.getId());
        response.setCustomerName(booking.getCustomerName());
        response.setServiceType(booking.getServiceType());
        response.setBookingDate(booking.getBookingDate());

        if (booking.getProperty() != null) {
            response.setPropertyId(booking.getProperty().getId());
            response.setPropertyName(booking.getProperty().getName());
            response.setPropertyType(booking.getProperty().getType());
            String images = booking.getProperty().getImageUrls();
            if (images != null && !images.isEmpty()) {
                response.setPropertyImage(images.split(",")[0]);
            }
        }

        if (booking.getGuest() != null) {
            response.setGuestId(booking.getGuest().getId());
            String firstName = booking.getGuest().getFirstName() != null ? booking.getGuest().getFirstName() : "";
            String lastName = booking.getGuest().getLastName() != null ? booking.getGuest().getLastName() : "";
            response.setGuestName(firstName + " " + lastName);
            response.setGuestEmail(booking.getGuest().getEmail());
        }

        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());
        response.setNumberOfGuests(booking.getNumberOfGuests());
        response.setNumberOfRooms(booking.getNumberOfRooms());
        response.setTotalPrice(booking.getTotalPrice());
        response.setBookingStatus(booking.getBookingStatus());
        response.setPaymentStatus(booking.getPaymentStatus());
        response.setSpecialRequests(booking.getSpecialRequests());
        response.setIsReviewed(booking.getIsReviewed());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());

        return response;
    }
}