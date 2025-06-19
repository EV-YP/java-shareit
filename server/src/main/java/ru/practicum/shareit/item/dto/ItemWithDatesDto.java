package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemWithDatesDto extends ItemDto {
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<CommentDto> comments;
}