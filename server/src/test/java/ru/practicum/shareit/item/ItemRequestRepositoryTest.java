package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    private User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    private ItemRequest createRequest(User requestor, String description, Instant created) {
        ItemRequest request = new ItemRequest();
        request.setRequestor(requestor);
        request.setDescription(description);
        request.setCreated(created);
        return itemRequestRepository.save(request);
    }

    @Test
    void findAllByOtherUsers_returnsRequestsFromOtherUsersSorted() {
        User owner = createUser("owner", "o@example.com");
        User other = createUser("other", "x@example.com");

        ItemRequest first = createRequest(other, "first", Instant.now().minusSeconds(10));
        ItemRequest second = createRequest(other, "second", Instant.now());
        createRequest(owner, "ignored", Instant.now());

        List<ItemRequest> result = itemRequestRepository.findAllByOtherUsers(owner.getId());

        assertEquals(2, result.size());
        assertEquals(second.getId(), result.get(0).getId());
        assertEquals(first.getId(), result.get(1).getId());
    }
}