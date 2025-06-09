package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validation.EndAfterStartValidator;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EndAfterStartValidator(message = "Дата окончания бронирования должна быть позже даты начала")
public class BookingRequestDto {
    @NotNull
    private Long itemId;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}
