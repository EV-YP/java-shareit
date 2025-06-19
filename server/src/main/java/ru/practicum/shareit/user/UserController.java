package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        UserDto createdUserDto = userService.createUser(userDto);
        log.info("Пользователь: {} добавлен", createdUserDto);
        return createdUserDto;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        UserDto updatedUserDto = userService.updateUser(userId, userDto);
        log.info("Пользователь с id: {} обновлен", userId);
        return updatedUserDto;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        UserDto foundUserDto = userService.getUser(userId);
        log.info("Пользователь с id: {} найден", userId);
        return foundUserDto;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        log.info("Пользователь с id: {} удален", userId);
    }
}