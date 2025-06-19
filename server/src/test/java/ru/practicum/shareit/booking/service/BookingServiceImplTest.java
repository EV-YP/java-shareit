package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exception.CustomValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private User owner;
    private Item item;
    private Booking booking;
    private BookingRequestDto bookingRequestDto;
    private final Long userId = 1L;
    private final Long ownerId = 2L;
    private final Long itemId = 1L;
    private final Long bookingId = 1L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("user@example.com");

        owner = new User();
        owner.setId(ownerId);
        owner.setName("Owner User");
        owner.setEmail("owner@example.com");

        item = new Item();
        item.setId(itemId);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);

        LocalDateTime startDateTime = LocalDateTime.now().plusSeconds(3600);
        LocalDateTime endDateTime = LocalDateTime.now().plusSeconds(7200);

        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemId);
        bookingRequestDto.setStart(startDateTime);
        bookingRequestDto.setEnd(endDateTime);

        booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user);
        booking.setItem(item);

        booking.setStart(startDateTime.atZone(ZoneOffset.UTC).toInstant());
        booking.setEnd(endDateTime.atZone(ZoneOffset.UTC).toInstant());
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    void createBooking_whenValidBooking_thenBookingSaved() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.createBooking(userId, bookingRequestDto);

        assertNotNull(result);
        assertEquals(bookingId, result.getId());
        assertEquals(userId, result.getBooker().getId());
        assertEquals(itemId, result.getItem().getId());
        assertEquals(BookingStatus.WAITING, result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBooking_whenInvalidDates_thenValidationExceptionThrown() {
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);

        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemId);
        bookingRequestDto.setStart(startDateTime);
        bookingRequestDto.setEnd(endDateTime);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        doThrow(new CustomValidationException("End date must be after start date"))
                .when(bookingRepository).save(any(Booking.class));

        assertThrows(CustomValidationException.class, () -> bookingService.createBooking(userId, bookingRequestDto));
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBooking_whenItemUnavailable_thenValidationExceptionThrown() {
        item.setAvailable(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(CustomValidationException.class, () -> bookingService.createBooking(userId, bookingRequestDto));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_whenUserNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(userId, bookingRequestDto));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_whenItemNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(userId, bookingRequestDto));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveBooking_whenOwnerApproves_thenBookingStatusApproved() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.approveBooking(ownerId, bookingId, true);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void approveBooking_whenOwnerRejects_thenBookingStatusRejected() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.approveBooking(ownerId, bookingId, false);

        assertNotNull(result);
        assertEquals(BookingStatus.REJECTED, result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void approveBooking_whenNotOwner_thenCustomValidationExceptionThrown() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(CustomValidationException.class, () -> bookingService.approveBooking(userId, bookingId, true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveBooking_whenAlreadyApproved_thenIllegalStateExceptionThrown() {
        booking.setStatus(BookingStatus.APPROVED);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(IllegalStateException.class, () -> bookingService.approveBooking(ownerId, bookingId, true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveBooking_whenBookingAlreadyStarted_thenCustomValidationExceptionThrown() {
        booking.setStart(Instant.now().minusSeconds(3600));
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(CustomValidationException.class, () -> bookingService.approveBooking(ownerId, bookingId, true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveBooking_whenItemUnavailable_thenCustomValidationExceptionThrown() {
        item.setAvailable(false);
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(CustomValidationException.class, () -> bookingService.approveBooking(ownerId, bookingId, true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveBooking_whenBookingForOwnItem_thenCustomValidationExceptionThrown() {
        item.setOwner(user);
        booking.setItem(item);
        booking.setBooker(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(CustomValidationException.class, () -> bookingService.approveBooking(userId, bookingId, true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approveBooking_whenUserNotFound_thenCustomValidationExceptionThrown() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> bookingService.approveBooking(ownerId, bookingId, true));
        verify(bookingRepository, never()).findById(anyLong());
    }

    @Test
    void approveBooking_whenBookingNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.approveBooking(ownerId, bookingId, true));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void getBookingById_whenBookingExists_thenReturnBooking() {
        when(userService.getUser(userId)).thenReturn(null);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.getBookingById(userId, bookingId);

        assertNotNull(result);
        assertEquals(bookingId, result.getId());
        assertEquals(userId, result.getBooker().getId());
        assertEquals(itemId, result.getItem().getId());
    }

    @Test
    void getBookingById_whenOwnerAccessesBooking_thenReturnBooking() {
        when(userService.getUser(ownerId)).thenReturn(null);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemService.getItemByIdAndOwnerId(itemId, ownerId)).thenReturn(new ItemDto());

        BookingDto result = bookingService.getBookingById(ownerId, bookingId);

        assertNotNull(result);
        assertEquals(bookingId, result.getId());
    }

    @Test
    void getBookingById_whenUnauthorizedAccess_thenNotFoundExceptionThrown() {
        Long unauthorizedUserId = 999L;
        when(userService.getUser(unauthorizedUserId)).thenReturn(null);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(itemService.getItemByIdAndOwnerId(itemId, unauthorizedUserId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(unauthorizedUserId, bookingId));
    }

    @Test
    void getBookingById_whenBookingNotFound_thenNotFoundExceptionThrown() {
        when(userService.getUser(userId)).thenReturn(null);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(userId, 999L));
    }

    @Test
    void getAllBookingsByUserId_whenStateAll_thenReturnAllBookings() {
        when(userService.getUser(userId)).thenReturn(null); // Just to satisfy method call
        when(bookingRepository.findByBookerIdOrderByStartDesc(userId)).thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsByUserId(userId, BookingState.ALL.name());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookingId, result.getFirst().getId());
    }

    @Test
    void getAllBookingsByUserId_whenStateWaiting_thenReturnWaitingBookings() {
        when(userService.getUser(userId)).thenReturn(null);
        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING))
            .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsByUserId(userId, BookingState.WAITING.name());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BookingStatus.WAITING, result.getFirst().getStatus());
    }

    @Test
    void getAllBookingsByUserId_whenStateRejected_thenReturnRejectedBookings() {
        booking.setStatus(BookingStatus.REJECTED);
        when(userService.getUser(userId)).thenReturn(null);
        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED))
            .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsByUserId(userId, BookingState.REJECTED.name());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BookingStatus.REJECTED, result.getFirst().getStatus());
    }

    @Test
    void getAllBookingsByItemOwnerId_whenStateAll_thenReturnAllOwnerBookings() {
        when(userService.getUser(ownerId)).thenReturn(null);
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId)).thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsByItemOwnerId(ownerId, BookingState.ALL.name());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookingId, result.getFirst().getId());
    }

    @Test
    void getAllBookingsByItemOwnerId_whenStateApproved_thenReturnApprovedOwnerBookings() {
        booking.setStatus(BookingStatus.APPROVED);
        when(userService.getUser(ownerId)).thenReturn(null);
        when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.APPROVED))
            .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsByItemOwnerId(ownerId, BookingState.FUTURE.name());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BookingStatus.APPROVED, result.getFirst().getStatus());
    }

    @Test
    void getAllBookingsByItemOwnerId_whenStateWaiting_thenReturnWaitingOwnerBookings() {
        when(userService.getUser(ownerId)).thenReturn(null);
        when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING))
            .thenReturn(List.of(booking));

        List<BookingDto> result = bookingService.getAllBookingsByItemOwnerId(ownerId, BookingState.WAITING.name());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BookingStatus.WAITING, result.getFirst().getStatus());
    }
}
