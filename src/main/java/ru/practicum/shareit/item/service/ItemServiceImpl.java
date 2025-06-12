package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.CustomValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemWithDatesDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingStatus.APPROVED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id: %d не найден", userId))));
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id: %d не найден", userId)));
        itemRepository.findById(itemId);
        ItemDto oldItem = getItemByIdAndOwnerId(itemId, userId);
        Item update = new Item();
        update.setId(itemId);
        update.setName(itemDto.getName() != null ? itemDto.getName() : oldItem.getName());
        update.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : oldItem.getDescription());
        update.setAvailable(itemDto.getAvailable() != null ? itemDto.getAvailable() : oldItem.getAvailable());
        update.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.save(update));
    }

    @Override
    public ItemDto getItemByIdAndOwnerId(Long itemId, Long userId) {
        return ItemMapper.toItemDto(itemRepository.findByIdAndOwnerId(itemId, userId));
    }

    @Override
    public ItemWithDatesDto getItemByIdAndUserId(Long itemId, Long userId) {
        userRepository.findById(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Предмет с id: %d не найден", itemId)));
        ItemWithDatesDto foundItem = ItemMapper.toItemWithDatesDto(item);
        if (item.getOwner().getId().equals(userId)) {
            foundItem.setNextBooking(getLocalDateTimeOrNull(
                    bookingRepository.findNextBookingStartDate(itemId, APPROVED, Instant.now())));
            foundItem.setLastBooking(getLocalDateTimeOrNull(
                    bookingRepository.findLastBookingEndDate(itemId, APPROVED, Instant.now())));
        }
        foundItem.setComments(commentRepository.findCommentByItemId(itemId)
                .stream().map(CommentMapper::toCommentDto).toList());
        return foundItem;
    }

    @Override
    public List<ItemWithDatesDto> getItemsByOwner(Long ownerId) {
        userRepository.findById(ownerId);
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        if (items.isEmpty()) {
            return List.of();
        }
        List<Long> itemIds = items.stream().map(Item::getId).toList();
        Map<Long, Instant> nextBookingStarts = bookingRepository.findNextBookingStarts(itemIds, APPROVED, Instant.now());
        Map<Long, Instant> lastBookingEnds = bookingRepository.findLastBookingEnds(itemIds, APPROVED, Instant.now());
        Map<Long, List<Comment>> comments = commentRepository.findCommentByItemIds(itemIds);
        return items.stream().map(item -> {
            ItemWithDatesDto dto = ItemMapper.toItemWithDatesDto(item);
            Instant nextInstant = nextBookingStarts.get(item.getId());
            dto.setNextBooking(nextInstant != null ?
                    nextInstant.atZone(ZoneId.systemDefault()).toLocalDateTime() : null);
            Instant lastInstant = lastBookingEnds.get(item.getId());
            dto.setLastBooking(lastInstant != null ?
                    lastInstant.atZone(ZoneId.systemDefault()).toLocalDateTime() : null);
            List<CommentDto> commentsById = comments.getOrDefault(item.getId(), List.of()).stream()
                    .map(CommentMapper::toCommentDto).collect(Collectors.toList());
            dto.setComments(commentsById);
            return dto;
        }).toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndDescriptionContainingIgnoreCase(text, text)
                .stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id: %d не найден", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Предмет с id: %d не найден", itemId)));
        List<Booking> bookings = bookingRepository.findByItemIdAndBookerIdAndStatus(itemId, userId, APPROVED);
        boolean hasPastBooking = bookings.stream().anyMatch(
                booking -> booking.getEnd().isBefore(Instant.now()));
        if (!hasPastBooking) {
            throw new CustomValidationException(String.format(
                    "Пользователь с id: %d не завершил аренду предмета с id: %d", userId, itemId));
        }
        Comment comment = createCommentFromDto(commentDto, user, item);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    private Comment createCommentFromDto(CommentDto commentDto, User user, Item item) {
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(Instant.now());
        return comment;
    }

    private LocalDateTime getLocalDateTimeOrNull(Instant instant) {
        return instant == null ? null : instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}