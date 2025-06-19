package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto userDto;
    private final long userId = 1L;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(userId);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
    }

    @Test
    void createUser_whenValidUserDto_thenStatus200AndUserReturned() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void updateUser_whenValidUserDto_thenStatus200AndUpdatedUserReturned() throws Exception {
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setName("Updated Name");
        updatedUserDto.setEmail("updated@example.com");

        UserDto resultUserDto = new UserDto();
        resultUserDto.setId(userId);
        resultUserDto.setName("Updated Name");
        resultUserDto.setEmail("updated@example.com");

        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(resultUserDto);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId), Long.class))
                .andExpect(jsonPath("$.name", is(resultUserDto.getName())))
                .andExpect(jsonPath("$.email", is(resultUserDto.getEmail())));

        verify(userService, times(1)).updateUser(anyLong(), any(UserDto.class));
    }

    @Test
    void getUser_whenExistingId_thenStatus200AndUserReturned() throws Exception {
        when(userService.getUser(userId)).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));

        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void getUser_whenNonExistingId_thenStatus404() throws Exception {
        when(userService.getUser(anyLong())).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/users/{userId}", 999L))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUser(anyLong());
    }

    @Test
    void deleteUser_whenExistingId_thenStatus200() throws Exception {
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(userId);
    }
}
