package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    private UserDto owner;
    private UserDto booker;
    private ItemDto item;
    private BookingRequestDto bookingRequest;

    @BeforeEach
    void setUp() {
        // Create owner user
        UserDto ownerDto = new UserDto();
        ownerDto.setName("Item Owner");
        ownerDto.setEmail("owner@example.com");
        owner = userService.createUser(ownerDto);

        // Create booker user
        UserDto bookerDto = new UserDto();
        bookerDto.setName("Item Booker");
        bookerDto.setEmail("booker@example.com");
        booker = userService.createUser(bookerDto);

        // Create test item
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Integration Test Description");
        itemDto.setAvailable(true);
        item = itemService.addItem(owner.getId(), itemDto);

        bookingRequest = new BookingRequestDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusSeconds(3600));  // 1 hour later
        bookingRequest.setEnd(LocalDateTime.now().plusSeconds(7200));    // 2 hours later
    }

    @Test
    void createBookingAndApprove_whenValidBooking_thenStatusChanged() {
        BookingDto createdBooking = bookingService.createBooking(booker.getId(), bookingRequest);

        assertNotNull(createdBooking);
        assertEquals(BookingStatus.WAITING, createdBooking.getStatus());

        BookingDto approvedBooking = bookingService.approveBooking(owner.getId(), createdBooking.getId(), true);

        assertNotNull(approvedBooking);
        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
        assertEquals(createdBooking.getId(), approvedBooking.getId());
        assertEquals(createdBooking.getBooker().getId(), approvedBooking.getBooker().getId());
        assertEquals(createdBooking.getItem().getId(), approvedBooking.getItem().getId());
    }

    @Test
    void getAllBookingsByUserIdWithDifferentStates_thenCorrectBookingsReturned() {
        BookingDto waitingBooking = bookingService.createBooking(booker.getId(), bookingRequest);
        assertEquals(BookingStatus.WAITING, waitingBooking.getStatus(), "Initial booking should be in WAITING status");

        BookingRequestDto anotherBookingRequest = new BookingRequestDto();
        anotherBookingRequest.setItemId(item.getId());
        anotherBookingRequest.setStart(LocalDateTime.now().plusHours(5));
        anotherBookingRequest.setEnd(LocalDateTime.now().plusHours(6));

        BookingDto anotherBookingDto = bookingService.createBooking(booker.getId(), anotherBookingRequest);
        BookingDto rejectedBooking = bookingService.approveBooking(owner.getId(), anotherBookingDto.getId(), false);
        assertEquals(BookingStatus.REJECTED, rejectedBooking.getStatus(), "Second booking should be REJECTED");

        var allBookings = bookingService.getAllBookingsByUserId(booker.getId(), "ALL");
        assertEquals(2, allBookings.size(), "ALL should contain both bookings");

        var waitingBookings = bookingService.getAllBookingsByUserId(booker.getId(), "WAITING");
        assertEquals(1, waitingBookings.size(), "WAITING should contain 1 booking");
        assertEquals(waitingBooking.getId(), waitingBookings.get(0).getId(), "The booking in WAITING list should be the waiting booking");
        var rejectedBookings = bookingService.getAllBookingsByUserId(booker.getId(), "REJECTED");
        assertEquals(1, rejectedBookings.size(), "REJECTED should contain 1 booking");
        assertEquals(rejectedBooking.getId(), rejectedBookings.get(0).getId(), "The booking in REJECTED list should be the rejected booking");

        var currentBookings = bookingService.getAllBookingsByUserId(booker.getId(), "CURRENT");
        var pastBookings = bookingService.getAllBookingsByUserId(booker.getId(), "PAST");

        for (BookingDto booking : currentBookings) {
            assertNotEquals(waitingBooking.getId(), booking.getId(), "Waiting booking should not be in CURRENT state");
            assertNotEquals(rejectedBooking.getId(), booking.getId(), "Rejected booking should not be in CURRENT state");
        }

        for (BookingDto booking : pastBookings) {
            assertNotEquals(waitingBooking.getId(), booking.getId(), "Waiting booking should not be in PAST state");
            assertNotEquals(rejectedBooking.getId(), booking.getId(), "Rejected booking should not be in PAST state");
        }
    }
}
