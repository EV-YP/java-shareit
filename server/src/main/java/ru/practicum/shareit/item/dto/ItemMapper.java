package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.Item;

import java.util.ArrayList;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        if (item == null) return null;
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setRequestId(item.getRequest() == null ? null : item.getRequest().getId());
        return dto;
    }

    public static Item toItem(ItemDto dto) {
        if (dto == null) return null;
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    public static ItemWithDatesDto toItemWithDatesDto(Item item) {
        if (item == null) return null;
        ItemWithDatesDto dto = new ItemWithDatesDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.getAvailable());
        dto.setLastBooking(null);
        dto.setNextBooking(null);
        dto.setComments(new ArrayList<>());
        return dto;
    }
}