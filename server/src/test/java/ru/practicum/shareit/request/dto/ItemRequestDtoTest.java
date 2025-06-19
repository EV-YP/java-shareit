package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDtoTest {

    @Test
    void testNoArgsConstructor() {
        ItemRequestDto dto = new ItemRequestDto();

        assertNull(dto.getId());
        assertNull(dto.getDescription());
        assertNull(dto.getCreated());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String description = "Need a drill";
        LocalDateTime created = LocalDateTime.now();

        ItemRequestDto dto = new ItemRequestDto(id, description, created);

        assertEquals(id, dto.getId());
        assertEquals(description, dto.getDescription());
        assertEquals(created, dto.getCreated());
    }

    @Test
    void testSettersAndGetters() {
        ItemRequestDto dto = new ItemRequestDto();

        Long id = 1L;
        String description = "Need a drill";
        LocalDateTime created = LocalDateTime.now();

        dto.setId(id);
        dto.setDescription(description);
        dto.setCreated(created);

        assertEquals(id, dto.getId());
        assertEquals(description, dto.getDescription());
        assertEquals(created, dto.getCreated());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime created = LocalDateTime.now();

        ItemRequestDto dto1 = new ItemRequestDto(1L, "Description", created);
        ItemRequestDto dto2 = new ItemRequestDto(1L, "Description", created);
        ItemRequestDto dto3 = new ItemRequestDto(2L, "Description", created);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime created = LocalDateTime.now();
        ItemRequestDto dto = new ItemRequestDto(1L, "Description", created);

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("description=Description"));
    }
}
