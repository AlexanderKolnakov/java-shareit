package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item createItem(Item item);

    Item updateItem(Long ownerId, Long itemId, Item item);

    List<Item> findAllItem(Long ownerId);

    Item getItem(Long ownerId, Long itemId);

    List<Item> getItemSearch(String text);
}
