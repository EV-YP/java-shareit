package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

public class EndAfterStartValidatorImpl implements ConstraintValidator<EndAfterStartValidator, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BookingRequestDto dto = (BookingRequestDto) value;
        if (dto.getStart() == null || dto.getEnd() == null) {
            return true;
        }
        return dto.getEnd().isAfter(dto.getStart());
    }
}