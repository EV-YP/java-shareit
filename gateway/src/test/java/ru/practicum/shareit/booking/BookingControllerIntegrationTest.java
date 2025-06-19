package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class BookingControllerIntegrationTest {

    @Autowired
    private BookingController bookingController;

    @MockBean
    private BookingClient bookingClient;

    @Test
    void getBookings_whenStateIsValid_thenBookingsReturned() {
        // Arrange
        long userId = 1L;
        String stateParam = "ALL";
        BookingState state = BookingState.from(stateParam).get();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(List.of());
        when(bookingClient.getBookings(anyLong(), any(BookingState.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Object> response = bookingController.getBookings(userId, stateParam);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(bookingClient, times(1)).getBookings(eq(userId), eq(state));
    }

    @Test
    void bookItem_whenValidBookingData_thenBookingCreated() {

        long userId = 1L;
        BookingRequestDto bookingRequestDto = new BookingRequestDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(bookingRequestDto);
        when(bookingClient.bookItem(anyLong(), any(BookingRequestDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.bookItem(userId, bookingRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(bookingClient, times(1)).bookItem(eq(userId), any(BookingRequestDto.class));
    }

    @Test
    void approveBooking_whenValidData_thenBookingApproved() {
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(bookingClient.approveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.approveBooking(userId, bookingId, approved);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingClient, times(1)).approveBooking(eq(userId), eq(bookingId), eq(approved));
    }

    @Test
    void getBooking_whenBookingExists_thenBookingReturned() {

        long userId = 1L;
        long bookingId = 1L;

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getBooking(userId, bookingId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingClient, times(1)).getBooking(eq(userId), eq(bookingId));
    }

    @Test
    void getBookingsByOwner_whenValidData_thenBookingsReturned() {

        long ownerId = 1L;
        String stateParam = "ALL";
        BookingState state = BookingState.from(stateParam).get();

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(List.of());
        when(bookingClient.getBookingsByOwner(anyLong(), any(BookingState.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getBookingsByOwner(ownerId, stateParam);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(bookingClient, times(1)).getBookingsByOwner(eq(ownerId), eq(state));
    }

    @Test
    void getBookingsByOwner_whenMultipleStates_thenCorrectStateProcessed() {
        BookingState[] states = BookingState.values();
        for (BookingState state : states) {

            long ownerId = 1L;
            String stateParam = state.name();

            ResponseEntity<Object> expectedResponse = ResponseEntity.ok(List.of());
            when(bookingClient.getBookingsByOwner(anyLong(), eq(state))).thenReturn(expectedResponse);

            ResponseEntity<Object> response = bookingController.getBookingsByOwner(ownerId, stateParam);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(bookingClient, times(1)).getBookingsByOwner(eq(ownerId), eq(state));

            reset(bookingClient);
        }
    }
}
