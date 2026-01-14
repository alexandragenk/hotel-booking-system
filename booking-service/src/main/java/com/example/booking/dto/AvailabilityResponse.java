package com.example.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailabilityResponse {
    private final boolean confirmed;
}
