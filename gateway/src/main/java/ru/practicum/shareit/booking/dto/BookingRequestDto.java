package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.validation.EndAfterStartValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EndAfterStartValidator(message = "Дата окончания бронирования должна быть позже даты начала")
public class BookingRequestDto {
    private long itemId;

    @NotNull
    @Future(message = "Дата начала бронирования должна быть в будущем")
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}