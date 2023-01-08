package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemDaoImpl implements ItemDao {

    private final Map<Long, Item> itemMap = new HashMap<>();

    private long itemsID = 1;

    @Override
    public ItemDto createItem(Item item) {
        generateID(item);
        itemMap.put(item.getId(), item);
        log.debug("Вещь с id {} и названием {} успешно добавлена.", item.getId(), item.getName());
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, Item item) {
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
        return ItemMapper.toItemDto(updateItem);
    }

    @Override
    public List<ItemDto> findAllItem(Long ownerId) {
        return itemMap.values().stream().map(ItemMapper::toItemDto)
                .filter(e -> e.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItem(Long ownerId, Long itemId) {
        return ItemMapper.toItemDto(itemMap.get(itemId));
    }

    @Override
    public List<ItemDto> getItemSearch(String text) {
        return itemMap.values().stream().map(ItemMapper::toItemDto)
                .filter(e -> e.getDescription().toLowerCase().contains(text) || e.getName().toLowerCase().contains(text))
                .filter(ItemDto::getAvailable)
                .collect(Collectors.toList());
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
