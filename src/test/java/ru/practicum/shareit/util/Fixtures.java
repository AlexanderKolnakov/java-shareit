package ru.practicum.shareit.util;

import ru.practicum.shareit.model.User;

public class Fixtures {

    public static User getUser() {
        User user = new User();
        user.setName("Test NAME");
        user.setEmail("test@email.ru");
        user.setId(1L);
        return user;
    }
}
