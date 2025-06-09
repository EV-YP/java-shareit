package ru.practicum.shareit.item.comment;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                LocalDateTime.ofInstant(comment.getCreated(), ZoneOffset.UTC)
        );
    }

    public static Comment toComment(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                null,
                null,
                commentDto.getCreated() != null ? commentDto.getCreated().toInstant(ZoneOffset.UTC) : null
        );
    }
}