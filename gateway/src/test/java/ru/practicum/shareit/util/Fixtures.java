package ru.practicum.shareit.util;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


public class Fixtures {

    public static User getUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test NAME");
        user.setEmail("test@email.ru");
        return user;
    }

    public static Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test NAME");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(1L);
        return item;
    }
}
