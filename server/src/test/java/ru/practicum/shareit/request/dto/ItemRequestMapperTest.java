package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    @Test
    void toDto_whenValidItemRequest_thenCorrectDto() {
        User user = new User(1L, "Requester", "requester@example.com");

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Test Request");
        request.setRequestor(user);
        request.setCreated(Instant.now());

        ItemRequestDto result = ItemRequestMapper.toDto(request);

        assertEquals(1L, result.getId());
        assertEquals("Test Request", result.getDescription());
        assertNotNull(result.getCreated());
    }

    @Test
    void toDtoWithAnswers_whenValidItemRequest_thenCorrectDto() {

        User user = new User(1L, "Requester", "requester@example.com");
        User itemOwner = new User(2L, "Owner", "owner@example.com");

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Test Request");
        request.setRequestor(user);
        request.setCreated(Instant.now());

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Item Description");
        item.setAvailable(true);
        item.setOwner(itemOwner);
        item.setRequest(request);

        List<Item> items = List.of(item);
        request.setItems(items);

        ItemRequestWithAnswersDto result = ItemRequestMapper.toDtoWithAnswers(request);

        assertEquals(1L, result.getId());
        assertEquals("Test Request", result.getDescription());
        assertNotNull(result.getCreated());
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());

        ItemRequestAnswerDto answer = result.getItems().getFirst();
        assertEquals(1L, answer.getId());
        assertEquals("Test Item", answer.getName());
        assertEquals(2L, answer.getOwnerId());
    }

    @Test
    void toDtoWithAnswers_whenRequestHasNoItems_thenEmptyItemsList() {
        User user = new User(1L, "Requester", "requester@example.com");

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Test Request");
        request.setRequestor(user);
        request.setCreated(Instant.now());
        request.setItems(Collections.emptyList());

        ItemRequestWithAnswersDto result = ItemRequestMapper.toDtoWithAnswers(request);

        assertEquals(1L, result.getId());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }
}
