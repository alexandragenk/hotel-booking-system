package com.example.hotel.controller;

import com.example.hotel.entity.Hotel;
import com.example.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<Hotel> create(@RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelService.create(hotel));
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> all() {
        return ResponseEntity.ok(hotelService.getAll());
    }
}
