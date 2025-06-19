package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.GlobalExceptionHandler;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ItemController.class, GlobalExceptionHandler.class})
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    private ItemDto itemDto;
    private CommentDto commentDto;
    private final long userId = 1L;
    private final long itemId = 1L;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setText("Test Comment");
    }

    @Test
    void addItem_whenValidItem_thenStatus200() throws Exception {
        when(itemClient.addItem(anyLong(), any(ItemDto.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void addItem_whenMissingName_thenStatus400() throws Exception {
        itemDto.setName(null);

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addItem_whenMissingDescription_thenStatus400() throws Exception {
        itemDto.setDescription(null);

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addItem_whenMissingAvailable_thenStatus400() throws Exception {
        itemDto.setAvailable(null);

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_whenValidItem_thenStatus200() throws Exception {
        when(itemClient.updateItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(patch("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getItem_whenValidItemId_thenStatus200() throws Exception {
        when(itemClient.getItem(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items/{itemId}", itemId)
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getItemsByOwner_whenValidUserId_thenStatus200() throws Exception {
        when(itemClient.getItemsByOwner(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items")
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void searchItems_whenValidQuery_thenStatus200() throws Exception {
        when(itemClient.searchItems(anyString())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/items/search")
                .param("text", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void addComment_whenValidComment_thenStatus200() throws Exception {
        when(itemClient.addComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());
    }

    @Test
    void addComment_whenEmptyText_thenStatus400() throws Exception {
        CommentDto invalidCommentDto = new CommentDto();
        invalidCommentDto.setText(null);

        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCommentDto)))
                .andExpect(status().isBadRequest());

        verify(itemClient, never()).addComment(anyLong(), anyLong(), any(CommentDto.class));
    }
}
