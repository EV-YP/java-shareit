package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BookingStateTest {

    @Test
    void from_ValidState_ReturnsCorrectEnum() {
        assertEquals(BookingState.ALL, BookingState.from("ALL"));
        assertEquals(BookingState.CURRENT, BookingState.from("CURRENT"));
        assertEquals(BookingState.PAST, BookingState.from("PAST"));
        assertEquals(BookingState.FUTURE, BookingState.from("FUTURE"));
        assertEquals(BookingState.WAITING, BookingState.from("WAITING"));
        assertEquals(BookingState.REJECTED, BookingState.from("REJECTED"));
    }

    @Test
    void from_LowercaseState_ReturnsCorrectEnum() {
        assertEquals(BookingState.ALL, BookingState.from("all"));
        assertEquals(BookingState.CURRENT, BookingState.from("current"));
        assertEquals(BookingState.WAITING, BookingState.from("waiting"));
    }

    @Test
    void from_MixedCaseState_ReturnsCorrectEnum() {
        assertEquals(BookingState.FUTURE, BookingState.from("Future"));
        assertEquals(BookingState.REJECTED, BookingState.from("ReJeCtEd"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"UNKNOWN", "INVALID", "", "COMPLETED"})
    void from_InvalidState_ThrowsIllegalArgumentException(String invalidState) {
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> BookingState.from(invalidState));
        assertTrue(exception.getMessage().contains("Неверно указан параметр state: " + invalidState));
    }
}
