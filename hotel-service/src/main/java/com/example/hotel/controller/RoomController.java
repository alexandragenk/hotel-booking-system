package com.example.hotel.controller;

import com.example.hotel.dto.RoomAvailabilityResponse;
import com.example.hotel.dto.RoomDto;
import com.example.hotel.entity.Room;
import com.example.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/api/rooms")
    public ResponseEntity<RoomDto> create(@RequestBody RoomDto room) {
        return ResponseEntity.ok(roomService.create(room));
    }

    @GetMapping("/api/rooms")
    public ResponseEntity<List<RoomDto>> allRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/api/rooms/recommend")
    public ResponseEntity<List<RoomDto>> recommend(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(roomService.getRecommendedRooms(startDate, endDate));
    }

    @PostMapping("/api/rooms/{id}/confirm-availability")
    public ResponseEntity<RoomAvailabilityResponse> confirm(
            @PathVariable Long id, @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(roomService.confirmAvailability(id, startDate, endDate, userId));
    }

    @PostMapping("/api/rooms/{id}/release")
    public ResponseEntity<Void> release(
            @PathVariable Long id, @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("id");
        roomService.release(id, startDate, endDate, userId);
        return ResponseEntity.noContent().build();
    }
}
