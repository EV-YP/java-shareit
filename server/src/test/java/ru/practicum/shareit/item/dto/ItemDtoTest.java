package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoTest {

    @Test
    void testNoArgsConstructor() {
        ItemDto itemDto = new ItemDto();

        assertNull(itemDto.getId());
        assertNull(itemDto.getName());
        assertNull(itemDto.getDescription());
        assertNull(itemDto.getAvailable());
        assertNull(itemDto.getRequestId());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "Test Item";
        String description = "Test description";
        Boolean available = true;
        Long requestId = 2L;

        ItemDto itemDto = new ItemDto(id, name, description, available, null,requestId);

        assertEquals(id, itemDto.getId());
        assertEquals(name, itemDto.getName());
        assertEquals(description, itemDto.getDescription());
        assertEquals(available, itemDto.getAvailable());
        assertEquals(requestId, itemDto.getRequestId());
    }

    @Test
    void testSettersAndGetters() {
        ItemDto itemDto = new ItemDto();

        Long id = 1L;
        String name = "Test Item";
        String description = "Test description";
        Boolean available = true;
        Long requestId = 2L;

        itemDto.setId(id);
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        itemDto.setRequestId(requestId);

        assertEquals(id, itemDto.getId());
        assertEquals(name, itemDto.getName());
        assertEquals(description, itemDto.getDescription());
        assertEquals(available, itemDto.getAvailable());
        assertEquals(requestId, itemDto.getRequestId());
    }

    @Test
    void testEqualsAndHashCode() {
        ItemDto itemDto1 = new ItemDto(1L, "Test", "Description", true, null, 2L);
        ItemDto itemDto2 = new ItemDto(1L, "Test", "Description", true, null, 2L);
        ItemDto itemDto3 = new ItemDto(2L, "Test", "Description", true, null, 2L);

        assertEquals(itemDto1, itemDto2);
        assertEquals(itemDto1.hashCode(), itemDto2.hashCode());

        assertNotEquals(itemDto1, itemDto3);
        assertNotEquals(itemDto1.hashCode(), itemDto3.hashCode());

        assertNotEquals(null, itemDto1);
        assertNotEquals(new Object(), itemDto1);
    }

    @Test
    void testToString() {
        ItemDto itemDto = new ItemDto(1L, "Test", "Description", true, null, 2L);

        String result = itemDto.toString();

        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Test"));
        assertTrue(result.contains("description=Description"));
    }
}
