package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private User user;
    private ItemRequest itemRequest;
    private final Long userId = 1L;
    private final Long requestId = 1L;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(userId);
        user.setName("Test User");
        user.setEmail("test@example.com");

        User otherUser = new User();
        Long otherUserId = 2L;
        otherUser.setId(otherUserId);
        otherUser.setName("Other User");
        otherUser.setEmail("other@example.com");

        itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        itemRequest.setDescription("Test Request Description");
        itemRequest.setRequestor(user);
        itemRequest.setCreated(Instant.now());
        itemRequest.setItems(new ArrayList<>());
    }

    @Test
    void createRequest_whenValidRequest_thenRequestSaved() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequest result = itemRequestService.createRequest("Test Request Description", userId);

        assertNotNull(result);
        assertEquals(requestId, result.getId());
        assertEquals("Test Request Description", result.getDescription());
        assertEquals(userId, result.getRequestor().getId());
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void createRequest_whenUserNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.createRequest("Test Description", 999L));
        verify(itemRequestRepository, never()).save(any(ItemRequest.class));
    }

    @Test
    void getOwnRequests_whenUserHasRequests_thenReturnRequestList() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId)).thenReturn(List.of(itemRequest));

        List<ItemRequestWithAnswersDto> result = itemRequestService.getOwnRequests(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(requestId, result.getFirst().getId());
        assertEquals("Test Request Description", result.getFirst().getDescription());
    }

    @Test
    void getOwnRequests_whenUserHasNoRequests_thenReturnEmptyList() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId)).thenReturn(Collections.emptyList());

        List<ItemRequestWithAnswersDto> result = itemRequestService.getOwnRequests(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getOtherRequests_whenOtherRequestsExist_thenReturnRequestList() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findAllByOtherUsers(userId)).thenReturn(List.of(itemRequest));

        List<ItemRequestDto> result = itemRequestService.getOtherRequests(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(requestId, result.getFirst().getId());
        assertEquals("Test Request Description", result.getFirst().getDescription());
    }

    @Test
    void getOtherRequests_whenNoOtherRequests_thenReturnEmptyList() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findAllByOtherUsers(userId)).thenReturn(Collections.emptyList());

        List<ItemRequestDto> result = itemRequestService.getOtherRequests(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getRequestById_whenRequestExists_thenReturnRequest() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));

        ItemRequestWithAnswersDto result = itemRequestService.getRequestById(requestId, userId);

        assertNotNull(result);
        assertEquals(requestId, result.getId());
        assertEquals("Test Request Description", result.getDescription());
    }

    @Test
    void getRequestById_whenRequestNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(999L, userId));
    }

    @Test
    void getRequestById_whenUserNotFound_thenNotFoundExceptionThrown() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemRequestService.getRequestById(requestId, userId));
    }
}
