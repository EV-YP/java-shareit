package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        BookingRequestDto dto = new BookingRequestDto();

        assertNull(dto.getItemId());
        assertNull(dto.getStart());
        assertNull(dto.getEnd());
    }

    @Test
    void testAllArgsConstructor() {
        Long itemId = 1L;
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 10, 12, 0);

        BookingRequestDto dto = new BookingRequestDto(itemId, start, end);

        assertEquals(itemId, dto.getItemId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
    }

    @Test
    void testSettersAndGetters() {
        BookingRequestDto dto = new BookingRequestDto();

        Long itemId = 1L;
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 10, 12, 0);

        dto.setItemId(itemId);
        dto.setStart(start);
        dto.setEnd(end);

        assertEquals(itemId, dto.getItemId());
        assertEquals(start, dto.getStart());
        assertEquals(end, dto.getEnd());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 10, 12, 0);

        BookingRequestDto dto1 = new BookingRequestDto(1L, start, end);
        BookingRequestDto dto2 = new BookingRequestDto(1L, start, end);
        BookingRequestDto dto3 = new BookingRequestDto(2L, start, end);

        // Проверка equals()
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);

        // Проверка hashCode()
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 10, 12, 0);

        BookingRequestDto dto = new BookingRequestDto(1L, start, end);

        String toStringResult = dto.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("itemId=1"));
        assertTrue(toStringResult.contains(start.toString()));
        assertTrue(toStringResult.contains(end.toString()));
    }
}
