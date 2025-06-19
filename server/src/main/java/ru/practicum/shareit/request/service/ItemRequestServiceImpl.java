package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public ItemRequest createRequest(String description, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", userId)));
        ItemRequest request = new ItemRequest();
        request.setDescription(description);
        request.setRequestor(user);
        request.setCreated(Instant.now());
        return itemRequestRepository.save(request);
    }

    public List<ItemRequestWithAnswersDto> getOwnRequests(Long userId) {
        validateUserExists(userId);
        return itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId)
                .stream()
                .map(ItemRequestMapper::toDtoWithAnswers)
                .toList();
    }

    public List<ItemRequestDto> getOtherRequests(Long userId) {
        validateUserExists(userId);
        return itemRequestRepository.findAllByOtherUsers(userId)
                .stream()
                .map(ItemRequestMapper::toDto)
                .toList();
    }

    public ItemRequestWithAnswersDto getRequestById(Long requestId, Long userId) {
        validateUserExists(userId);
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id %d не найден", requestId)));
        return ItemRequestMapper.toDtoWithAnswers(request);
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
    }
}
