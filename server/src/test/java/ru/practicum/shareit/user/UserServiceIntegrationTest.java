package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void createUserAndGetUserById_whenValidUser_thenCorrectUserReturned() {
        // Create user DTO
        UserDto userToCreate = new UserDto();
        userToCreate.setName("Integration Test User");
        userToCreate.setEmail("integration_test@example.com");

        // Create the user
        UserDto createdUser = userService.createUser(userToCreate);
        assertNotNull(createdUser.getId());
        assertEquals(userToCreate.getName(), createdUser.getName());
        assertEquals(userToCreate.getEmail(), createdUser.getEmail());

        // Retrieve the user and verify
        UserDto retrievedUser = userService.getUser(createdUser.getId());
        assertEquals(createdUser.getId(), retrievedUser.getId());
        assertEquals(createdUser.getName(), retrievedUser.getName());
        assertEquals(createdUser.getEmail(), retrievedUser.getEmail());
    }

    @Test
    void updateUserAndVerifyChanges_whenValidData_thenUserInfoChanged() {
        // Create initial user
        UserDto initialUser = new UserDto();
        initialUser.setName("Initial User");
        initialUser.setEmail("initial@example.com");
        UserDto createdUser = userService.createUser(initialUser);

        // Update the user
        UserDto updateRequest = new UserDto();
        updateRequest.setName("Updated Name");
        updateRequest.setEmail("updated@example.com");

        UserDto updatedUser = userService.updateUser(createdUser.getId(), updateRequest);

        // Verify the changes
        assertEquals(createdUser.getId(), updatedUser.getId());
        assertEquals(updateRequest.getName(), updatedUser.getName());
        assertEquals(updateRequest.getEmail(), updatedUser.getEmail());

        // Verify by getting the user again
        UserDto retrievedUser = userService.getUser(createdUser.getId());
        assertEquals(updateRequest.getName(), retrievedUser.getName());
        assertEquals(updateRequest.getEmail(), retrievedUser.getEmail());
    }
}
