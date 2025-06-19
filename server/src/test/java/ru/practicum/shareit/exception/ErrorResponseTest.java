package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorResponseTest {

    @Test
    void errorResponse_shouldCreateAndUpdateFields() {
        ErrorResponse response = new ErrorResponse();
        assertNotNull(response);

        String error = "Test Error";
        String description = "Test Description";

        response.setError(error);
        response.setDescription(description);

        assertEquals(error, response.getError());
        assertEquals(description, response.getDescription());

        ErrorResponse withArgsResponse = new ErrorResponse(error, description);
        assertNotNull(withArgsResponse);
        assertEquals(error, withArgsResponse.getError());
        assertEquals(description, withArgsResponse.getDescription());
    }
}
