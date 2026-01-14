package com.example.hotel.repository;

import com.example.hotel.entity.Booking;
import com.example.hotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
    SELECT COUNT(b) > 0 FROM Booking b
    WHERE b.room = :room
    AND b.startDate < :endDate
    AND b.endDate > :startDate
    """)
    boolean existsOverlap(
            Room room,
            LocalDate startDate,
            LocalDate endDate
    );

    boolean existsByRoomAndStartDateAndEndDateAndUserId(
            Room room,
            LocalDate startDate,
            LocalDate endDate,
            Long userId
    );

    void deleteByRoomIdAndStartDateAndEndDateAndUserId(
            Long roomId,
            LocalDate startDate,
            LocalDate endDate,
            Long userId
    );
}
