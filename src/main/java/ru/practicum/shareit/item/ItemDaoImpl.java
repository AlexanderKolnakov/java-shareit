package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

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

    private void generateID(Item item) {
        item.setId(itemsID);
        itemsID++;
    }
}
