package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    ItemDto createItem(Item item);

    ItemDto updateItem(Long ownerId, Long itemId, Item item);

    List<ItemDto> findAllItem(Long ownerId);

    ItemDto getItem(Long ownerId, Long itemId);

    List<ItemDto> getItemSearch(String text);
}
