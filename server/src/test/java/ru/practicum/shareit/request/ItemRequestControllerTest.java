package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto itemRequestDto;
    private ItemRequestWithAnswersDto itemRequestWithAnswersDto;
    private final Long userId = 1L;
    private final Long requestId = 1L;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(requestId);
        itemRequestDto.setDescription("Test Request Description");
        itemRequestDto.setCreated(LocalDateTime.now());

        itemRequestWithAnswersDto = new ItemRequestWithAnswersDto();
        itemRequestWithAnswersDto.setId(requestId);
        itemRequestWithAnswersDto.setDescription("Test Request Description");
        itemRequestWithAnswersDto.setCreated(LocalDateTime.now());
        itemRequestWithAnswersDto.setItems(List.of());
    }

    @Test
    void createRequest_whenValidRequestDto_thenStatus200AndRequestReturned() throws Exception {
        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setDescription("Test Request Description");
        request.setCreated(Instant.now());

        when(itemRequestService.createRequest(anyString(), anyLong())).thenReturn(request);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnRequests_whenUserExists_thenStatus200AndRequestsReturned() throws Exception {
        when(itemRequestService.getOwnRequests(anyLong())).thenReturn(List.of(itemRequestWithAnswersDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestWithAnswersDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestWithAnswersDto.getDescription())));
    }

    @Test
    void getOtherRequests_whenUserExists_thenStatus200AndRequestsReturned() throws Exception {
        when(itemRequestService.getOtherRequests(anyLong())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())));
    }

    @Test
    void getRequestById_whenValidId_thenStatus200AndRequestReturned() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong())).thenReturn(itemRequestWithAnswersDto);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestWithAnswersDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestWithAnswersDto.getDescription())));
    }
}
