package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemStorage {
    Item save(Long userId, Item item);
    Item update(Long id, Item item);
    Item findById(Long id);
    List<Item> findByOwnerId(Long ownerId);
    List<Item> search(String text);
    void validateItem(Long id);
    void validateOwner(Long itemId, Long userId);
}