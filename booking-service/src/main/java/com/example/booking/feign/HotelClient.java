package com.example.booking.feign;

import com.example.booking.dto.AvailabilityResponse;
import com.example.booking.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "hotel-service")
public interface HotelClient {

    @GetMapping("/api/rooms/recommend")
    List<RoomDto> getRecommendedRooms(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    );

    @PostMapping("/api/rooms/{id}/confirm-availability")
    AvailabilityResponse confirmAvailability(
            @PathVariable("id") Long roomId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    );

    @PostMapping("/api/rooms/{id}/release")
    void releaseRoom(
            @PathVariable("id") Long roomId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    );
}
