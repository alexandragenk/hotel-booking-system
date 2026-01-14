package com.example.booking.service;

import com.example.booking.dto.AvailabilityResponse;
import com.example.booking.dto.BookingRequest;
import com.example.booking.dto.BookingResponse;
import com.example.booking.dto.RoomDto;
import com.example.booking.entity.Booking;
import com.example.booking.entity.BookingStatus;
import com.example.booking.feign.HotelClient;
import com.example.booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final HotelClient hotelClient;

    @Transactional
    public Booking createBooking(Long userId, BookingRequest request) {
        Booking booking = new Booking(userId, null, request.getStartDate(), request.getEndDate(), BookingStatus.PENDING);
        booking = bookingRepository.save(booking);

        try {
            Long roomId = request.isAutoSelect()
                    ? selectRoom(request)
                    : request.getRoomId();

            if (roomId == null) {
                throw new RuntimeException("No available rooms");
            }

            booking.setRoomId(roomId);
            AvailabilityResponse response = hotelClient.confirmAvailability(
                    booking.getRoomId(),
                    booking.getStartDate(),
                    booking.getEndDate()
            );

            if (response.isConfirmed()) {
                booking.setStatus(BookingStatus.CONFIRMED);
            } else {
                booking.setStatus(BookingStatus.CANCELLED);
            }

        } catch (Exception ex) {
            booking.setStatus(BookingStatus.CANCELLED);
            hotelClient.releaseRoom(booking.getRoomId(), booking.getStartDate(), booking.getEndDate());
        }

        return bookingRepository.save(booking);
    }

    private Long selectRoom(BookingRequest request) {
        List<RoomDto> rooms = hotelClient.getRecommendedRooms(
                request.getStartDate(), request.getEndDate()
        );
        if (rooms.isEmpty()) {
            return null;
        }
        return rooms.get(0).getId();
    }

    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(Booking::toResponse)
                .toList();
    }

    @Transactional
    public void cancelBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        hotelClient.releaseRoom(booking.getRoomId(), booking.getStartDate(), booking.getEndDate());
    }

    public BookingResponse getBooking(Long userId, Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }
        return booking.toResponse();
    }
}
