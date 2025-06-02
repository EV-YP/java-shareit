package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ErrorResponse> handleCustomValidationException(CustomValidationException ex) {
        log.error(ex.getMessage());
        ErrorResponse response = new ErrorResponse("Ошибка: ", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringBuilder description = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                description.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        ErrorResponse response = new ErrorResponse("Ошибка валидации", description.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage());
        ErrorResponse response = new ErrorResponse("Запрашиваемый ресурс не найден", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("Ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Неверный формат запроса: тело запроса отсутствует или некорректно",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        ErrorResponse response = new ErrorResponse("Отсутствует требуемый параметр", ex.getParameterName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        log.error(ex.getMessage());
        ErrorResponse response = new ErrorResponse("Ошибка", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}