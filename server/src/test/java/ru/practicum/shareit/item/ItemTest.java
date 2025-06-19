package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testEqualsAndHashCode() {
        User owner = new User();
        owner.setId(1L);

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Test Item");
        item1.setDescription("Description");
        item1.setAvailable(true);
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Test Item");
        item2.setDescription("Description");
        item2.setAvailable(true);
        item2.setOwner(owner);

        Item item3 = new Item();
        item3.setId(2L);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());

        assertNotEquals(item1, item3);
        assertNotEquals(item1.hashCode(), item3.hashCode());

        assertNotEquals(null, item1);
        assertNotEquals(new Object(), item1);
    }

    @Test
    void testGettersAndSetters() {
        Item item = new Item();

        Long id = 1L;
        String name = "Test Item";
        String description = "Description";
        boolean available = true;

        User owner = new User();
        owner.setId(2L);

        ItemRequest request = new ItemRequest();
        request.setId(3L);

        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(owner);
        item.setRequest(request);

        assertEquals(id, item.getId());
        assertEquals(name, item.getName());
        assertEquals(description, item.getDescription());
        assertEquals(available, item.getAvailable());
        assertEquals(owner, item.getOwner());
        assertEquals(request, item.getRequest());
    }

    @Test
    void testToString() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        String result = item.toString();

        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Test Item"));
    }
}
