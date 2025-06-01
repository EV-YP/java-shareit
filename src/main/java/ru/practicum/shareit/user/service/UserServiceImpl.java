package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserStorage userStorage;

    @Override
    public UserDto createUser(UserDto userDto) {
        validateUserEmail(userDto);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.save(user));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        validateUserEmail(userDto);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.update(id, user));
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.delete(id);
    }

    @Override
    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userStorage.getUser(userId));
    }

    private void validateUserEmail(UserDto userDto) {
        if (userStorage.getAll().stream().anyMatch(u -> u.getEmail().equals(userDto.getEmail()))) {
            log.warn("Пользователь с email {} уже существует", userDto.getEmail());
            throw new ConflictException("Пользователь с таким email уже существует");
        }
    }
}