package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.CustomValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item save(Long userId, Item item) {
        item.setId(getNextId());
        item.setOwnerId(userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long id, Item item) {
        Item existing = items.get(id);

        if (item.getName() != null) {
            existing.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existing.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            existing.setAvailable(item.getAvailable());
        }

        items.put(id, existing);
        return existing;
    }

    @Override
    public Item findById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> findByOwnerId(Long ownerId) {
        return items.values().stream().filter(i -> i.getOwnerId().equals(ownerId)).toList();
    }

    @Override
    public List<Item> search(String text) {

        String lower = text.toLowerCase();
        return items.values().stream()
                .filter(i -> Boolean.TRUE.equals(i.getAvailable()))
                .filter(i -> (i.getName().toLowerCase().contains(lower)) ||
                        (i.getDescription() != null && i.getDescription().toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }

    @Override
    public void validateItem(Long id) {
        if (id == null) {
            throw new CustomValidationException("Id должен быть указан");
        }

        if (!items.containsKey(id)) {
            throw new NotFoundException(String.format("Предмет с id = %d не найден", id));
        }
    }

    @Override
    public void validateOwner(Long id, Long userId) {
        if (!userId.equals(items.get(id).getOwnerId())) {
            throw new CustomValidationException(
                    String.format("Пользователь с id = %d не является владельцем предмета с id = %d", userId, id));
        }
    }

    private long getNextId() {
        long currentMaxId = items.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}