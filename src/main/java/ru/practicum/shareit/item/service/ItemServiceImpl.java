package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        userStorage.validateUser(userId);
        Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemStorage.save(userId, item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        userStorage.validateUser(userId);
        itemStorage.validateItem(itemId);
        itemStorage.validateOwner(itemId, userId);
        Item update = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(itemStorage.update(itemId, update));
    }

    @Override
    public ItemDto getItem(Long itemId) {
        itemStorage.validateItem(itemId);
        return ItemMapper.toItemDto(itemStorage.findById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        userStorage.validateUser(ownerId);
        return itemStorage.findByOwnerId(ownerId).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemStorage.search(text).stream().map(ItemMapper::toItemDto).toList();
    }
}