package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStart(LocalDateTime.ofInstant(booking.getStart(), ZoneId.systemDefault()));
        dto.setEnd(LocalDateTime.ofInstant(booking.getEnd(), ZoneId.systemDefault()));
        dto.setStatus(booking.getStatus());
        dto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        dto.setItem(ItemMapper.toItemDto(booking.getItem()));
        return dto;
    }

    public static Booking toBooking(BookingRequestDto dto) {
        if (dto == null) return null;
        Booking booking = new Booking();
        booking.setStart(dto.getStart().atZone(ZoneId.systemDefault()).toInstant());
        booking.setEnd(dto.getEnd().atZone(ZoneId.systemDefault()).toInstant());
        booking.setItem(new Item(dto.getItemId(), null, null, null, null));
        return booking;
    }
}