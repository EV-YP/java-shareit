package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    @Query("SELECT b.item.id, MIN(b.start) " +
            "FROM Booking b " +
            "WHERE b.item.id IN ?1 AND b.status = ?2 AND b.start > ?3 " +
            "GROUP BY b.item.id")
    List<Object[]> findNextBookingsStartDates(List<Long> itemIds, BookingStatus status, Instant now);

    @Query("SELECT b.item.id, MAX(b.end) " +
            "FROM Booking b " +
            "WHERE b.item.id IN ?1 AND b.status = ?2 AND b.end < ?3 " +
            "GROUP BY b.item.id")
    List<Object[]> findLastBookingsEndDates(List<Long> itemIds, BookingStatus status, Instant now);

    default Map<Long, Instant> findNextBookingStarts(List<Long> itemIds, BookingStatus status, Instant now) {
        return findNextBookingsStartDates(itemIds, status, now).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Instant) row[1]
                ));
    }

    default Map<Long, Instant> findLastBookingEnds(List<Long> itemIds, BookingStatus status, Instant now) {
        return findLastBookingsEndDates(itemIds, status, now).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Instant) row[1]
                ));
    }

    @Query("SELECT b.start, MIN(b.start) " +
            "FROM Booking b " +
            "WHERE b.item.id = ?1 AND b.status = ?2 AND b.start > ?3 " +
            "GROUP BY b.start")
    Instant findNextBookingStartDate(Long itemId, BookingStatus bookingStatus, Instant now);

    @Query("SELECT b.end, MAX(b.end) " +
            "FROM Booking b " +
            "WHERE b.item.id = ?1 AND b.status = ?2 AND b.end < ?3 " +
            "GROUP BY b.end")
    Instant findLastBookingEndDate(Long itemId, BookingStatus bookingStatus, Instant now);

    boolean existsByItemIdAndBookerIdAndStatusAndEndBefore(Long itemId, Long bookerId, BookingStatus status, Instant now);
}