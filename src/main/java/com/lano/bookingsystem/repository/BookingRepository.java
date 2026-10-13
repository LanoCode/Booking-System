package com.lano.bookingsystem.repository;

import com.lano.bookingsystem.entity.Booking;  // ✅ ADD THIS IMPORT!
import com.lano.bookingsystem.entity.BookingStatus;
import com.lano.bookingsystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Legacy methods
    Page<Booking> findByServiceTypeAndBookingDate(String serviceType, LocalDate bookingDate, Pageable pageable);
    Page<Booking> findByServiceType(String serviceType, Pageable pageable);
    Page<Booking> findByBookingDate(LocalDate bookingDate, Pageable pageable);

    // New methods
    Page<Booking> findByGuest(User guest, Pageable pageable);
    Page<Booking> findByProperty_Host(User host, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.property.id = :propertyId AND " +
            "((b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate) OR " +
            "(b.checkInDate >= :checkInDate AND b.checkInDate < :checkOutDate)) AND " +
            "b.bookingStatus != 'CANCELLED'")
    List<Booking> findOverlappingBookings(
            @Param("propertyId") Long propertyId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );

    List<Booking> findByBookingStatus(BookingStatus status);
    List<Booking> findByGuestAndBookingStatus(User guest, BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.property.id = :propertyId AND " +
            "b.checkInDate <= :date AND b.checkOutDate > :date AND " +
            "b.bookingStatus != 'CANCELLED'")
    Long countOccupiedRooms(@Param("propertyId") Long propertyId, @Param("date") LocalDate date);
}