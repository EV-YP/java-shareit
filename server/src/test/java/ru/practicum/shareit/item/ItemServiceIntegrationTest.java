package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDatesDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    private UserDto owner;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        // Create test user
        UserDto userDto = new UserDto();
        userDto.setName("Item Owner");
        userDto.setEmail("owner@example.com");
        owner = userService.createUser(userDto);

        // Create test item
        itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Integration Test Description");
        itemDto.setAvailable(true);
    }

    @Test
    void addAndGetItem_whenValidItem_thenCorrectItemReturned() {
        // Add item
        ItemDto createdItem = itemService.addItem(owner.getId(), itemDto);

        assertNotNull(createdItem.getId());
        assertEquals(itemDto.getName(), createdItem.getName());
        assertEquals(itemDto.getDescription(), createdItem.getDescription());
        assertEquals(itemDto.getAvailable(), createdItem.getAvailable());

        // Get item
        ItemWithDatesDto retrievedItem = itemService.getItemByIdAndUserId(createdItem.getId(), owner.getId());

        assertEquals(createdItem.getId(), retrievedItem.getId());
        assertEquals(createdItem.getName(), retrievedItem.getName());
        assertEquals(createdItem.getDescription(), retrievedItem.getDescription());
        assertEquals(createdItem.getAvailable(), retrievedItem.getAvailable());
    }

    @Test
    void searchItems_whenItemsInRepository_thenReturnMatchingItems() {
        // Add several items with different descriptions
        ItemDto item1 = new ItemDto();
        item1.setName("First Item");
        item1.setDescription("searchable description");
        item1.setAvailable(true);

        ItemDto item2 = new ItemDto();
        item2.setName("Second searchable Item");
        item2.setDescription("another description");
        item2.setAvailable(true);

        ItemDto item3 = new ItemDto();
        item3.setName("Third Item");
        item3.setDescription("not matching");
        item3.setAvailable(true);

        itemService.addItem(owner.getId(), item1);
        itemService.addItem(owner.getId(), item2);
        itemService.addItem(owner.getId(), item3);

        // Search for "searchable" and verify results
        List<ItemDto> searchResults = itemService.searchItems("searchable");

        assertEquals(2, searchResults.size());
        assertTrue(searchResults.stream().anyMatch(item -> item.getName().equals("First Item")));
        assertTrue(searchResults.stream().anyMatch(item -> item.getName().equals("Second searchable Item")));
        assertFalse(searchResults.stream().anyMatch(item -> item.getName().equals("Third Item")));
    }
}
