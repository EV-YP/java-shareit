package ru.practicum.shareit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EndAfterStartValidatorImpl.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndAfterStartValidator {
    String message() default "Дата завершения бронирования должна быть после даты начала";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}