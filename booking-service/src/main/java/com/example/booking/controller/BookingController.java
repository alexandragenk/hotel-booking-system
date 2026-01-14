package com.example.booking.controller;

import com.example.booking.dto.BookingRequest;
import com.example.booking.dto.BookingResponse;
import com.example.booking.dto.ErrorResponse;
import com.example.booking.entity.Booking;
import com.example.booking.security.JwtService;
import com.example.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/api/booking")
    public ResponseEntity<?> create(@RequestBody BookingRequest request,
                                    @AuthenticationPrincipal Jwt jwt) {
        try {
            Long userId = jwt.getClaim(JwtService.ID);
            Booking resp = bookingService.createBooking(userId, request);
            return ResponseEntity.ok(resp);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
        }
    }

    @GetMapping("/api/bookings")
    public List<BookingResponse> myBookings(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim(JwtService.ID);
        return bookingService.getUserBookings(userId);
    }

    @GetMapping("/api/booking/{id}")
    public BookingResponse get(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim(JwtService.ID);
        return bookingService.getBooking(userId, id);
    }

    @DeleteMapping("/api/booking/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim(JwtService.ID);
        bookingService.cancelBooking(userId, id);
        return ResponseEntity.noContent().build();
    }
}
