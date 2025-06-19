package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestWithAnswersDtoTest {

    @Test
    void testNoArgsConstructor() {
        ItemRequestWithAnswersDto dto = new ItemRequestWithAnswersDto();

        assertNull(dto.getId());
        assertNull(dto.getDescription());
        assertNull(dto.getCreated());
        assertNull(dto.getItems());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String description = "Need a tool";
        LocalDateTime created = LocalDateTime.now();
        List<ItemRequestAnswerDto> items = new ArrayList<>();
        items.add(new ItemRequestAnswerDto(2L, "Drill", 1L));

        ItemRequestWithAnswersDto dto = new ItemRequestWithAnswersDto(id, description, created, items);

        assertEquals(id, dto.getId());
        assertEquals(description, dto.getDescription());
        assertEquals(created, dto.getCreated());
        assertEquals(items, dto.getItems());
        assertEquals(1, dto.getItems().size());
    }

    @Test
    void testSettersAndGetters() {
        ItemRequestWithAnswersDto dto = new ItemRequestWithAnswersDto();

        Long id = 1L;
        String description = "Need a tool";
        LocalDateTime created = LocalDateTime.now();
        List<ItemRequestAnswerDto> items = new ArrayList<>();
        items.add(new ItemRequestAnswerDto(2L, "Drill", 1L));

        dto.setId(id);
        dto.setDescription(description);
        dto.setCreated(created);
        dto.setItems(items);

        assertEquals(id, dto.getId());
        assertEquals(description, dto.getDescription());
        assertEquals(created, dto.getCreated());
        assertEquals(items, dto.getItems());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime created = LocalDateTime.now();
        List<ItemRequestAnswerDto> items = new ArrayList<>();
        items.add(new ItemRequestAnswerDto(2L, "Drill", 1L));

        ItemRequestWithAnswersDto dto1 = new ItemRequestWithAnswersDto(1L, "Description", created, items);
        ItemRequestWithAnswersDto dto2 = new ItemRequestWithAnswersDto(1L, "Description", created, items);
        ItemRequestWithAnswersDto dto3 = new ItemRequestWithAnswersDto(2L, "Description", created, items);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime created = LocalDateTime.now();
        List<ItemRequestAnswerDto> items = new ArrayList<>();
        items.add(new ItemRequestAnswerDto(2L, "Drill", 1L));

        ItemRequestWithAnswersDto dto = new ItemRequestWithAnswersDto(1L, "Description", created, items);

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("description=Description"));
    }
}
