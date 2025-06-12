package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.exception.CustomValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.item.ItemRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public BookingDto createBooking(Long bookerId, BookingRequestDto bookingRequestDto) {
        getUserOrThrow(bookerId);
        Item item = getItemOrThrow(bookingRequestDto.getItemId());
        if (!item.getAvailable()) {
            throw new CustomValidationException("Предмет недоступен для бронирования");
        }
        Booking booking = BookingMapper.toBooking(bookingRequestDto);
        booking.setBooker(getUserOrThrow(bookerId));
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long userId, Long bookingId, boolean approved) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomValidationException("Пользователь не найден"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalStateException("Бронирование уже обработано");
        } else if (booking.getStart().isBefore(Instant.now())) {
            throw new CustomValidationException("Нельзя подтвердить бронирование, которое уже началось");
        }

        Item item = booking.getItem();
        if (!item.getOwner().getId().equals(userId)) {
            throw new CustomValidationException("Пользователь не является владельцем предмета");
        } else if (!item.getAvailable()) {
            throw new CustomValidationException("Предмет недоступен для бронирования");
        } else if (item.getOwner().getId().equals(booking.getBooker().getId())) {
            throw new CustomValidationException("Нельзя подтвердить бронирование для самого себя");
        } else if (!approved) {
            booking.setStatus(BookingStatus.REJECTED);
        } else {
            booking.setStatus(BookingStatus.APPROVED);
            item.setAvailable(false);
        }

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        userService.getUser(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getBooker().getId().equals(userId) && itemService.getItemByIdAndOwnerId(booking.getItem().getId(), userId) == null) {
            throw new NotFoundException("Бронирование не найдено для данного пользователя");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByUserId(Long userId, String state) {
        userService.getUser(userId);
        if ("ALL".equals(state)) {
            return bookingRepository.findByBookerIdOrderByStartDesc(userId).stream()
                    .map(BookingMapper::toBookingDto)
                    .toList();
        } else {
            BookingStatus status = getBookingStatusFromBookingState(state);
            return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, status).stream()
                    .map(BookingMapper::toBookingDto)
                    .toList();
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByItemOwnerId(Long ownerId, String state) {
        userService.getUser(ownerId);
        if ("ALL".equals(state)) {
            return mapBookingsToDto(bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId));
        } else {
            BookingStatus status = getBookingStatusFromBookingState(state);
            return mapBookingsToDto(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, status));
        }
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Item getItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));
    }

    private List<BookingDto> mapBookingsToDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    private static BookingStatus getBookingStatusFromBookingState(String state) {
        BookingState bookingState = BookingState.from(state);
        return switch (bookingState) {
            case ALL -> null;
            case CURRENT, FUTURE -> BookingStatus.APPROVED;
            case PAST -> BookingStatus.CANCELED;
            case WAITING -> BookingStatus.WAITING;
            case REJECTED -> BookingStatus.REJECTED;
        };
    }
}