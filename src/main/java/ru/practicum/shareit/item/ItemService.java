package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, Item item);

    Item updateItem(Long ownerId, Long itemId, Item item);

    List<Item> findAllItem(Long ownerId);

    Item getUserItem(Long ownerId, Long itemId);

    List<Item> getItemSearchByDescription(String text);
}
