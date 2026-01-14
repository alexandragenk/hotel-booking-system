package com.example.hotel.service;

import com.example.hotel.dto.RoomAvailabilityResponse;
import com.example.hotel.dto.RoomDto;
import com.example.hotel.entity.Booking;
import com.example.hotel.entity.Hotel;
import com.example.hotel.entity.Room;
import com.example.hotel.repository.BookingRepository;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.repository.RoomRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomDto create(RoomDto room) {
        Hotel hotel = hotelRepository.findById(room.getHotelId())
                .orElseThrow(() -> new NotFoundException("Hotel not found"));
        return roomRepository.save(new Room(null, hotel, room.getNumber(), 0)).toDto();
    }

    public List<RoomDto> getRecommendedRooms(LocalDate startDate, LocalDate endDate) {
        return roomRepository.findAvailableRooms(startDate, endDate)
                .stream()
                .map(Room::toDto)
                .toList();
    }

    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(Room::toDto)
                .toList();
    }

    @Transactional
    public RoomAvailabilityResponse confirmAvailability(
            Long roomId, LocalDate startDate,
            LocalDate endDate, Long userId) {
        Room room = roomRepository.lockById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found"));

        boolean alreadyBooked =
                bookingRepository.existsByRoomAndStartDateAndEndDateAndUserId(room, startDate, endDate, userId);

        if (alreadyBooked) {
            return new RoomAvailabilityResponse(true);
        }

        boolean busy = bookingRepository.existsOverlap(room, startDate, endDate);

        if (busy) {
            return new RoomAvailabilityResponse(false);
        }

        Booking booking = new Booking(null, room, userId, startDate, endDate);
        bookingRepository.save(booking);
        return new RoomAvailabilityResponse(true);
    }

    @Transactional
    public void release(
            Long roomId, LocalDate startDate,
            LocalDate endDate, Long userId) {
        bookingRepository.deleteByRoomIdAndStartDateAndEndDateAndUserId(roomId, startDate, endDate, userId);
    }
}
