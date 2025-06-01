package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserStorage {
    User save(User user);

    User update(Long id, User user);

    void delete(Long id);

    Collection<User> getAll();

    void validateUser(Long id);

    User getUser(Long userId);
}