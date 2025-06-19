package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ItemRequestControllerIntegrationTest {

    @Autowired
    private ItemRequestController itemRequestController;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Test
    void createRequest_whenValidRequestData_thenRequestCreated() {
        long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Integration Test Request Description");

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(itemRequestDto);
        when(itemRequestClient.createRequest(anyLong(), any(ItemRequestDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemRequestController.createRequest(userId, itemRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemRequestClient, times(1)).createRequest(eq(userId), any(ItemRequestDto.class));
    }

    @Test
    void getOwnRequests_whenUserHasRequests_thenRequestsReturned() {
        long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test Request");
        itemRequestDto.setCreated(LocalDateTime.now());

        List<ItemRequestDto> requests = List.of(itemRequestDto);
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(requests);
        when(itemRequestClient.getOwnRequests(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemRequestController.getOwnRequests(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemRequestClient, times(1)).getOwnRequests(userId);
    }

    @Test
    void getOtherRequests_whenRequestsExist_thenRequestsReturned() {
        long userId = 1L;
        List<ItemRequestDto> requests = List.of(
            new ItemRequestDto(1L, "Request 1", LocalDateTime.now()),
            new ItemRequestDto(2L, "Request 2", LocalDateTime.now())
        );

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(requests);
        when(itemRequestClient.getOtherRequests(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemRequestController.getOtherRequests(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemRequestClient, times(1)).getOtherRequests(userId);
    }

    @Test
    void getRequestById_whenRequestExists_thenRequestReturned() {
        long userId = 1L;
        long requestId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(requestId);
        itemRequestDto.setDescription("Test Request Description");
        itemRequestDto.setCreated(LocalDateTime.now());

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(itemRequestDto);
        when(itemRequestClient.getRequestById(anyLong(), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemRequestController.getRequestById(userId, requestId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(itemRequestClient, times(1)).getRequestById(userId, requestId);
    }
}
