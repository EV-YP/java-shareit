package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
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
    private JacksonTester<BookingDto> bookingDtoJacksonTester;

    @Autowired
    private JacksonTester<ItemRequestDto> itemRequestDtoJacksonTester;

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
    void bookingDto_whenSerialize_thenCorrectJson() throws Exception {
        UserDto booker = new UserDto();
        booker.setId(1L);
        booker.setName("Test User");
        booker.setEmail("test@example.com");

        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setName("Test Item");

        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 1, 2, 12, 0);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setItem(item);
        bookingDto.setBooker(booker);
        bookingDto.setStatus(BookingStatus.APPROVED);

        JsonContent<BookingDto> result = bookingDtoJacksonTester.write(bookingDto);

        // Adjust expectations to match actual serialized format without Z
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-01-01T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-01-02T12:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Test Item");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
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
}
