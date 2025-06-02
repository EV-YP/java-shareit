package ru.practicum.shareit.request;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        if (itemRequest == null) return null;
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestorId(itemRequest.getRequestorId());
        dto.setCreated(itemRequest.getCreated());
        return dto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto dto) {
        if (dto == null) return null;
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setRequestorId(dto.getRequestorId());
        itemRequest.setCreated(dto.getCreated());
        return itemRequest;
    }
}