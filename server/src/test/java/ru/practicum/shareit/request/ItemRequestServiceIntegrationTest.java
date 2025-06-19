package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestServiceIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserService userService;

    private UserDto requester;
    private UserDto otherUser;

    @BeforeEach
    void setUp() {

        UserDto requesterDto = new UserDto();
        requesterDto.setName("Requester");
        requesterDto.setEmail("requester@example.com");
        requester = userService.createUser(requesterDto);

        UserDto otherUserDto = new UserDto();
        otherUserDto.setName("Other User");
        otherUserDto.setEmail("other@example.com");
        otherUser = userService.createUser(otherUserDto);
    }

    @Test
    void createRequestAndGetById_whenValidRequest_thenCorrectRequestReturned() {
        String requestDescription = "I need a drill";
        ItemRequest createdRequest = itemRequestService.createRequest(requestDescription, requester.getId());

        assertNotNull(createdRequest);
        assertEquals(requestDescription, createdRequest.getDescription());

        ItemRequestWithAnswersDto retrievedRequest = itemRequestService.getRequestById(createdRequest.getId(), requester.getId());

        assertNotNull(retrievedRequest);
        assertEquals(createdRequest.getId(), retrievedRequest.getId());
        assertEquals(requestDescription, retrievedRequest.getDescription());
    }

    @Test
    void getOwnRequestsWithItems_whenRequestsHaveItems_thenReturnRequestsWithItems() {
        String firstDescription = "I need a lawn mower";
        String secondDescription = "I need a bicycle";

        itemRequestService.createRequest(firstDescription, requester.getId());
        itemRequestService.createRequest(secondDescription, requester.getId());

        List<ItemRequestWithAnswersDto> ownRequests = itemRequestService.getOwnRequests(requester.getId());

        assertFalse(ownRequests.isEmpty());
        assertEquals(2, ownRequests.size());
        assertTrue(ownRequests.stream().anyMatch(r -> r.getDescription().equals(firstDescription)));
        assertTrue(ownRequests.stream().anyMatch(r -> r.getDescription().equals(secondDescription)));
    }

    @Test
    void getOtherRequests_whenRequesterHasRequests_thenOtherUserCanSee() {

        String requestDescription = "I need a table";
        ItemRequest createdRequest = itemRequestService.createRequest(requestDescription, requester.getId());

        var otherRequests = itemRequestService.getOtherRequests(otherUser.getId());

        assertFalse(otherRequests.isEmpty());
        assertEquals(1, otherRequests.size());
        assertEquals(createdRequest.getId(), otherRequests.getFirst().getId());
        assertEquals(requestDescription, otherRequests.getFirst().getDescription());
    }
}
