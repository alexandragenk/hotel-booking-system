package com.example.hotel.service;

import com.example.hotel.entity.Hotel;
import com.example.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    public Hotel create(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }
}
