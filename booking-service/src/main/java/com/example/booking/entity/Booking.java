package com.example.booking.entity;

import com.example.booking.dto.BookingResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long roomId;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public Booking(Long userId, Long roomId, LocalDate startDate, LocalDate endDate, BookingStatus status) {
        this.userId = userId;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public BookingResponse toResponse() {
        return new BookingResponse(id, userId, roomId, startDate, endDate, status);
    }
}
