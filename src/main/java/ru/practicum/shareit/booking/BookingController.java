package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                    @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        BookingDto createdBookingDto = bookingService.createBooking(bookerId, bookingRequestDto);
        log.info("Бронирование создано: {}", createdBookingDto);
        return createdBookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking (@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId,
                                  @RequestParam Boolean approved) {
        BookingDto approvedBookingDto = bookingService.approveBooking(userId, bookingId, approved);
        log.info("Бронирование с id: {} {}", bookingId, approved ? "одобрено" : "отклонено");
        return approvedBookingDto;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        BookingDto bookingDto = bookingService.getBookingById(userId, bookingId);
        log.info("Бронирование с id: {} найдено", bookingId);
        return bookingDto;
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDto> bookingsDto = bookingService.getAllBookingsByUserId(userId, state);
        log.info("Найдено {} бронирований пользователя с id: {}", bookingsDto.size(), userId);
        return bookingsDto;
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsByItemOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDto> bookingsDto = bookingService.getAllBookingsByItemOwnerId(ownerId, state);
        log.info("Найдено {} бронирований предметов владельца с id: {}", bookingsDto.size(), ownerId);
        return bookingsDto;
    }
}