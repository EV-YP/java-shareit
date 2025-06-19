package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        validateUserEmail(userDto.getEmail());
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        validateUserEmail(userDto.getEmail() != null ? userDto.getEmail() : null);
        UserDto user = getUser(id);
        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setName(userDto.getName() != null ? userDto.getName() : user.getName());
        updatedUser.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
        return UserMapper.toUserDto(userRepository.save(updatedUser));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id %d не найден", userId))));
    }

    private void validateUserEmail(String email) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(email))) {
            throw new ConflictException(String.format("Пользователь с email %s уже существует", email));
        }
    }
}