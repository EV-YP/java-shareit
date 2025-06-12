package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDatesDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        ItemDto createdItemDto = itemService.addItem(userId, itemDto);
        log.info("Предмет: {} добавлен", createdItemDto);
        return createdItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        ItemDto updatedItemDto = itemService.updateItem(userId, itemId, itemDto);
        log.info("Предмет с id: {} обновлен", itemId);
        return updatedItemDto;
    }

    @GetMapping("/{itemId}")
    public ItemWithDatesDto getItem(@RequestHeader ("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {
        ItemWithDatesDto itemDto = itemService.getItemByIdAndUserId(itemId, userId);
        log.info("Предмет с id: {} найден", itemId);
        return itemDto;
    }

    @GetMapping
    public List<ItemWithDatesDto> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemWithDatesDto> userItemsWithDatesDto = itemService.getItemsByOwner(userId);
        log.info("Найдено {} предметов пользователя с id: {}", userItemsWithDatesDto.size(), userId);
        return userItemsWithDatesDto;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        List<ItemDto> foundItemsDto = itemService.searchItems(text);
        log.info("По запросу: {} найдено {} предметов, доступных для аренды", text, foundItemsDto.size());
        return foundItemsDto;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @Valid @RequestBody CommentDto commentDto) {
        CommentDto savedCommentDto = itemService.addComment(userId, itemId, commentDto);
        log.info("Комментарий добавлен к предмету с id: {}", itemId);
        return savedCommentDto;
    }
}