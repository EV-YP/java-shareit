package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserClient userClient;

    @Test
    void createUser_whenValidData_thenUserCreated() {
        UserDto userDto = new UserDto();
        userDto.setName("Integration Test User");
        userDto.setEmail("integration_test@example.com");

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(userDto);
        when(userClient.createUser(any(UserDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.createUser(userDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(userClient, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void updateUser_whenValidData_thenUserUpdated() {
        long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setName("Updated User");
        userDto.setEmail("updated@example.com");

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(userDto);
        when(userClient.updateUser(anyLong(), any(UserDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.updateUser(userId, userDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(userClient, times(1)).updateUser(anyLong(), any(UserDto.class));
    }

    @Test
    void getUser_whenUserExists_thenUserReturned() {
        long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(userDto);
        when(userClient.getUser(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.getUser(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
        verify(userClient, times(1)).getUser(userId);
    }

    @Test
    void deleteUser_whenUserExists_thenUserDeleted() {

        long userId = 1L;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok().build();
        when(userClient.deleteUser(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.deleteUser(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userClient, times(1)).deleteUser(userId);
    }
}
