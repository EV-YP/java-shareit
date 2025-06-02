package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.CustomValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        User existing = users.get(id);
        if (user.getName() != null) {
            existing.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existing.setEmail(user.getEmail());
        }
        users.put(id, existing);
        return existing;
    }

    @Override
    public User getUser(Long userId) {
        validateUser(userId);
        return users.get(userId);
    }

    @Override
    public void delete(Long id) {
        validateUser(id);
        users.remove(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public void validateUser(Long id) {
        if (id == null) {
            throw new CustomValidationException("Id должен быть указан");
        }

        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователь с id = %d не найден", id));
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}