package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestClient itemRequestClient;

    private ItemRequestDto itemRequestDto;
    private final long userId = 1L;
    private final long requestId = 1L;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Test Item Request Description");
    }

    @Test
    void createRequest_whenValidRequestDto_thenStatus200() throws Exception {
        when(itemRequestClient.createRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void createRequest_whenEmptyDescription_thenStatus400() throws Exception {
        itemRequestDto.setDescription("");

        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRequest_whenNullDescription_thenStatus400() throws Exception {
        itemRequestDto.setDescription(null);

        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnRequests_whenValidUserId_thenStatus200() throws Exception {
        when(itemRequestClient.getOwnRequests(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/requests")
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getOtherRequests_whenValidUserId_thenStatus200() throws Exception {
        when(itemRequestClient.getOtherRequests(anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/requests/all")
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestById_whenValidRequestId_thenStatus200() throws Exception {
        when(itemRequestClient.getRequestById(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/requests/{requestId}", requestId)
                .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk());
    }
}
