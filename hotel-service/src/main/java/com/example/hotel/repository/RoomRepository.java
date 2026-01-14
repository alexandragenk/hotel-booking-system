package com.example.hotel.repository;

import com.example.hotel.entity.Room;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("""
    SELECT r FROM Room r
    WHERE NOT EXISTS (
        SELECT 1 FROM Booking b
        WHERE b.room = r
        AND b.startDate < :endDate
        AND b.endDate > :startDate
    )
    """)
    List<Room> findAvailableRooms(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Room r where r.id = :id")
    Optional<Room> lockById(@Param("id") Long id);
}
