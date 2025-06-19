package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingRepositoryDefaultMethodsTest {

    @Test
    void findNextAndLastBookingDates_shouldConvertListToMap() {
        BookingRepository repository = Mockito.mock(BookingRepository.class, Mockito.CALLS_REAL_METHODS);

        Instant now = Instant.now();
        List<Object[]> next = List.of(new Object[]{1L, now.plusSeconds(1)}, new Object[]{2L, now.plusSeconds(2)});
        List<Object[]> last = List.of(new Object[]{1L, now.minusSeconds(1)}, new Object[]{2L, now.minusSeconds(2)});

        when(repository.findNextBookingsStartDates(anyList(), any(), any())).thenReturn(next);
        when(repository.findLastBookingsEndDates(anyList(), any(), any())).thenReturn(last);

        Map<Long, Instant> nextMap = repository.findNextBookingStarts(List.of(1L, 2L), BookingStatus.APPROVED, now);
        Map<Long, Instant> lastMap = repository.findLastBookingEnds(List.of(1L, 2L), BookingStatus.APPROVED, now);

        assertEquals(now.plusSeconds(1), nextMap.get(1L));
        assertEquals(now.plusSeconds(2), nextMap.get(2L));
        assertEquals(now.minusSeconds(1), lastMap.get(1L));
        assertEquals(now.minusSeconds(2), lastMap.get(2L));

        verify(repository).findNextBookingsStartDates(anyList(), any(), any());
        verify(repository).findLastBookingsEndDates(anyList(), any(), any());
    }
}