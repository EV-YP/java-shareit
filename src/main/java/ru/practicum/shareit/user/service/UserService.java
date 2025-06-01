package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.UserDto;

public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    UserDto getUser(Long userId);
}