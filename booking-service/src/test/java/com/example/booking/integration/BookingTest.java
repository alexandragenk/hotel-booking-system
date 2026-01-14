package com.example.booking.integration;

import com.example.booking.dto.*;
import com.example.booking.entity.BookingStatus;
import com.example.booking.feign.HotelClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@AutoConfigureRestTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingTest {
    @Autowired private RestTestClient restClient;

    @MockitoBean
    HotelClient hotelClient;

    private int userId = 0;

    @BeforeAll
    void baseSetUp() {
        when(hotelClient.getRecommendedRooms(any(), any()))
                .thenReturn(List.of(
                        new RoomDto(0L, 0L, "", true, 0)
                ));

        when(hotelClient.confirmAvailability(eq(0L), any(), any()))
                .thenReturn(new AvailabilityResponse(true));

        when(hotelClient.confirmAvailability(eq(1L), any(), any()))
                .thenReturn(new AvailabilityResponse(false));
    }

    private String registerUser() {
        return restClient
                .post()
                .uri("/api/user/register")
                .body(new AuthRequest("user" + userId++, "pass"))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(AuthResponse.class)
                .returnResult()
                .getResponseBody()
                .getToken();
    }

    @Test
    void testConfirmed() {
        testBooking(null, true, BookingStatus.CONFIRMED);
    }

    @Test
    void testCancelled() {
        testBooking(1L, false, BookingStatus.CANCELLED);
    }

    private void testBooking(Long room, boolean autoSelect, BookingStatus status) {
        String token = registerUser();
        BookingResponse booking = restClient
                .post()
                .uri("/api/booking")
                .headers(h -> h.setBearerAuth(token))
                .body(new BookingRequest(room,
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusDays(2),
                        autoSelect)
                )
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(BookingResponse.class)
                .returnResult()
                .getResponseBody();
        assertThat(booking.getStatus()).isEqualTo(status);

        BookingResponse[] bookings = restClient
                .get()
                .uri("/api/bookings")
                .headers(h -> h.setBearerAuth(token))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(BookingResponse[].class)
                .returnResult()
                .getResponseBody();
        assertThat(bookings).isEqualTo(new BookingResponse[]{booking});
    }
}