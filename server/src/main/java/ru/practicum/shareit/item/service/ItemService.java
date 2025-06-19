package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDatesDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getItemByIdAndOwnerId(Long itemId, Long userId);

    ItemWithDatesDto getItemByIdAndUserId(Long itemId, Long userId);

    List<ItemWithDatesDto> getItemsByOwner(Long ownerId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}