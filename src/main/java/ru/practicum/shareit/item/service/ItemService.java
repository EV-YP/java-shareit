package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);
    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);
    ItemDto getItem(Long itemId);
    List<ItemDto> getItemsByOwner(Long ownerId);
    List<ItemDto> searchItems(String text);
}

