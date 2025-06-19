package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @Size(min = 1,
            max = 255,
            groups = {Create.class, Update.class},
            message = "Имя пользователя должно быть от 1 до 255 символов")
    @NotBlank(groups = Create.class,
            message = "Имя обязательно при создании пользователя")
    private String name;

    @Email(groups = {Create.class, Update.class},
            message = "Email должен быть корректным")
    @NotBlank(groups = Create.class,
            message = "Email обязателен при создании пользователя")
    private String email;
}