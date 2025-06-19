package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toUserDto_whenValidUser_thenCorrectDto() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        UserDto result = UserMapper.toUserDto(user);

        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void toUser_whenValidDto_thenCorrectEntity() {
        UserDto dto = new UserDto();
        dto.setName("Test User");
        dto.setEmail("test@example.com");

        User result = UserMapper.toUser(dto);

        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void toUserDto_whenNullUser_thenReturnsNull() {
        UserDto result = UserMapper.toUserDto(null);

        assertNull(result);
    }

    @Test
    void toUser_whenNullDto_thenReturnsNull() {
        User result = UserMapper.toUser(null);

        assertNull(result);
    }
}
