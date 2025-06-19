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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    void getItemByIdAndUserId_whenItemNotExists_thenNotFoundExceptionThrown() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemByIdAndUserId(999L, userId));
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
}
