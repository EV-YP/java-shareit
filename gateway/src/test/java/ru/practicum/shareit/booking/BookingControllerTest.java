package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.GlobalExceptionHandler;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BookingController.class, GlobalExceptionHandler.class})
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private BookingRequestDto bookingRequestDto;
    private final long userId = 1L;
    private final long bookingId = 1L;

    @BeforeEach
    void setUp() {
        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.now().plusHours(1));
        bookingRequestDto.setEnd(LocalDateTime.now().plusHours(2));
    }

    @Test
    void getBookings_whenValidState_thenStatus200() throws Exception {
        when(bookingClient.getBookings(anyLong(), any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", userId)
                .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookings_whenInvalidState_thenStatus400() throws Exception {
        // Mock the client to throw an exception when called with an invalid state
        when(bookingClient.getBookings(anyLong(), any(BookingState.class)))
                .thenThrow(new IllegalArgumentException("Unknown state: INVALID_STATE"));

        mockMvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", userId)
                .param("state", "INVALID_STATE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bookItem_whenValidBookingRequest_thenStatus200() throws Exception {
        when(bookingClient.bookItem(anyLong(), any(BookingRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void bookItem_whenStartDateInPast_thenStatus400() throws Exception {
        // Create a booking with start date in the past
        BookingRequestDto pastBookingDto = new BookingRequestDto();
        pastBookingDto.setItemId(1L);
        pastBookingDto.setStart(LocalDateTime.now().minusDays(1)); // Explicitly in the past
        pastBookingDto.setEnd(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pastBookingDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bookItem_whenEndDateBeforeStartDate_thenStatus400() throws Exception {
        // Create a booking with end before start
        BookingRequestDto invalidBookingDto = new BookingRequestDto();
        invalidBookingDto.setItemId(1L);
        invalidBookingDto.setStart(LocalDateTime.now().plusDays(2));
        invalidBookingDto.setEnd(LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBookingDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approveBooking_whenValidRequest_thenStatus200() throws Exception {
        when(bookingClient.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                .header("X-Sharer-User-Id", userId)
                .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking_whenValidRequest_thenStatus200() throws Exception {
        when(bookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_whenValidState_thenStatus200() throws Exception {
        when(bookingClient.getBookingsByOwner(anyLong(), any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/bookings/owner")
                .header("X-Sharer-User-Id", userId)
                .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_whenInvalidState_thenStatus400() throws Exception {
        // Mock the client to throw an exception when called with an invalid state
        when(bookingClient.getBookingsByOwner(anyLong(), any(BookingState.class)))
                .thenThrow(new IllegalArgumentException("Unknown state: INVALID_STATE"));

        mockMvc.perform(get("/bookings/owner")
                .header("X-Sharer-User-Id", userId)
                .param("state", "INVALID_STATE"))
                .andExpect(status().isBadRequest());
    }
}
