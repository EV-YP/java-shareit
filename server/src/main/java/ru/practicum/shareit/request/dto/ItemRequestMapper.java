package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                LocalDateTime.ofInstant(request.getCreated(), ZoneOffset.UTC)
        );
    }

    public static ItemRequestWithAnswersDto toDtoWithAnswers(ItemRequest request) {
        List<ItemRequestAnswerDto> answers = request.getItems() == null ? List.of() :
                request.getItems().stream().map(ItemRequestMapper::toAnswerDto).collect(Collectors.toList());
        return new ItemRequestWithAnswersDto(
                request.getId(),
                request.getDescription(),
                LocalDateTime.ofInstant(request.getCreated(), ZoneOffset.UTC),
                answers
        );
    }

    public static ItemRequestAnswerDto toAnswerDto(ru.practicum.shareit.item.Item item) {
        return new ItemRequestAnswerDto(
                item.getId(),
                item.getName(),
                item.getOwner() != null ? item.getOwner().getId() : null
        );
    }
}