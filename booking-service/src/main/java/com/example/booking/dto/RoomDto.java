package com.example.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private Long hotelId;
    private String number;
    private boolean available;
    private long timesBooked;
}
