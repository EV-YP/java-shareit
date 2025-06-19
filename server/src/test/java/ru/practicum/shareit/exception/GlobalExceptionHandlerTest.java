package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleCustomValidationException_shouldReturnBadRequest() {
        String errorMessage = "Validation error message";
        CustomValidationException exception = new CustomValidationException(errorMessage);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleCustomValidationException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ошибка: ", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getDescription());
    }

    @Test
    void handleNotFoundException_shouldReturnNotFound() {
        // Arrange
        String errorMessage = "Resource not found message";
        NotFoundException exception = new NotFoundException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNotFoundException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Запрашиваемый ресурс не найден", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getDescription());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        // Arrange
        Exception exception = new RuntimeException("Some unexpected error");

        // Act
        ResponseEntity<String> response = exceptionHandler.handleGenericException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Ошибка сервера", response.getBody());
    }

    @Test
    void handleHttpMessageNotReadable_shouldReturnBadRequest() {
        // Arrange
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);

        // Act
        ResponseEntity<String> response = exceptionHandler.handleHttpMessageNotReadable(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Неверный формат запроса: тело запроса отсутствует или некорректно", response.getBody());
    }

    @Test
    void handleMissingParams_shouldReturnBadRequest() {
        // Arrange
        MissingServletRequestParameterException exception = mock(MissingServletRequestParameterException.class);
        when(exception.getParameterName()).thenReturn("userId");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMissingParams(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Отсутствует требуемый параметр", response.getBody().getError());
        assertEquals("userId", response.getBody().getDescription());
    }

    @Test
    void handleConflictException_shouldReturnConflict() {
        // Arrange
        String errorMessage = "Conflict error message";
        ConflictException exception = new ConflictException(errorMessage);

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleConflictException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ошибка", response.getBody().getError());
        assertEquals(errorMessage, response.getBody().getDescription());
    }
}
