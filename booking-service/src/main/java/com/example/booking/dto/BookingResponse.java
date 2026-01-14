package com.example.booking.dto;

import com.example.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
}
