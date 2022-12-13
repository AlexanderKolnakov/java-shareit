package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long userId, Item item);

    ItemDto updateItem(Long ownerId, Long itemId, ItemUpdateDto itemUpdateDto);

    List<ItemDto> findAllItem(Long ownerId);

    ItemDto getUserItem(Long ownerId, Long itemId);

    List<ItemDto> getItemSearchByDescription(String text);
}
