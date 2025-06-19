package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private static final ZoneId CLIENT_ZONE = ZoneId.systemDefault();

    @Test
    void toBookingDto_whenValidBooking_thenCorrectDto() {

        User booker = new User(1L, "Booker", "booker@example.com");

        Item item = new Item(1L, "Test Item", "Description", true, booker,null);

        Instant start = Instant.now().plusSeconds(3600);
        Instant end = Instant.now().plusSeconds(7200);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        BookingDto result = BookingMapper.toBookingDto(booking);

        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.ofInstant(start, CLIENT_ZONE), result.getStart());
        assertEquals(LocalDateTime.ofInstant(end, CLIENT_ZONE), result.getEnd());
        assertEquals(BookingStatus.WAITING, result.getStatus());
        assertNotNull(result.getItem());
        assertEquals(1L, result.getItem().getId());
        assertNotNull(result.getBooker());
        assertEquals(1L, result.getBooker().getId());
    }

    @Test
    void toBooking_whenValidRequestDto_thenCorrectBooking() {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        BookingRequestDto dto = new BookingRequestDto();
        dto.setItemId(1L);
        dto.setStart(start);
        dto.setEnd(end);

        Booking result = BookingMapper.toBooking(dto);

        assertEquals(start.atZone(CLIENT_ZONE).toInstant(), result.getStart());
        assertEquals(end.atZone(CLIENT_ZONE).toInstant(), result.getEnd());
        assertNotNull(result.getItem());
        assertEquals(1L, result.getItem().getId());
        assertNull(result.getBooker());
        assertNull(result.getStatus());
    }

    @Test
    void toBookingDto_whenNullBooking_thenReturnsNull() {
        BookingDto result = BookingMapper.toBookingDto(null);

        assertNull(result);
    }

    @Test
    void toBooking_whenNullDto_thenReturnsNull() {
        Booking result = BookingMapper.toBooking(null);

        assertNull(result);
    }
}
