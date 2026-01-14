package com.example.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingRequest {
    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean autoSelect;
}
