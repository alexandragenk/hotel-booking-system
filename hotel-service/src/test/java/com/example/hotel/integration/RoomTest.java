package com.example.hotel.integration;

import com.example.hotel.dto.RoomAvailabilityResponse;
import com.example.hotel.dto.RoomDto;
import com.example.hotel.entity.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureRestTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomTest {
    @Autowired
    private RestTestClient restClient;

    private static final String adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlkIjoxLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3Njg0MTkwMzgsImV4cCI6MzkxNTkwMjY4NX0.VAYh40OKPGjvTu6L0oP7YtaR2EfvhsyjyolYQkrDaDY";
    private static final String userToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMCIsImlkIjoxLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc2ODQxODMzNywiZXhwIjozOTE1OTAxOTg0fQ.rQW2zdcwhMAyOOVRvSiCmyGSclNnf3Ek6LyXE9Lvw_s";

    @Test
    void test() {
        Hotel hotel = restClient
                .post()
                .uri("/api/hotels")
                .headers(h -> h.setBearerAuth(adminToken))
                .body(new Hotel(null, "foo", "bar"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Hotel.class)
                .returnResult()
                .getResponseBody();
        assertThat(hotel).isNotNull();

        RoomDto room = restClient
                .post()
                .uri("/api/rooms")
                .headers(h -> h.setBearerAuth(adminToken))
                .body(new RoomDto(null, hotel.getId(), "baz", null))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RoomDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(room).isNotNull();

        RoomDto[] recommend = restClient
                .get()
                .uri("/api/rooms/recommend?startDate=2026-01-15&endDate=2026-01-20")
                .headers(h -> h.setBearerAuth(userToken))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RoomDto[].class)
                .returnResult()
                .getResponseBody();
        assertThat(recommend).isEqualTo(new RoomDto[]{ room });

        RoomAvailabilityResponse resp = restClient
                .post()
                .uri("/api/rooms/%d/confirm-availability?startDate=2026-01-15&endDate=2026-01-20".formatted(room.getId()))
                .headers(h -> h.setBearerAuth(userToken))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(RoomAvailabilityResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(resp.isConfirmed()).isTrue();
    }
}