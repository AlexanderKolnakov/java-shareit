package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemDaoImpl implements ItemDao {

    private final Map<Long, Item> itemMap = new HashMap<>();
    private long itemsID = 1;

    @Override
    public Item createItem(Item item) {
        generateID(item);
        itemMap.put(item.getId(), item);
        log.debug("Вещь с id {} и названием {} успешно добавлена.", item.getId(), item.getName());
        return item;
    }

    @Override
    public Item updateItem(Long ownerId, Long itemId, Item item) {
        checkItemID(itemId);
        checkItemOwner(ownerId, itemId);
        Item updateItem = itemMap.get(itemId);

        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        itemMap.put(itemId, updateItem);
        return updateItem;
    }

    @Override
    public List<Item> findAllItem(Long ownerId) {
        return itemMap.values().stream().filter(e -> e.getOwner().equals(ownerId)).collect(Collectors.toList());
    }

    @Override
    public Item getItem(Long ownerId, Long itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public List<Item> getItemSearch(String text) {
        return itemMap.values().stream().
                filter(e -> e.getDescription().toLowerCase().contains(text) || e.getName().toLowerCase().contains(text)).
                filter(Item::getAvailable).
                collect(Collectors.toList());
    }

    private void generateID(Item item) {
        item.setId(itemsID);
        itemsID++;
    }
    private void checkItemID(Long id) {
        if (!itemMap.containsKey(id)) {
            throw new UserNotFoundException("Вещи с id " + id + " не существует.");
        }
    }
    private void checkItemOwner(Long ownerId, Long itemId) {
        if (!itemMap.get(itemId).getOwner().equals(ownerId)) {
            throw new UserNotFoundException("У пользователя с id " + ownerId + " нет вещи с id " + itemId);
        }
    }
}
