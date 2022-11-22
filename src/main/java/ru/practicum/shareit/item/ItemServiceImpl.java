package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDao;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public Item createItem(Long ownerId, Item item) {
        checkOwnerId(ownerId);
        item.setOwner(ownerId);
        return itemDao.createItem(item);
    }

    private void checkOwnerId(Long ownerId) {
        userDao.checkUserID(ownerId);
    }
}
