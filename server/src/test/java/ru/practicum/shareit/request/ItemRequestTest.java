package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {

    @Test
    void testGettersAndSetters() {
        ItemRequest request = new ItemRequest();

        Long id = 1L;
        String description = "Need a tool";
        User requestor = new User();
        requestor.setId(2L);
        Instant created = Instant.now();
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(3L);
        items.add(item);

        request.setId(id);
        request.setDescription(description);
        request.setRequestor(requestor);
        request.setCreated(created);
        request.setItems(items);

        assertEquals(id, request.getId());
        assertEquals(description, request.getDescription());
        assertEquals(requestor, request.getRequestor());
        assertEquals(created, request.getCreated());
        assertEquals(items, request.getItems());
        assertEquals(1, request.getItems().size());
    }

    @Test
    void testToString() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need a tool");

        String result = request.toString();

        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("description=Need a tool"));
    }
}
