package com.example.hotel.entity;

import com.example.hotel.dto.RoomDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;

    private String number;
    private long timesBooked;

    public RoomDto toDto() {
        return new RoomDto(id, hotel.getId(), number, timesBooked);
    }
}
