package ru.practicum.shareit.item.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotNull(message = "Текст комментария не может быть пустым")
    private String text;

    private String authorName;

    private LocalDateTime created;
}