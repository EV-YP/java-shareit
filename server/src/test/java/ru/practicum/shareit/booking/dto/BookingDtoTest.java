package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingDtoTest {

    @Test
    void testNoArgsConstructor() {
        BookingDto bookingDto = new BookingDto();

        assertNull(bookingDto.getId());
        assertNull(bookingDto.getStart());
        assertNull(bookingDto.getEnd());
        assertNull(bookingDto.getStatus());
        assertNull(bookingDto.getBooker());
        assertNull(bookingDto.getItem());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 10, 12, 0);
        BookingStatus status = BookingStatus.APPROVED;
        UserDto booker = new UserDto(2L, "User Name", "user@example.com");
        ItemDto item = new ItemDto(3L, "Item Name", "Item Description", true, null, null);

        BookingDto bookingDto = new BookingDto(id, start, end, status, booker, item);

        assertEquals(id, bookingDto.getId());
        assertEquals(start, bookingDto.getStart());
        assertEquals(end, bookingDto.getEnd());
        assertEquals(status, bookingDto.getStatus());
        assertEquals(booker, bookingDto.getBooker());
        assertEquals(item, bookingDto.getItem());
    }

    @Test
    void testSettersAndGetters() {
        BookingDto bookingDto = new BookingDto();

        Long id = 1L;
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 10, 12, 0);
        BookingStatus status = BookingStatus.WAITING;
        UserDto booker = new UserDto(2L, "User Name", "user@example.com");
        ItemDto item = new ItemDto(3L, "Item Name", "Item Description", true, null, null);

        bookingDto.setId(id);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setStatus(status);
        bookingDto.setBooker(booker);
        bookingDto.setItem(item);

        assertEquals(id, bookingDto.getId());
        assertEquals(start, bookingDto.getStart());
        assertEquals(end, bookingDto.getEnd());
        assertEquals(status, bookingDto.getStatus());
        assertEquals(booker, bookingDto.getBooker());
        assertEquals(item, bookingDto.getItem());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 10, 12, 0);
        UserDto booker = new UserDto(2L, "User Name", "user@example.com");
        ItemDto item = new ItemDto(3L, "Item Name", "Item Description", true, null, null);

        BookingDto bookingDto1 = new BookingDto(1L, start, end, BookingStatus.WAITING, booker, item);
        BookingDto bookingDto2 = new BookingDto(1L, start, end, BookingStatus.WAITING, booker, item);
        BookingDto bookingDto3 = new BookingDto(2L, start, end, BookingStatus.WAITING, booker, item);

        // Проверка equals()
        assertEquals(bookingDto1, bookingDto2);
        assertNotEquals(bookingDto1, bookingDto3);

        // Проверка hashCode()
        assertEquals(bookingDto1.hashCode(), bookingDto2.hashCode());
        assertNotEquals(bookingDto1.hashCode(), bookingDto3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 10, 12, 0);
        UserDto booker = new UserDto(2L, "User Name", "user@example.com");
        ItemDto item = new ItemDto(3L, "Item Name", "Item Description", true, null, null);

        BookingDto bookingDto = new BookingDto(1L, start, end, BookingStatus.WAITING, booker, item);

        String toStringResult = bookingDto.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("status=WAITING"));
    }
}