package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    @Test
    void toItemDto_whenValidItem_thenCorrectDto() {
        User owner = new User(1L, "Owner", "owner@example.com");
        ItemRequest request = new ItemRequest();
        request.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(request);

        ItemDto result = ItemMapper.toItemDto(item);

        assertEquals(1L, result.getId());
        assertEquals("Test Item", result.getName());
        assertEquals("Description", result.getDescription());
        assertTrue(result.getAvailable());
        assertEquals(2L, result.getRequestId());
    }

    @Test
    void toItem_whenValidDto_thenCorrectEntity() {
        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("Test Item");
        dto.setDescription("Description");
        dto.setAvailable(true);
        dto.setRequestId(2L);

        Item result = ItemMapper.toItem(dto);

        assertEquals(1L, result.getId());
        assertEquals("Test Item", result.getName());
        assertEquals("Description", result.getDescription());
        assertTrue(result.getAvailable());
        assertNull(result.getRequest());
    }

    @Test
    void toItemWithDatesDto_whenValidItem_thenCorrectDto() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Description");
        item.setAvailable(true);

        LocalDateTime nextBookingStart = LocalDateTime.now().plusDays(1);
        LocalDateTime lastBookingEnd = LocalDateTime.now().minusDays(1);

        ItemWithDatesDto result = ItemMapper.toItemWithDatesDto(item);

        assertEquals(1L, result.getId());
        assertEquals("Test Item", result.getName());
        assertEquals("Description", result.getDescription());
        assertTrue(result.getAvailable());
        assertNull(result.getNextBooking());
        assertNull(result.getLastBooking());
        assertNotNull(result.getComments());
        assertTrue(result.getComments().isEmpty());

        result.setNextBooking(nextBookingStart);
        result.setLastBooking(lastBookingEnd);

        assertEquals(nextBookingStart, result.getNextBooking());
        assertEquals(lastBookingEnd, result.getLastBooking());
    }

    @Test
    void toItemDto_whenNullItem_thenReturnsNull() {
        ItemDto result = ItemMapper.toItemDto(null);

        assertNull(result);
    }

    @Test
    void toItem_whenNullDto_thenReturnsNull() {
        Item result = ItemMapper.toItem(null);

        assertNull(result);
    }

    @Test
    void toItemWithDatesDto_whenNullItem_thenReturnsNull() {
        ItemWithDatesDto result = ItemMapper.toItemWithDatesDto(null);

        assertNull(result);
    }
}