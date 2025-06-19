package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookingStatusTest {

    @Test
    void ensureEnumValuesExist() {
        assertNotNull(BookingStatus.WAITING);
        assertNotNull(BookingStatus.APPROVED);
        assertNotNull(BookingStatus.REJECTED);
        assertNotNull(BookingStatus.CANCELED);
    }

    @Test
    void testEnumValues() {
        BookingStatus[] statuses = BookingStatus.values();
        assertEquals(4, statuses.length);
        assertEquals(BookingStatus.WAITING, statuses[0]);
        assertEquals(BookingStatus.APPROVED, statuses[1]);
        assertEquals(BookingStatus.REJECTED, statuses[2]);
        assertEquals(BookingStatus.CANCELED, statuses[3]);
    }

    @Test
    void testEnumValueOf() {
        assertEquals(BookingStatus.WAITING, BookingStatus.valueOf("WAITING"));
        assertEquals(BookingStatus.APPROVED, BookingStatus.valueOf("APPROVED"));
        assertEquals(BookingStatus.REJECTED, BookingStatus.valueOf("REJECTED"));
        assertEquals(BookingStatus.CANCELED, BookingStatus.valueOf("CANCELED"));
    }

    @Test
    void testInvalidValueOf() {
        assertThrows(IllegalArgumentException.class, () -> BookingStatus.valueOf("INVALID"));
    }
}
