package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody ItemRequestDto itemRequestDto) {
        return ResponseEntity.ok(itemRequestService.createRequest(itemRequestDto.getDescription(), userId));
    }

    @GetMapping
    public List<ItemRequestWithAnswersDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getOtherRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithAnswersDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable Long requestId) {
        return itemRequestService.getRequestById(requestId, userId);
    }
}