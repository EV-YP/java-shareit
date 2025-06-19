package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class DtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> userDtoJacksonTester;

    @Autowired
    private JacksonTester<ItemDto> itemDtoJacksonTester;

    @Autowired
    private JacksonTester<BookingRequestDto> bookingRequestDtoJacksonTester;

    @Autowired
    private JacksonTester<ItemRequestDto> itemRequestDtoJacksonTester;

    @Autowired
    private JacksonTester<CommentDto> commentDtoJacksonTester;

    @Test
    void userDto_whenSerialize_thenCorrectJson() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");

        JsonContent<UserDto> result = userDtoJacksonTester.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test User");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@example.com");
    }

    @Test
    void userDto_whenDeserialize_thenCorrectObject() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UserDto result = userDtoJacksonTester.parse(json).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void itemDto_whenSerialize_thenCorrectJson() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);
        itemDto.setRequestId(2L);

        JsonContent<ItemDto> result = itemDtoJacksonTester.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test Item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test Description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }

    @Test
    void itemDto_whenDeserialize_thenCorrectObject() throws Exception {
        String json = "{\"id\":1,\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"requestId\":2}";

        ItemDto result = itemDtoJacksonTester.parse(json).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Item");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getRequestId()).isEqualTo(2L);
    }

    @Test
    void bookingRequestDto_whenSerialize_thenCorrectJson() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 12, 0);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(start);
        bookingRequestDto.setEnd(end);

        JsonContent<BookingRequestDto> result = bookingRequestDtoJacksonTester.write(bookingRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isNotEmpty();
        assertThat(result).extractingJsonPathStringValue("$.end").isNotEmpty();
    }

    @Test
    void bookingRequestDto_whenDeserialize_thenCorrectObject() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2023-01-01T12:00:00\",\"end\":\"2023-01-02T12:00:00\"}";

        BookingRequestDto result = bookingRequestDtoJacksonTester.parse(json).getObject();

        assertThat(result.getItemId()).isEqualTo(1L);
        assertThat(result.getStart()).isNotNull();
        assertThat(result.getEnd()).isNotNull();
    }

    @Test
    void itemRequestDto_whenSerialize_thenCorrectJson() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Test Request Description");
        itemRequestDto.setCreated(LocalDateTime.of(2023, 1, 1, 12, 0));

        JsonContent<ItemRequestDto> result = itemRequestDtoJacksonTester.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test Request Description");
        assertThat(result).extractingJsonPathStringValue("$.created").isNotEmpty();
    }

    @Test
    void itemRequestDto_whenDeserialize_thenCorrectObject() throws Exception {
        String json = "{\"id\":1,\"description\":\"Test Request Description\",\"created\":\"2023-01-01T12:00:00\"}";

        ItemRequestDto result = itemRequestDtoJacksonTester.parse(json).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Test Request Description");
        assertThat(result.getCreated()).isNotNull();
    }

    @Test
    void commentDto_whenSerialize_thenCorrectJson() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Test Comment");
        commentDto.setAuthorName("Test Author");
        commentDto.setCreated(LocalDateTime.of(2023, 1, 1, 12, 0));

        JsonContent<CommentDto> result = commentDtoJacksonTester.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Test Comment");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Test Author");
        assertThat(result).extractingJsonPathStringValue("$.created").isNotEmpty();
    }

    @Test
    void commentDto_whenDeserialize_thenCorrectObject() throws Exception {
        String json = "{\"id\":1,\"text\":\"Test Comment\",\"authorName\":\"Test Author\",\"created\":\"2023-01-01T12:00:00\"}";

        CommentDto result = commentDtoJacksonTester.parse(json).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getText()).isEqualTo("Test Comment");
        assertThat(result.getAuthorName()).isEqualTo("Test Author");
        assertThat(result.getCreated()).isNotNull();
    }
}
