package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

public class EndAfterStartValidatorImpl implements ConstraintValidator<EndAfterStartValidator, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof BookingDto) {
            BookingDto dto = (BookingDto) value;
            if (dto.getStart() == null || dto.getEnd() == null) {
                return true;
            }
            return dto.getEnd().isAfter(dto.getStart());

        } else if (value instanceof BookingRequestDto) {
            BookingRequestDto dto = (BookingRequestDto) value;
            if (dto.getStart() == null || dto.getEnd() == null) {
                return true;
            }
            return dto.getEnd().isAfter(dto.getStart());
        }
        return true;
    }
}