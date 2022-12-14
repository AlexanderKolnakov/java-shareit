package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public ItemDto createItem(Long ownerId, Item item) {
        checkOwnerId(ownerId);
        item.setOwner(ownerId);
        return itemDao.createItem(item);
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, Item item) {
        checkOwnerId(ownerId);
        return itemDao.updateItem(ownerId, itemId, item);
    }

    @Override
    public List<ItemDto> findAllItem(Long ownerId) {
        checkOwnerId(ownerId);
        return itemDao.findAllItem(ownerId);
    }

    @Override
    public ItemDto getUserItem(Long ownerId, Long itemId) {
        checkOwnerId(ownerId);
        return itemDao.getItem(ownerId, itemId);
    }

    @Override
    public List<ItemDto> getItemSearchByDescription(String text) {
        return itemDao.getItemSearch(text.toLowerCase());
    }

    private void checkOwnerId(Long ownerId) {
        userDao.checkUserID(ownerId);
    }
}
