package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class BookingMapper {
    private static final ZoneId CLIENT_ZONE = ZoneId.systemDefault();

    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(LocalDateTime.ofInstant(booking.getStart(), CLIENT_ZONE));
        dto.setEnd(LocalDateTime.ofInstant(booking.getEnd(), CLIENT_ZONE));
        dto.setStatus(booking.getStatus());
        dto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        dto.setItem(ItemMapper.toItemDto(booking.getItem()));
        return dto;
    }

    public static Booking toBooking(BookingRequestDto dto) {
        if (dto == null) return null;
        Booking booking = new Booking();
        booking.setStart(dto.getStart().atZone(CLIENT_ZONE).toInstant());
        booking.setEnd(dto.getEnd().atZone(CLIENT_ZONE).toInstant());
        booking.setItem(new Item(dto.getItemId(), null, null, null, null, null));
        return booking;
    }
}