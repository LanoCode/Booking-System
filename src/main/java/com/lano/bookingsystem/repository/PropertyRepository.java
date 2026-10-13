package com.lano.bookingsystem.repository;

import com.lano.bookingsystem.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE " +
            "(:type IS NULL OR p.type = :type) AND " +
            "(:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
            "(:minPrice IS NULL OR p.pricePerNight >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.pricePerNight <= :maxPrice) AND " +
            "p.isActive = true AND p.availableRooms > 0")
    Page<Property> searchProperties(
            @Param("type") String type,
            @Param("city") String city,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );

    List<Property> findByHostId(Long hostId);
    List<Property> findByCity(String city);
    List<Property> findByType(String type);
}