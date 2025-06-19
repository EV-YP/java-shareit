package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequest createRequest(String description, Long userId);

    List<ItemRequestWithAnswersDto> getOwnRequests(Long userId);

    List<ItemRequestDto> getOtherRequests(Long userId);

    ItemRequestWithAnswersDto getRequestById(Long requestId, Long userId);
}