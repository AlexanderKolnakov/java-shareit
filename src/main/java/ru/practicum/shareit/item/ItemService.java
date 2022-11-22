package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

public interface ItemService {
    Item createItem(Long userId, Item item);
}
