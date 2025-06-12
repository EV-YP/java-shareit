package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.validation.EndAfterStartValidator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EndAfterStartValidator(message = "Дата окончания бронирования должна быть позже даты начала")
public class BookingDto {
    private Long id;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private BookingStatus status;

    private UserDto booker;

    private ItemDto item;
}
