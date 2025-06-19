package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ItemControllerIntegrationTest {

    @Autowired
    private ItemController itemController;

    @MockBean
    private ItemClient itemClient;

    @Test
    void addItem_whenValidItemData_thenItemAdded() {
        long userId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Integration Test Item");
        itemDto.setDescription("Integration Test Description");
        itemDto.setAvailable(true);

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(itemDto);
        when(itemClient.addItem(anyLong(), any(ItemDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.addItem(userId, itemDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemClient, times(1)).addItem(eq(userId), any(ItemDto.class));
    }

    @Test
    void updateItem_whenValidItemData_thenItemUpdated() {
        long userId = 1L;
        long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated Item");
        itemDto.setDescription("Updated Description");

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(itemDto);
        when(itemClient.updateItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.updateItem(userId, itemId, itemDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemClient, times(1)).updateItem(eq(userId), eq(itemId), any(ItemDto.class));
    }

    @Test
    void getItem_whenItemExists_thenItemReturned() {
        long userId = 1L;
        long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName("Test Item");

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(itemDto);
        when(itemClient.getItem(anyLong(), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getItem(userId, itemId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemClient, times(1)).getItem(userId, itemId);
    }

    @Test
    void getItemsByOwner_whenItemsExist_thenItemsReturned() {
        long userId = 1L;
        List<ItemDto> items = List.of(
                new ItemDto(1L, "Item 1", "Description 1", true, null, null),
                new ItemDto(2L, "Item 2", "Description 2", true, null, null)
        );

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(items);
        when(itemClient.getItemsByOwner(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getItemsByOwner(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemClient, times(1)).getItemsByOwner(userId);
    }

    @Test
    void searchItems_whenQueryProvided_thenMatchingItemsReturned() {
        String searchText = "test";
        List<ItemDto> items = List.of(
                new ItemDto(1L, "Test Item", "Test Description", true, null, null)
        );

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(items);
        when(itemClient.searchItems(anyString())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.searchItems(searchText);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemClient, times(1)).searchItems(searchText);
    }

    @Test
    void addComment_whenValidCommentData_thenCommentAdded() {
        long userId = 1L;
        long itemId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Integration Test Comment");

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(commentDto);
        when(itemClient.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.addComment(userId, itemId, commentDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemClient, times(1)).addComment(eq(userId), eq(itemId), any(CommentDto.class));
    }
}
