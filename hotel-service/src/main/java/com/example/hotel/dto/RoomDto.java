package com.example.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private Long hotelId;
    private String number;
    private Long timesBooked;
}
