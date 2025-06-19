package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.CustomValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDatesDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User owner;
    private Item item;
    private ItemDto itemDto;
    private final Long userId = 1L;
    private final Long itemId = 1L;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(userId);
        owner.setName("Owner Name");
        owner.setEmail("owner@example.com");

        item = new Item();
        item.setId(itemId);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);

        itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
    }

    @Test
    void addItem_whenValidItem_thenItemSaved() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.addItem(userId, itemDto);

        assertNotNull(result);
        assertEquals(itemDto.getId(), result.getId());
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void addItem_whenUserNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addItem(999L, itemDto));
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void addItem_whenRequestIdProvided_thenItemSavedWithRequest() {
        itemDto.setRequestId(5L);
        ItemRequest request = new ItemRequest();
        request.setId(5L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(5L)).thenReturn(Optional.of(request));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.addItem(userId, itemDto);

        assertNotNull(result);
        verify(itemRequestRepository, times(1)).findById(5L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void addItem_whenRequestNotFound_thenNotFoundExceptionThrown() {
        itemDto.setRequestId(5L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addItem(userId, itemDto));
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void updateItem_whenValidData_thenItemUpdated() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Name");
        updateDto.setDescription("Updated Description");
        updateDto.setAvailable(false);

        Item updatedItem = new Item();
        updatedItem.setId(itemId);
        updatedItem.setName("Updated Name");
        updatedItem.setDescription("Updated Description");
        updatedItem.setAvailable(false);
        updatedItem.setOwner(owner);

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findByIdAndOwnerId(itemId, userId)).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        ItemDto result = itemService.updateItem(userId, itemId, updateDto);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(updateDto.getName(), result.getName());
        assertEquals(updateDto.getDescription(), result.getDescription());
        assertEquals(updateDto.getAvailable(), result.getAvailable());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItem_whenPartialUpdate_thenOnlyUpdatedFieldsChanged() {
        // Create a DTO with only the name updated
        ItemDto partialUpdateDto = new ItemDto();
        partialUpdateDto.setName("Updated Name");
        // Note: description and available are null

        Item originalItem = new Item();
        originalItem.setId(itemId);
        originalItem.setName("Original Name");
        originalItem.setDescription("Original Description");
        originalItem.setAvailable(true);
        originalItem.setOwner(owner);

        Item updatedItem = new Item();
        updatedItem.setId(itemId);
        updatedItem.setName("Updated Name");  // Only name is updated
        updatedItem.setDescription("Original Description"); // Original description remains
        updatedItem.setAvailable(true); // Original availability remains
        updatedItem.setOwner(owner);

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findByIdAndOwnerId(itemId, userId)).thenReturn(originalItem);
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        ItemDto result = itemService.updateItem(userId, itemId, partialUpdateDto);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Original Description", result.getDescription());
        assertTrue(result.getAvailable());
    }

    @Test
    void updateItem_whenUserNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            itemService.updateItem(userId, itemId, new ItemDto()));

        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void getItemByIdAndUserId_whenItemExists_thenReturnItemWithDates() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findCommentByItemId(itemId)).thenReturn(List.of());

        ItemWithDatesDto result = itemService.getItemByIdAndUserId(itemId, userId);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    void getItemByIdAndUserId_whenOwnerViewsItem_thenShowBookingDates() {
        Instant now = Instant.now();
        Instant nextBooking = now.plusSeconds(3600);
        Instant lastBooking = now.minusSeconds(3600);

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findCommentByItemId(itemId)).thenReturn(List.of());
        when(bookingRepository.findNextBookingStartDate(eq(itemId), eq(BookingStatus.APPROVED), any(Instant.class)))
            .thenReturn(nextBooking);
        when(bookingRepository.findLastBookingEndDate(eq(itemId), eq(BookingStatus.APPROVED), any(Instant.class)))
            .thenReturn(lastBooking);

        ItemWithDatesDto result = itemService.getItemByIdAndUserId(itemId, userId);

        assertNotNull(result);
        assertNotNull(result.getNextBooking());
        assertNotNull(result.getLastBooking());
    }

    @Test
    void getItemByIdAndUserId_whenNotOwner_thenNoBookingDates() {
        User notOwner = new User();
        notOwner.setId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(notOwner));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findCommentByItemId(itemId)).thenReturn(List.of());

        ItemWithDatesDto result = itemService.getItemByIdAndUserId(itemId, 2L);

        assertNotNull(result);
        assertNull(result.getNextBooking());
        assertNull(result.getLastBooking());
    }

    @Test
    void getItemByIdAndUserId_whenItemNotExists_thenNotFoundExceptionThrown() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemByIdAndUserId(999L, userId));
    }

    @Test
    void getItemsByOwner_whenOwnerHasItems_thenReturnItems() {
        List<Item> items = new ArrayList<>();
        items.add(item);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Second Item");
        item2.setDescription("Second Description");
        item2.setAvailable(true);
        item2.setOwner(owner);
        items.add(item2);

        List<Long> itemIds = List.of(1L, 2L);
        Map<Long, Instant> nextBookings = new HashMap<>();
        nextBookings.put(1L, Instant.now().plusSeconds(3600));

        Map<Long, Instant> lastBookings = new HashMap<>();
        lastBookings.put(1L, Instant.now().minusSeconds(3600));

        Map<Long, List<Comment>> comments = new HashMap<>();
        comments.put(1L, new ArrayList<>());
        comments.put(2L, new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findByOwnerId(userId)).thenReturn(items);
        when(bookingRepository.findNextBookingStarts(eq(itemIds), eq(BookingStatus.APPROVED), any(Instant.class)))
                .thenReturn(nextBookings);
        when(bookingRepository.findLastBookingEnds(eq(itemIds), eq(BookingStatus.APPROVED), any(Instant.class)))
            .thenReturn(lastBookings);
        when(commentRepository.findCommentByItemIds(itemIds)).thenReturn(comments);

        List<ItemWithDatesDto> result = itemService.getItemsByOwner(userId);

        assertEquals(2, result.size());
        assertNotNull(result.get(0).getNextBooking());
        assertNotNull(result.get(0).getLastBooking());
        assertNull(result.get(1).getNextBooking());
        assertNull(result.get(1).getLastBooking());
    }

    @Test
    void getItemsByOwner_whenNoItems_thenReturnEmptyList() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findByOwnerId(userId)).thenReturn(Collections.emptyList());

        List<ItemWithDatesDto> result = itemService.getItemsByOwner(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchItems_whenTextProvided_thenReturnMatchingItems() {
        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase("test", "test"))
            .thenReturn(items);

        List<ItemDto> result = itemService.searchItems("test");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(itemId, result.get(0).getId());
    }

    @Test
    void searchItems_whenEmptyText_thenReturnEmptyList() {
        List<ItemDto> result = itemService.searchItems("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemRepository, never())
            .findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase(anyString(), anyString());
    }

    @Test
    void searchItems_whenNullText_thenReturnEmptyList() {
        List<ItemDto> result = itemService.searchItems(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemRepository, never())
            .findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase(anyString(), anyString());
    }

    @Test
    void getItemByIdAndOwnerId_whenItemExists_thenReturnItem() {
        when(itemRepository.findByIdAndOwnerId(itemId, userId)).thenReturn(item);

        ItemDto result = itemService.getItemByIdAndOwnerId(itemId, userId);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(item.getName(), result.getName());
    }

    @Test
    void addComment_whenValidComment_thenCommentSaved() {
        User booker = new User();
        booker.setId(2L);
        booker.setName("Booker Name");

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Great item!");
        comment.setAuthor(booker);
        comment.setItem(item);
        comment.setCreated(Instant.now());

        List<Booking> bookings = new ArrayList<>();
        Booking booking = new Booking();
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        bookings.add(booking);

        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                eq(itemId), eq(2L), eq(BookingStatus.APPROVED), any(Instant.class)))
                .thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = itemService.addComment(2L, itemId, commentDto);

        assertNotNull(result);
        assertEquals(commentDto.getText(), result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addComment_whenUserNotBooker_thenExceptionThrown() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                eq(itemId), eq(2L), eq(BookingStatus.APPROVED), any(Instant.class)))
                .thenReturn(false);

        assertThrows(CustomValidationException.class, () -> itemService.addComment(2L, itemId, commentDto));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void addComment_whenUserNotFound_thenNotFoundExceptionThrown() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addComment(2L, itemId, commentDto));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void addComment_whenItemNotFound_thenNotFoundExceptionThrown() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addComment(2L, itemId, commentDto));
        verify(commentRepository, never()).save(any(Comment.class));
    }
}
