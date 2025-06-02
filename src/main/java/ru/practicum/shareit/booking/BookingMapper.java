package ru.practicum.shareit.booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;
        BookingDto dto = new BookingDto();
        dto.setBookerId(booking.getBookerId());
        dto.setItemId(booking.getItemId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public static Booking toBooking(BookingDto dto) {
        if (dto == null) return null;
        Booking booking = new Booking();
        booking.setBookerId(dto.getBookerId());
        booking.setItemId(dto.getItemId());
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setStatus(dto.getStatus());
        return booking;
    }
}