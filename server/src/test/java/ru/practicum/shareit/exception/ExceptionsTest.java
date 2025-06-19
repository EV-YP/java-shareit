package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionsTest {

    @Test
    void conflictException_shouldStoreMessage() {
        String errorMessage = "Test conflict message";
        ConflictException exception = new ConflictException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void customValidationException_shouldStoreMessage() {
        String errorMessage = "Test validation message";
        CustomValidationException exception = new CustomValidationException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void notFoundException_shouldStoreMessage() {
        String errorMessage = "Test not found message";
        NotFoundException exception = new NotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
